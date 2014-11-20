package com.testplus.dzikovski.ivan.test;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ivan on 17.11.2014.
 */
public class DeviceListAdapter extends ArrayAdapter<WifiP2pDevice> {
    private final Context context;
    private final ArrayList<WifiP2pDevice> values;

    public DeviceListAdapter(Context context,  ArrayList<WifiP2pDevice> values) {
        super(context,R.layout.device_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.device_list_item, parent, false);
        TextView tw=(TextView) rowView.findViewById(R.id.text1);
        tw.setText(values.get(position).deviceName);
        return rowView;
    }
}
