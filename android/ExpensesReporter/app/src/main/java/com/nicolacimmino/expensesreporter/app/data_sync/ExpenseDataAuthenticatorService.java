package com.nicolacimmino.expensesreporter.app.data_sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.nicolacimmino.expensesreporter.app.data_sync.ExpenseDataAuthenticator;

/**
 * Created by nicola on 07/05/2014.
 */
public class ExpenseDataAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private ExpenseDataAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new ExpenseDataAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
