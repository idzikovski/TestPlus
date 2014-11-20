package com.testplus.dzikovski.ivan.test;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements ConnectDialogFragment.ConnectDialogListener {

    public TextView tvAvailableDevices;
    public View viewAvailableDevices;
    public ListView lvAvailableDevices;
    public WifiP2pInfo info=null;


    public ProgressDialog progressDialogDiscover,progressDialogConnect;



    public List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiP2pDevice mDevice;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    private boolean wifiDirectEnabled=false;
    private boolean isOwnerAddressNull=true;
    private String enteredPin="";

    public boolean isWifiDirectEnabled() {
        return wifiDirectEnabled;
    }

    public void setWifiDirectEnabled(boolean wifiDirectEnabled) {
        this.wifiDirectEnabled = wifiDirectEnabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Retrieving UI
        tvAvailableDevices= (TextView) findViewById(R.id.tvAvailableDevices);
        viewAvailableDevices= (View) findViewById(R.id.viewAvailableDevices);
        lvAvailableDevices = (ListView) findViewById(R.id.lvAvailableDevices);

        //Initializing WiFi Direct manager

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        //Adding onItemClickedListener
        lvAvailableDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    mDevice=(WifiP2pDevice) parent.getAdapter().getItem(position);
                    DialogFragment dialog=new ConnectDialogFragment();

                    Bundle args=new Bundle();
                    args.putString("deviceName",mDevice.deviceName);
                    args.putString("deviceAddress",mDevice.deviceAddress);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(),"ConnectDialogFragment");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                /*final WifiP2pDevice device=(WifiP2pDevice) parent.getAdapter().getItem(position);
                WifiP2pConfig config=new WifiP2pConfig();
                config.deviceAddress=device.deviceAddress;
                config.groupOwnerIntent=0;
                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Connected to: "+device.deviceName, Toast.LENGTH_LONG).show();
                        mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                            @Override
                            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                                Toast.makeText(MainActivity.this, "Group owner: "+Boolean.toString(info.isGroupOwner), Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    @Override
                    public void onFailure(int reason) {

                    }
                });*/
            }
        });

        //Creating dummy items for list

        /*ArrayList<String> itemList=new ArrayList<String>();
        for (int i=0; i<=33; i++){
            itemList.add(i,"Item "+Integer.toString(i));
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemList);
        lvAvailableDevices.setAdapter(arrayAdapter);*/

    }

    public void btnStartScanClicked(View view){


        mManager.discoverPeers(mChannel,new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Toast.makeText(MainActivity.this,getString(R.string.discovery_process_started),Toast.LENGTH_LONG).show();

                progressDialogDiscover=new ProgressDialog(MainActivity.this);
                progressDialogDiscover.setTitle(getString(R.string.progress_dialog_title));
                progressDialogDiscover.setMessage(getString(R.string.progress_dialog_message));
                progressDialogDiscover.setCanceledOnTouchOutside(false);
                progressDialogDiscover.show();
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, final String enteredPin) {

        this.enteredPin=enteredPin;
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = mDevice.deviceAddress;
        config.groupOwnerIntent = 0;

        progressDialogConnect=new ProgressDialog(MainActivity.this);
        progressDialogConnect.setTitle(getString(R.string.connect_dialog_title));
        progressDialogConnect.setMessage(getString(R.string.conect_dialog_text));
        progressDialogConnect.setCanceledOnTouchOutside(false);
        progressDialogConnect.show();

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (info!=null){
                    connect(info);
                }
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public void connect(WifiP2pInfo info){
        Global.ownerAddress = info.groupOwnerAddress;

        if (Global.ownerAddress != null) {
            Log.d("MainActivity ", Global.ownerAddress.toString());
            ConnectAsyncTask asyncTask = new ConnectAsyncTask(MainActivity.this, Global.ownerAddress, 8888, enteredPin);
            asyncTask.execute();
        } else {
            progressDialogConnect.dismiss();
            Toast.makeText(MainActivity.this, "Connection failed! Try again!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
