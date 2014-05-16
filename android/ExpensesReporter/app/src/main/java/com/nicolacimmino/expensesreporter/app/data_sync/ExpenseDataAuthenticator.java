/* ExpenseDataAuthenticator is part of ExpensesReporter and is the account authenticator.
 *   Copyright (C) 2014 Nicola Cimmino
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */
package com.nicolacimmino.expensesreporter.app.data_sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.nicolacimmino.expensesreporter.app.data_model.ExpenseDataContract;
import com.nicolacimmino.expensesreporter.app.ui.ExpenseDataLoginActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExpenseDataAuthenticator extends AbstractAccountAuthenticator {

    private static final String TAG = "ExpenseDataAuthenticator";

    private Context mContext;

    public ExpenseDataAuthenticator(Context context) {
        super(context);
        mContext = context;
    }
    // Editing properties is not supported
    @Override
    public Bundle editProperties(
            AccountAuthenticatorResponse r, String s) {
        throw new UnsupportedOperationException();
    }

    // Invoked when a new account is to be added.
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse response,
            String accountType,
            String authTokenType,
            String[] strings,
            Bundle bundle) throws NetworkErrorException {

        // We want to start the ExpenseDataLoginActivity and pass info about the account
        //  to be created. We don't start the activity here
        //  but return a bundle for the AccountManager to actually start it.
        final Intent intent = new Intent(mContext, ExpenseDataLoginActivity.class);
        intent.putExtra(ExpenseDataLoginActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(ExpenseDataLoginActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(ExpenseDataLoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle returnBundle = new Bundle();
        returnBundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return returnBundle;
    }

    // Ignore attempts to confirm credentials
    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }
    // Getting an authentication token is not supported
    @Override
    public Bundle getAuthToken(
            AccountAuthenticatorResponse response,
            Account account,
            String authTokenType,
            Bundle bundle) throws NetworkErrorException {

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                authToken = signInUser(account.name, password, authTokenType);
            }
        }

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = new Intent(mContext, ExpenseDataLoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(ExpenseDataLoginActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(ExpenseDataLoginActivity.ARG_AUTH_TYPE, authTokenType);
        final Bundle responseBundle = new Bundle();
        responseBundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return responseBundle;
    }
    // Getting a label for the auth token is not supported
    @Override
    public String getAuthTokenLabel(String s) {
        throw new UnsupportedOperationException();
    }
    // Updating user credentials is not supported
    @Override
    public Bundle updateCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
    // Checking features for the account is not supported
    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse r,
            Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    private String signInUser(String name, String password, String authTokenType)
    {
        String authToken = "";

        HttpURLConnection connection = null;

        try {

            Map<String,Object> params = new LinkedHashMap<String,Object>();
            params.put("name", name);
            params.put("password", password);
            params.put("type", authTokenType);

            // URLencode and place all params in a string like name=value&name=value...
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            URL url = new URL("https://intra.nicolacimmino.com/report_authorize.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(postDataBytes.length));
            connection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
            wr.write(postDataBytes);
            wr.flush();
            wr.close();

            int response = connection.getResponseCode();
            authToken = connection.getResponseMessage();
            Log.i(TAG, String.valueOf(response));
            connection.disconnect();
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL is malformed", e);
            return "";
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            return "";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        Log.i(TAG, "Got token:" + authToken);

        return authToken;
    }
}
