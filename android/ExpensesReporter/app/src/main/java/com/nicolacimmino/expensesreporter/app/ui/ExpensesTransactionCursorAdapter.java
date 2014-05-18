/* ExpensesTransactionCursorAdapter is part of ExpensesReporter.
 * Based on the template included in AndroidStudio.
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

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.nicolacimmino.expensesreporter.app.data_model.ExpenseDataContract;
import com.nicolacimmino.expensesreporter.app.R;
import com.nicolacimmino.expensesreporter.app.data_sync.ExpenseDataAuthenticatorContract;

import java.util.List;

public class ExpensesTransactionCursorAdapter extends ResourceCursorAdapter {

    Context context;
    int layoutResourceId;
    Cursor cursor = null;


    public ExpensesTransactionCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
        this.layoutResourceId = layout;
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtAmount = (TextView)view.findViewById(R.id.expense_row_amount);
        TextView txtDescription = (TextView)view.findViewById(R.id.expense_row_description);
        TextView txtAccounnts = (TextView)view.findViewById(R.id.expense_row_accounts);
        TextView txtTimestamp = (TextView)view.findViewById(R.id.expense_row_timestamp);
        TextView txtSync = (TextView)view.findViewById(R.id.expense_row_sync);

        txtDescription.setText(cursor.getString(cursor.getColumnIndex(ExpenseDataContract.Expense.COLUMN_NAME_DESCRIPTION)));
        txtAmount.setText(cursor.getString(cursor.getColumnIndex(ExpenseDataContract.Expense.COLUMN_NAME_AMOUNT)));
        txtAccounnts.setText(cursor.getString(cursor.getColumnIndex(ExpenseDataContract.Expense.COLUMN_NAME_SOURCE)) + " > "
                + cursor.getString(cursor.getColumnIndex(ExpenseDataContract.Expense.COLUMN_NAME_DESTINATION)));
        txtTimestamp.setText(cursor.getString(cursor.getColumnIndex(ExpenseDataContract.Expense.COLUMN_NAME_TIMESTAMP)));
        if(cursor.getInt(cursor.getColumnIndex(ExpenseDataContract.Expense.COLUMN_NAME_SYNC))==1) {
            txtSync.setText("S");
        }
    }
}
