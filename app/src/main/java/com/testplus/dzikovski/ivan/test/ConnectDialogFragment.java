package com.testplus.dzikovski.ivan.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Ivan on 11.11.2014.
 */
public class ConnectDialogFragment extends DialogFragment {

    private String deviceName;
    private String deviceAddress;
    EditText etEnteredPin;


    public interface ConnectDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, String enteredPin);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.deviceAddress = getArguments().getString("deviceAddress");
        this.deviceName = getArguments().getString("deviceName");


        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_connect, null);
        etEnteredPin = (EditText) view.findViewById(R.id.etEnteredPin);


        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_conect_to))
                .setMessage(deviceName + "\n" + deviceAddress)
                .setPositiveButton(getString(R.string.dialog_connect_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(ConnectDialogFragment.this,etEnteredPin.getText().toString());
                    }
                })
                .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick(ConnectDialogFragment.this);
                    }
                })
                .setView(view);
        return builder.create();
    }

    ConnectDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener=(ConnectDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement ConnectionDialogListener");
        }
    }
}
