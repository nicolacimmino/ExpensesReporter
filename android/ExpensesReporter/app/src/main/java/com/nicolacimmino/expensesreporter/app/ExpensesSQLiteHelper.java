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

package com.nicolacimmino.expensesreporter.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nicola on 03/05/14.
 */
public class ExpensesSQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Expenses";
    public static final int DATABASE_VERSION = 4;

    // Transactions table.
    public static final String TABLE_TRANSACTIONS = "Transactions";
    public static final String TRANSACTIONS_ID = "id";
    public static final String TRANSACTIONS_Timestamp = "timstamp";
    public static final String TRANSACTIONS_SyncDone = "syncdone";
    public static final String TRANSACTIONS_Source = "source";
    public static final String TRANSACTIONS_Destination = "destination";
    public static final String TRANSACTIONS_Amount = "amount";

    public static final String[] ALL_TRANSACTIONS_COLS = {
                TRANSACTIONS_ID,
                TRANSACTIONS_Source,
                TRANSACTIONS_Destination,
                TRANSACTIONS_Amount,
                TRANSACTIONS_Timestamp,
                TRANSACTIONS_SyncDone };

    // Statement to create the database.
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRANSACTIONS + "("
            + TRANSACTIONS_ID + " integer primary key autoincrement, "
            + TRANSACTIONS_Timestamp + " timestamp default current_timestamp,"
            + TRANSACTIONS_SyncDone + " text not null default '0',"
            + TRANSACTIONS_Source + " text not null,"
            + TRANSACTIONS_Destination + " text not null,"
            + TRANSACTIONS_Amount + " text not null);";

    public ExpensesSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(sqLiteDatabase);
    }
}
