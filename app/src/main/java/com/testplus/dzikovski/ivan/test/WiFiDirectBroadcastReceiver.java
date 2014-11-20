package com.testplus.dzikovski.ivan.test;

import android.content.BroadcastReceiver;
import 	android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import 	android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.util.Log;
import java.util.Iterator;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.net.wifi.p2p.WifiP2pManager.Channel;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private MainActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
                                       MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Toast.makeText(context,"Wifi Direct ON",Toast.LENGTH_LONG).show();
                mActivity.setWifiDirectEnabled(true);
            } else {
                // Wi-Fi P2P is not enabled
                Toast.makeText(context,"Wifi Direct OFF",Toast.LENGTH_LONG).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {

                    //Updating UI
                    if (mActivity.tvAvailableDevices!=null) mActivity.tvAvailableDevices.setVisibility(View.VISIBLE);
                    if (mActivity.viewAvailableDevices!=null) mActivity.viewAvailableDevices.setVisibility(View.VISIBLE);
                    if (mActivity.lvAvailableDevices!=null) mActivity.lvAvailableDevices.setVisibility(View.VISIBLE);



                    ArrayList<WifiP2pDevice> deviceList=new ArrayList<WifiP2pDevice>();
                    deviceList.addAll(peers.getDeviceList());
                    //ArrayAdapter<WifiP2pDevice> arrayAdapter=new ArrayAdapter<WifiP2pDevice>(mActivity,android.R.layout.simple_list_item_1,deviceList);
                    DeviceListAdapter arrayAdapter=new DeviceListAdapter(mActivity,deviceList);
                    mActivity.lvAvailableDevices.setAdapter(arrayAdapter);


                    if (mActivity.progressDialogDiscover!=null) {
                        mActivity.progressDialogDiscover.dismiss();
                    }


                    /*ArrayList<String> itemList=new ArrayList<String>();
                    for (int i=0; i<=33; i++){
                        itemList.add(i,"Item "+Integer.toString(i));
                    }

                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemList);
                    lvAvailableDevices.setAdapter(arrayAdapter);*/



                    Log.d("WIFI_P2P_PEERS_CHANGED_ACTION",String.format("%d peers available",peers.getDeviceList().size()));
                    mActivity.peers.clear();
                    mActivity.peers.addAll(peers.getDeviceList());
                    /*Collection<WifiP2pDevice> deviceList= peers.getDeviceList();
                    Iterator iterator=deviceList.iterator();
                    if (deviceList!=null) {
                        WifiP2pDevice device=(WifiP2pDevice) iterator.next();
                        Log.d("WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION",device.deviceName);

                    }*/
                }
            });

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (!networkInfo.isConnected()){
                return;
            }

            mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                @Override
                public void onConnectionInfoAvailable(WifiP2pInfo info) {
                    mActivity.info=info;
                    mActivity.connect(info);
                }
            });

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

}