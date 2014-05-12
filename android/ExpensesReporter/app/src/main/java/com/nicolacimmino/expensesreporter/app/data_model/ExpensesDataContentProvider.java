package com.nicolacimmino.expensesreporter.app.data_model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


/**
 * Content provider for expenses data.
 */
public class ExpensesDataContentProvider extends ContentProvider {

    private ExpensesSQLiteHelper databaseHelper;

    // helper constants for use with the UriMatcher
    private static final int EXPENSES_LIST = 1;
   // private static final UriMatcher URI_MATCHER;

    // prepare the UriMatcher
/*    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(ExpenseDataContract.AUTHORITY, "expenses", EXPENSES_LIST);
    }
*/
    /*
   * Always return true, indicating that the
   * provider loaded correctly.
   */
    @Override
    public boolean onCreate() {
 //       databaseHelper = new ExpensesSQLiteHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        /*switch (URI_MATCHER.match(uri)) {
            case EXPENSES_LIST: return ExpenseDataContract.Transactions.CONTENT_TYPE;
            default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }*/
        return new String();
    }

    /*
         * query() always returns no results
         *
         */
    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        return null;
    }
    /*
     * insert() always returns null (no URI)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
    /*
     * delete() always returns "no rows affected" (0)
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
    /*
     * update() always returns "no rows affected" (0)
     */
    public int update(
            Uri uri,
            ContentValues values,
            String selection,
            String[] selectionArgs) {
        return 0;
    }
}
