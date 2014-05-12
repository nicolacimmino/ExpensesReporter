package com.nicolacimmino.expensesreporter.app.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.nicolacimmino.expensesreporter.app.data_model.ExpensesTransaction;
import com.nicolacimmino.expensesreporter.app.data_model.ExpensesTransactionData;
import com.nicolacimmino.expensesreporter.app.R;

import java.util.List;

public class ExpensesListActivity extends ListActivity  {

    private ExpensesTransactionData datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasource = new ExpensesTransactionData(this);

        List<ExpensesTransaction> values = datasource.getAllTransactions();

        ExpensesTransactionArrayAdapter adapter = new ExpensesTransactionArrayAdapter(this,
                R.layout.expeses_transactions_row, values);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}