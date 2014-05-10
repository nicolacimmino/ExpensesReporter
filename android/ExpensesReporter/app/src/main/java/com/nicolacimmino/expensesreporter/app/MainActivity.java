/* ExpensesTransaction is part of ExpensesReporter and models a single transaction.
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
package com.nicolacimmino.expensesreporter.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private ExpensesTransactionData transactionData;

    public static final String AUTHORITY = "com.nicolacimmino.expensesreporter.app.ExpenseDataSyncAdapter";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";

    Account mAccount;

    ContentResolver mResolver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionData = new ExpensesTransactionData(this);
        transactionData.open();

        mAccount = CreateSyncAccount(this);

        // Get the content resolver for your app
        mResolver = getContentResolver();
        // Turn on automatic syncing for the default account and authority
        mResolver.setSyncAutomatically(mAccount, AUTHORITY, true);


        Spinner spinner = (Spinner) findViewById(R.id.sourceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.transaction_sources, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.destinationSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_destinations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_submit) {
            onTransactionConfirmClick();
            return true;
        }

        if(id == R.id.action_show_expenses)
        {
            Intent intent = new Intent(this, ExpensesListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public String getSelectedText(RadioGroup radioGroup)
    {
        for(int x=0; x<radioGroup.getChildCount(); x++)
        {
            ToggleButton toggleButton = (ToggleButton)radioGroup.getChildAt(x);
            if(toggleButton.isChecked())
            {
                return (String)toggleButton.getTag();
            }
        }
        return "";
    }

    public void onTransactionConfirmClick()
    {
        try {
            TextView amountView = (TextView) findViewById(R.id.textAmount);
            Spinner sourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
            Spinner destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);
            TextView notesView = (TextView) findViewById(R.id.textDescription);
            TextView currencyView = (TextView) findViewById(R.id.textAmountCurrency);

            transactionData.addTransaction(sourceSpinner.getSelectedItem().toString(),
                                            destinationSpinner.getSelectedItem().toString(),
                                            Double.parseDouble(amountView.getText().toString()),
                                            notesView.getText().toString(),
                                            currencyView.getText().toString());
            amountView.setText("");
            Toast.makeText(getApplicationContext(), "Saved.", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Invalid input.", Toast.LENGTH_SHORT).show();
        }
    }

}
