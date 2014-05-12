/* ExpenseDataContract is part of ExpensesReporter and provides the expenses data provider contract.
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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract defining the interface of the ExpenseData data provider.
 * Here we define URIs, columns names and other constants that define the data exposed
 *  by the Expense Data Provider.
 */
public class ExpenseDataContract {

    // This is the content authority that applications will use to invoke our content provider.
    public static final String CONTENT_AUTHORITY ="com.nicolacimmino.expensesreporter.provider";

    // The base URI of all resources exposed by this content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Path of the expenses content
    public static final String PATH_EXPENSES = "expenses";

    public static class Expense implements BaseColumns {

        // MIME type for the Expenses content. "vnd" is for "vendor specific"
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.expensereporter.expenses";

        // MIME type for the single Expense content. "vnd" is for "vendor specific"
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.expensereporter.expense";

        // URI for expenses resources.
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_EXPENSES).build();

        // Table where expense data is saved.
        public static final String TABLE_NAME = "expenses";

        // Name of the columns of data in an expense
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_DESTINATION = "destination";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CURRENCY = "currency";
        public static final String COLUMN_NAME_SYNC = "sync";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        // All table columns.
        public static final String[] COLUMN_NAME_ALL = {
                                        COLUMN_NAME_ID,
                                        COLUMN_NAME_SOURCE,
                                        COLUMN_NAME_DESTINATION,
                                        COLUMN_NAME_TIMESTAMP,
                                        COLUMN_NAME_AMOUNT,
                                        COLUMN_NAME_CURRENCY,
                                        COLUMN_NAME_SYNC,
                                        COLUMN_NAME_DESCRIPTION};
    }
}
