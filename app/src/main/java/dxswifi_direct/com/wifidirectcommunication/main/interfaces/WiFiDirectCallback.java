package dxswifi_direct.com.wifidirectcommunication.main.interfaces;

/**
 * Created by Deepak Sharma on 14/6/16.
 */

public interface WiFiDirectCallback {

    public void onWifiP2PStateChangedAction();
    public void onWifiP2PPeersChangedAction();
    public void onWifiP2PConnectionChangedAction();
    public void onWifiP2PThisDeviceChangedAction();
}
