package com.anlisoft.vsafe.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;

import com.anlisoft.vsafe.models.data.ConnectionModel;

import static com.anlisoft.vsafe.models.data.Global.MobileData;
import static com.anlisoft.vsafe.models.data.Global.WifiData;

public class ConnectionLiveData extends LiveData<ConnectionModel> {

    private Context context;
    private NetworkInfo activeDataNetwork = null;

    public ConnectionLiveData(Context context) {
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getExtras() != null) {
                NetworkInfo activeNetwork = (NetworkInfo) intent
                        .getExtras()
                        .get(ConnectivityManager.EXTRA_NETWORK_INFO);

                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context
                                .getSystemService(
                                        Context.CONNECTIVITY_SERVICE
                                );

                if (connectivityManager != null) {
                    activeDataNetwork = connectivityManager.getActiveNetworkInfo();
                }

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                boolean isDataConnected = activeDataNetwork != null &&
                        activeDataNetwork.isConnectedOrConnecting();

                if (isConnected && isDataConnected) {

                    switch (activeNetwork.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            postValue(new ConnectionModel(WifiData, true));
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            postValue(new ConnectionModel(MobileData, true));
                            break;
                    }
                } else {

                    postValue(new ConnectionModel(0, false));
                }
            }
        }
    };

}
