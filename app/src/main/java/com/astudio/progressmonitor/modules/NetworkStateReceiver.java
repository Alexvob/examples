package com.astudio.progressmonitor.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.astudio.progressmonitor.R;

import java.util.Objects;

public class NetworkStateReceiver extends BroadcastReceiver {


    private static final String TAG = "NetworkStateReceiver";
    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        //Log.e(TAG, "onReceive!");


        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
            //boolean isConnected = Objects.requireNonNull(activeNetwork).isConnectedOrConnecting();

            boolean isConnected = false;

            if (cm != null) {
                NetworkInfo[] info = cm.getAllNetworkInfo();
                //if (info != null)
                    for (NetworkInfo networkInfo : info)
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED & networkInfo.isConnected()) {
                            Log.e(TAG, "connect: " + networkInfo.getState());
                            isConnected = true;
                        }
            }

            if (isConnected) {
                App.getInstance().generateNewStatusNotify(context.getResources().getString(R.string.network_status_online));
            } else {
                App.getInstance().generateNewStatusNotify(context.getResources().getString(R.string.network_status_offline));
            }
        }

    }


    public void getNetworkState(){
        Log.e(TAG, "na state");
    }


}
