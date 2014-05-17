/* MainActivity is part of ExpensesReporter and provides the expenses input interface.
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
package com.nicolacimmino.expensesreporter.app.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nicolacimmino.expensesreporter.app.R;
import com.nicolacimmino.expensesreporter.app.data_model.ExpenseDataContract;
import com.nicolacimmino.expensesreporter.app.data_sync.ExpenseDataAuthenticator;
import com.nicolacimmino.expensesreporter.app.data_sync.ExpenseDataAuthenticatorContract;

import org.w3c.dom.Text;

import java.util.prefs.Preferences;

public class MainActivity extends Activity {

    // Tag used in logs.
    private static final String TAG = "MainActivity";

    private ContentResolver mResolver;

    private Account mAccount;

    // Name of the settings where we save the last UI settings.
    private final static String SAVED_STATE_LAST_SOURCE = "last_source";
    private final static String SAVED_STATE_LAST_DESTINATION = "last_destination";

    /*
     *
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add values to the source spinner.
        Spinner sourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_sources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(adapter);

        // Add values to the destination spinner.
        Spinner destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_destinations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(adapter);

        // Recover from preferences the last used values in UI and restore them if available.
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String lastSource = preferences.getString(SAVED_STATE_LAST_SOURCE, "");
        String lastDestination = preferences.getString(SAVED_STATE_LAST_DESTINATION, "");

        if(lastSource != "") {
            sourceSpinner.setSelection(((ArrayAdapter)sourceSpinner.getAdapter())
                                        .getPosition(lastSource));
        }

        if(lastDestination != "") {
            destinationSpinner.setSelection(((ArrayAdapter) destinationSpinner.getAdapter())
                    .getPosition(lastDestination));
        }

        mAccount = getmAccount();
    }

    private Account getmAccount()
    {
        Account theAccount;

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts =  accountManager.getAccountsByType(ExpenseDataAuthenticatorContract.ACCOUNT_TYPE);

        Log.i(TAG, "Accounts:" + accounts.length);

        if(accounts.length == 0) {
            // There is no account, we need to ask user to set one up.
            Toast.makeText(getApplicationContext(), "Create account!", Toast.LENGTH_SHORT).show();
            accountManager.addAccount(ExpenseDataAuthenticatorContract.ACCOUNT_TYPE,
                    ExpenseDataAuthenticatorContract.AUTHTOKEN_TYPE_FULL_ACCESS,
                    null, null, this, null, null);
            return null;
        }
        else {
            theAccount = accounts[0];
        }

        // Turn on automatic syncing for the default account and authority
        ContentResolver.setIsSyncable(theAccount, ExpenseDataContract.CONTENT_AUTHORITY, 1);
        mResolver.setSyncAutomatically(theAccount, ExpenseDataContract.CONTENT_AUTHORITY, true);

        return theAccount;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store some values that we want to persist across instance of the activity.
        Spinner sourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
        Spinner destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_STATE_LAST_SOURCE, sourceSpinner.getSelectedItem().toString());
        editor.putString(SAVED_STATE_LAST_DESTINATION, destinationSpinner.getSelectedItem().toString());
        editor.commit();
    }

    /*
     * Action bar created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu adding items to the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
     * Action bar item clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // User confirmed the transaction.
        if(id == R.id.action_submit) {
            onTransactionConfirmClick();
            return true;
        }

        // User wants to see the expenses list.
        if(id == R.id.action_show_expenses)
        {
            Intent intent = new Intent(this, ExpensesListActivity.class);
            startActivity(intent);
        }

        // Let base class handle other stuff (eg back button etc.)
        return super.onOptionsItemSelected(item);
    }


    /*
     * User clicked on "OK" to confirm the transaction.
     * Here we save the new transaction and give user visual feedback.
     */
    public void onTransactionConfirmClick()
    {

        try {

            // Get the input controls.
            TextView amountView = (TextView) findViewById(R.id.textAmount);
            Spinner sourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
            Spinner destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);
            TextView notesView = (TextView) findViewById(R.id.textDescription);
            TextView currencyView = (TextView) findViewById(R.id.textAmountCurrency);

            // We cannot accept an empty amount. Give error and don't save.
            // Also set focus on the amount input control so it's faster for the user
            //  to rectify the problem.
            if(amountView.getText().toString().isEmpty()) {
                amountView.setError(getString(R.string.error_amount));
                amountView.requestFocus();
                return;
            }

            // Prepare values and invoke insert on the Expense Data Content Provider
            ContentValues values = new ContentValues();
            values.put(ExpenseDataContract.Expense.COLUMN_NAME_AMOUNT, amountView.getText().toString());
            values.put(ExpenseDataContract.Expense.COLUMN_NAME_SOURCE, sourceSpinner.getSelectedItem().toString());
            values.put(ExpenseDataContract.Expense.COLUMN_NAME_DESTINATION, destinationSpinner.getSelectedItem().toString());
            values.put(ExpenseDataContract.Expense.COLUMN_NAME_DESCRIPTION, notesView.getText().toString());
            values.put(ExpenseDataContract.Expense.COLUMN_NAME_CURRENCY, currencyView.getText().toString());

            getContentResolver().insert(ExpenseDataContract.Expense.CONTENT_URI, values);

            // Clear the input fields so the interface is ready for another operation.
            amountView.setText("");
            notesView.setText("");
            amountView.requestFocus();

            // We show a toast as visual feedback that something has happened.
            Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();

            if(mAccount == null) {
                mAccount = getmAccount();
            }
            
            Bundle extras = new Bundle();
            getContentResolver().requestSync(mAccount, ExpenseDataContract.CONTENT_AUTHORITY, extras);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
