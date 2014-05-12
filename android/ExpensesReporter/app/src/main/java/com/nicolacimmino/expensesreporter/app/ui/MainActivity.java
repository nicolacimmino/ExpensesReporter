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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nicolacimmino.expensesreporter.app.R;
import com.nicolacimmino.expensesreporter.app.data_model.ExpensesTransactionData;
import com.nicolacimmino.expensesreporter.app.ui.ExpensesListActivity;


public class MainActivity extends Activity {

    // Data provider.
    private ExpensesTransactionData transactionData;
    public static final String AUTHORITY = "com.nicolacimmino.expensesreporter.app.data_sync.ExpenseDataSyncAdapter";
    public static final String ACCOUNT_TYPE = "intra.nicolacimmino.com";
    public static final String ACCOUNT = "dummyaccount";
    Account mAccount;
    ContentResolver mResolver;

    /*
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add values to the source spinner.
        Spinner spinner = (Spinner) findViewById(R.id.sourceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_sources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Add values to the destination spinner.
        spinner = (Spinner) findViewById(R.id.destinationSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_destinations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get ready the data provider.
        transactionData = new ExpensesTransactionData(this);

        mAccount = CreateSyncAccount(this);
        // Get the content resolver for your app
        mResolver = getContentResolver();
        // Turn on automatic syncing for the default account and authority
        //mResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
        mResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                new Bundle(),
                1000);

    }

    public static Account CreateSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
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

            // Create the new transaction.
            transactionData.addTransaction(sourceSpinner.getSelectedItem().toString(),
                    destinationSpinner.getSelectedItem().toString(),
                    Double.parseDouble(amountView.getText().toString()),
                    notesView.getText().toString(),
                    currencyView.getText().toString());

            // Clear the input fields so the interface is ready for another operation.
            amountView.setText("");
            notesView.setText("");

            // We show a toaster as visual feedback that something has happened.
            Toast.makeText(getApplicationContext(), "Saved.", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Invalid input.", Toast.LENGTH_SHORT).show();
        }
    }

}
