package com.nicolacimmino.expensesreporter.app;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ExpensesTransactionArrayAdapter extends ArrayAdapter<ExpensesTransaction> {

    Context context;
    int layoutResourceId;
    List<ExpensesTransaction> data = null;

    public ExpensesTransactionArrayAdapter(Context context, int layoutResourceId, List<ExpensesTransaction> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ExpenseTransactionItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ExpenseTransactionItemHolder();
            ExpensesTransaction expensesTransaction = data.get(position);
            holder.txtDestination.setText(expensesTransaction.getDestination());
            holder.txtAmount.setText(String.valueOf(expensesTransaction.getAmount()));
            row.setTag(holder);
        }
        else
        {
            holder = (ExpenseTransactionItemHolder)row.getTag();
            ExpensesTransaction expensesTransaction = data.get(position);
            holder.txtDestination.setText(expensesTransaction.getDestination());
            holder.txtAmount.setText(String.valueOf(expensesTransaction.getAmount()));
        }

        return row;
    }

    static class ExpenseTransactionItemHolder
    {
        TextView txtDestination;
        TextView txtAmount;

    }
}
