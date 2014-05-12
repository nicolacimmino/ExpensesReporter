/* ExpensesSQLiteHelper is part of ExpensesReporter and provides database access support.
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

package com.nicolacimmino.expensesreporter.app.data_model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Wrapper for SQL operations on the Expenses database.
 */
public class ExpensesSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Expenses";
    public static final int DATABASE_VERSION = 7;

    // Statement to create the database.
    private static final String DATABASE_CREATE = "create table "
            + ExpenseDataContract.Expense.TABLE_NAME + "("
            + ExpenseDataContract.Expense.COLUMN_NAME_ID + " integer primary key autoincrement, "
            + ExpenseDataContract.Expense.COLUMN_NAME_TIMESTAMP + " timestamp default current_timestamp,"
            + ExpenseDataContract.Expense.COLUMN_NAME_SYNC + " text not null default '0',"
            + ExpenseDataContract.Expense.COLUMN_NAME_SOURCE + " text not null,"
            + ExpenseDataContract.Expense.COLUMN_NAME_DESTINATION + " text not null,"
            + ExpenseDataContract.Expense.COLUMN_NAME_DESCRIPTION + " text not null,"
            + ExpenseDataContract.Expense.COLUMN_NAME_CURRENCY + " text not null,"
            + ExpenseDataContract.Expense.COLUMN_NAME_AMOUNT + " text not null);";

    public ExpensesSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the database.
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // In this case we have a very simple policy to drop the database
        //  and recreate it if the version is changed. This means user data is
        //  lost. This is fine as the data is backed by the server.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseDataContract.Expense.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
