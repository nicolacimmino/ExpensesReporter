package com.nicolacimmino.expensesreporter.app.ui;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicolacimmino.expensesreporter.app.data_model.ExpensesTransaction;
import com.nicolacimmino.expensesreporter.app.R;

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
            holder.txtAmount = (TextView)row.findViewById(R.id.expense_row_amount);
            holder.txtDescription = (TextView)row.findViewById(R.id.expense_row_description);
            holder.txtAccounnts = (TextView)row.findViewById(R.id.expense_row_accounts);

            row.setTag(holder);
        }
        else
        {
            holder = (ExpenseTransactionItemHolder)row.getTag();
        }


        holder = (ExpenseTransactionItemHolder)row.getTag();
        ExpensesTransaction expensesTransaction = data.get(position);
        holder.txtDescription.setText(expensesTransaction.getDescription());
        holder.txtAmount.setText(String.valueOf(expensesTransaction.getAmount()));
        holder.txtAccounnts.setText(expensesTransaction.getSource().toString() + " > "
                                        + expensesTransaction.getDestination().toString());
        return row;
    }

    static class ExpenseTransactionItemHolder
    {
        TextView txtDescription;
        TextView txtAmount;
        TextView txtAccounnts;

    }
}
