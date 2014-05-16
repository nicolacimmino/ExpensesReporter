/* ExpensesDataContentProvider is part of ExpensesReporter is the expenses data provider.
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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Content provider for expenses data.
 */
public class ExpensesDataContentProvider extends ContentProvider {

    // This is an instance of the database helper which is used
    //  to access data in the actual SQLite database.
    ExpensesSQLiteHelper mDataBaseHelper;

    // ID for the route /expenses
    public static final int ROUTE_EXPENSES = 1;

    // ID for the route /expenses/ID
    public static final int ROUTE_EXPENSES_ID = 2;

    // This is the URI matcher that is used to resolve a route to its ID.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ExpenseDataContract.CONTENT_AUTHORITY, "expenses", ROUTE_EXPENSES);
        sUriMatcher.addURI(ExpenseDataContract.CONTENT_AUTHORITY, "expenses/*", ROUTE_EXPENSES_ID);
    }

    @Override
    public boolean onCreate() {
        mDataBaseHelper = new ExpensesSQLiteHelper(getContext());
        return true;
    }

    /*
     * Gets the MIME type of a given URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_EXPENSES: return ExpenseDataContract.Expense.CONTENT_TYPE;
            case ROUTE_EXPENSES_ID: return ExpenseDataContract.Expense.CONTENT_ITEM_TYPE;
            default: throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                            String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_EXPENSES:
                cursor = db.query( ExpenseDataContract.Expense.TABLE_NAME,   // Table
                                            projection,                             // Columns
                                            selection,                              // Selection
                                            selectionArgs,                          // Selection args
                                            null,                                   // Group by
                                            null,                                   // Having
                                            sortOrder);                             // Sort order

                // We set here the notification URI for the cursor
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                return cursor;
            case ROUTE_EXPENSES_ID:
                String id = uri.getLastPathSegment();
                cursor = db.query( ExpenseDataContract.Expense.TABLE_NAME,       // Table
                        projection,                                                     // Columns
                        "WHERE " + ExpenseDataContract.Expense.COLUMN_NAME_ID + "=?",   // Selection
                        new String[]{id},                                               // Selection args
                        null,                                                           // Group by
                        null,                                                           // Having
                        null);                                                          // Sort order

                // We set here the notification URI for the cursor
                cursor.setNotificationUri(getContext().getContentResolver(), uri);

                return cursor;
            default:
                throw new UnsupportedOperationException("Unknonw URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch(match) {
            case ROUTE_EXPENSES:
                long id = db.insertOrThrow(ExpenseDataContract.Expense.TABLE_NAME, null, contentValues);
                result = Uri.parse(ExpenseDataContract.Expense.CONTENT_URI + "/" + id);
                break;
            case ROUTE_EXPENSES_ID:
                throw  new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsAffected;
        switch(match) {
            case ROUTE_EXPENSES:
                rowsAffected = db.delete(ExpenseDataContract.Expense.TABLE_NAME, null, null);
                break;
            case ROUTE_EXPENSES_ID:
                String id = uri.getLastPathSegment();
                rowsAffected = db.delete(ExpenseDataContract.Expense.TABLE_NAME,
                        "WHERE " + ExpenseDataContract.Expense.COLUMN_NAME_ID + "=?",
                        new String[]{id});
                break;
            default:
                throw  new UnsupportedOperationException("Unknonw URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null, false);
        return rowsAffected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        final  int match = sUriMatcher.match(uri);
        int rowsAffected;
        switch (match) {
            case ROUTE_EXPENSES:
                rowsAffected = db.update(ExpenseDataContract.Expense.TABLE_NAME,    // Table
                                            contentValues,                          // Content values
                                            null,                                   // Where clause
                                            null);                                  // Where args
                break;
            case ROUTE_EXPENSES_ID:
                String id = uri.getLastPathSegment();
                rowsAffected = db.update(ExpenseDataContract.Expense.TABLE_NAME,     // Table
                        contentValues,                          // Content values
                        "WHERE " + ExpenseDataContract.Expense.COLUMN_NAME_ID + "=?",// Where clause
                        new String[]{id});                                           // Where args
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null, false);
        return rowsAffected;
    }
}
