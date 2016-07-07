package dxswifi_direct.com.wifidirectcommunication.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

import dxswifi_direct.com.wifidirectcommunication.main.ui.activity.WiFiDirectActivity;
import dxswifi_direct.com.wifidirectcommunication.main.interfaces.WiFiDirectCallback;

/**
 * Created by Deepak Sharma on 14/6/16.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiDirectActivity mActivity;
    public WiFiDirectCallback callback;

    public WiFiDirectBroadcastReceiver(Context context, WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       WiFiDirectActivity activity) {
        super();
        this.callback= (WiFiDirectCallback) context;
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                callback.onWifiP2PStateChangedAction();
            } else {
                // Wi-Fi P2P is not enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                callback.onWifiP2PPeersChangedAction();
            } else {
                /*try {
                    Method method1 = mManager.getClass().getMethod("enableP2p", WifiP2pManager.Channel.class);
                    method1.invoke(mManager, mChannel);
                    //Toast.makeText(getActivity(), "method found",
                    //       Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "method did not found",
                    //   Toast.LENGTH_SHORT).show();
                }*/
                // Wi-Fi P2P is not enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
