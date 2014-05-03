/* ExpensesTransactionData is part of ExpensesReporter and is the DAO for transaction data.
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by nicola on 03/05/14.
 */
public class ExpensesTransactionData {

    private SQLiteDatabase database;
    private ExpensesSQLiteHelper dbHelper;

    public ExpensesTransactionData(Context context)
    {
        dbHelper = new ExpensesSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTransaction(String source, String destination, double amount) {
        ContentValues values = new ContentValues();
        values.put(ExpensesSQLiteHelper.TRANSACTIONS_Source, source);
        values.put(ExpensesSQLiteHelper.TRANSACTIONS_Destination, destination);
        values.put(ExpensesSQLiteHelper.TRANSACTIONS_Amount, amount);
        database.insert(ExpensesSQLiteHelper.TABLE_TRANSACTIONS, null, values);

    }
}
