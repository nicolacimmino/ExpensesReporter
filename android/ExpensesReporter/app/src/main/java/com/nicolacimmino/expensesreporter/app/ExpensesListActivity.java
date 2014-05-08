package com.nicolacimmino.expensesreporter.app;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import org.w3c.dom.Comment;

import java.util.List;

public class ExpensesListActivity extends ListActivity  {

    private ExpensesTransactionData datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasource = new ExpensesTransactionData(this);
        datasource.open();

        List<ExpensesTransaction> values = datasource.getAllTransactions();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        //ArrayAdapter<ExpensesTransaction> adapter = new ArrayAdapter<ExpensesTransaction>(this,
        //        R.xml.expenses_list_item, values);

        ExpensesTransactionArrayAdapter adapter = new ExpensesTransactionArrayAdapter(this,
                R.xml.expenses_list_item, values);

        setListAdapter(adapter);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}