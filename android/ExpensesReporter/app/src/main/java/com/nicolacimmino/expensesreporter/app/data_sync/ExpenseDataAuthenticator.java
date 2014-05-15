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

import com.nicolacimmino.expensesreporter.app.ui.ExpenseDataLoginActivity;

/**
 * Created by nicola on 06/05/2014.
 */
public class ExpenseDataAuthenticator extends AbstractAccountAuthenticator {

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
    // Don't add additional accounts
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse response,
            String accountType,
            String authTokenType,
            String[] strings,
            Bundle bundle) throws NetworkErrorException {

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
        return "tokenfromserverhere";
    }
}
