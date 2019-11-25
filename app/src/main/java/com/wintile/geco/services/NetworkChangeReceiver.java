package com.wintile.geco.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wintile.geco.Utils.Constants;
import com.wintile.geco.Utils.NetworkUtil;



public abstract class NetworkChangeReceiver extends BroadcastReceiver {
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String status = NetworkUtil.getConnectivityStatusString(context);

        Log.e("Receiver ", "" + status);

        if (status.equals(Constants.NOT_CONNECT)) {
            Log.e("Receiver ", "not connection");// your code when internet lost

            change();
        } else {
            dismissSnack();
            Log.e("Receiver ", "connected to internet");//your code when internet connection come back
        }

    }
    protected abstract void change();
    protected abstract void dismissSnack();
}
