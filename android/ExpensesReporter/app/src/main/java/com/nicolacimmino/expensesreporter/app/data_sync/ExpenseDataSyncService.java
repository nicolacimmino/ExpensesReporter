/* ExpenseDataSyncService is part of ExpensesReporter and is the sync service to sync data to
 *   the server.
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

package com.nicolacimmino.expensesreporter.app.data_sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This is the sync service that is responsible to provide binding to the sync adapter.
 */
public class ExpenseDataSyncService extends Service {

    private static ExpenseDataSyncAdapter syncAdapter = null;
    private static final Object syncAdapterLock = new Object();

    // Tag used for logging so we can filter messages from this class.
    public static final String TAG = "ExpenseDataSyncService";

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new ExpenseDataSyncAdapter(getApplicationContext(), true);
            }
        }
        Log.i(TAG, "Service created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service bound to sync adapter");
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        super.onDestroy();
    }
}
