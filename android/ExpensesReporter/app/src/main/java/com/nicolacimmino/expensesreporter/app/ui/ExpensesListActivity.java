package com.nicolacimmino.expensesreporter.app.ui;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.nicolacimmino.expensesreporter.app.data_model.ExpenseDataContract;
import com.nicolacimmino.expensesreporter.app.R;

public class ExpensesListActivity extends ListActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = getContentResolver().query(ExpenseDataContract.Expense.CONTENT_URI,
                ExpenseDataContract.Expense.COLUMN_NAME_ALL, null, null, ExpenseDataContract.Expense.COLUMN_NAME_TIMESTAMP + " DESC");

        ExpensesTransactionCursorAdapter adapter = new ExpensesTransactionCursorAdapter(this, R.layout.expeses_transactions_row, cursor, 0);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}