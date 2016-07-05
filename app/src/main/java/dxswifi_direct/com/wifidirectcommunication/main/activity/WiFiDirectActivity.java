package dxswifi_direct.com.wifidirectcommunication.main.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dxswifi_direct.com.wifidirectcommunication.R;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;
import dxswifi_direct.com.wifidirectcommunication.main.interfaces.WiFiDirectCallback;
import dxswifi_direct.com.wifidirectcommunication.main.receiver.WiFiDirectBroadcastReceiver;
import static android.R.attr.action;

/**
 * Created by Deepak Sharma on 14/6/16.
 */

public class WiFiDirectActivity extends BaseActivity implements WifiP2pManager.PeerListListener, WiFiDirectCallback {

    private final String TAG = WiFiDirectActivity.this.getClass().getSimpleName();
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    WifiP2pManager.PeerListListener myPeerListListener;

    private ListView mListviewPeers;
    private Button btnDiscover;
    private List peersList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_activity);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(WiFiDirectActivity.this, mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        /*try {
            Method method1 = mManager.getClass().getMethod("enableP2p", WifiP2pManager.Channel.class);
            method1.invoke(mManager, mChannel);
        } catch (Exception e) {

        }*/

        try {
            Class<?> wifiManager = Class
                    .forName("android.net.wifi.p2p.WifiP2pManager");

            Method method = wifiManager
                    .getMethod(
                            "enableP2p",
                            new Class[] { android.net.wifi.p2p.WifiP2pManager.Channel.class });

            method.invoke(mManager, mChannel);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(WiFiDirectActivity.this, "Wifi problem", Toast.LENGTH_SHORT).show();
        }

        mListviewPeers = (ListView)findViewById(R.id.listviewPeers);
        mListviewPeers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        btnDiscover = (Button)findViewById(R.id.btnDiscover);
        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WiFiDirectActivity.this, "Discovery Process Succeded", Toast.LENGTH_SHORT).show();
                        System.out.println("changed");
                    }
                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WiFiDirectActivity.this, "Discovery Process Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* register the broadcast receiver with the intent values to be matched */
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* unregister the broadcast receiver */
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onWifiP2PStateChangedAction() {
        Toast.makeText(this, "P2P State changed.", Toast.LENGTH_SHORT).show();

        if (mManager != null) {
            mManager.requestPeers(mChannel, myPeerListListener);
        }

        /*if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }
        }*/
    }

    @Override
    public void onWifiP2PPeersChangedAction() {

            Toast.makeText(this, "P2P Peers Changed.", Toast.LENGTH_SHORT).show();
        if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            /*if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }*/
        }
    }

    @Override
    public void onWifiP2PConnectionChangedAction() {

            Toast.makeText(this, "P2P Connection changed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWifiP2PThisDeviceChangedAction() {
            Toast.makeText(this, "P2P This device changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Log.i(TAG, ""+peers.toString());
        if (peers!=null)
        {
            peersList.clear();
            peersList.addAll(peers.getDeviceList());
        }
    }
}
