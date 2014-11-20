package com.testplus.dzikovski.ivan.test;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.testplus.dzikovski.ivan.test.model.Question;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Ivan on 11.11.2014.
 */
public class ConnectAsyncTask extends AsyncTask {
    private Context context;
    private InetAddress ownerAddress;
    private int portNumber;
    private String enteredPin;

    public ConnectAsyncTask(Context context, InetAddress ownerAddress, int portNumber, String enteredPin) {
        this.context = context;
        this.ownerAddress = ownerAddress;
        this.portNumber = portNumber;
        this.enteredPin = enteredPin;
    }

    Socket socket=new Socket();

    @Override
    protected Object doInBackground(Object[] params) {
        try {


            socket.bind(null);
            socket.connect((new InetSocketAddress(ownerAddress, 8888)));
            //socket.connect((new InetSocketAddress("192.168.100.43",8888)));

            OutputStream os=socket.getOutputStream();
            OutputStreamWriter osw=new OutputStreamWriter(os);
            BufferedWriter bw=new BufferedWriter(osw);
            bw.write(enteredPin);
            bw.newLine();
            bw.flush();

            InputStream is=socket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br= new BufferedReader(isr);
            String statusMessage=br.readLine();


            return statusMessage;




        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        String statusMessage=(String) o;
        MainActivity mActivity=(MainActivity) context;
        mActivity.progressDialogConnect.dismiss();
        Toast.makeText(context,statusMessage,Toast.LENGTH_LONG).show();
        if (statusMessage.equals("Successfully connected!")){
            Intent intent=new Intent(mActivity,QuestionActivity.class);
            mActivity.startActivity(intent);
        }
    }
}
