package com.testplus.dzikovski.ivan.test;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ivan on 19.11.2014.
 */
public class QuestionAsyncTask extends AsyncTask {

    private Context context;
    ServerSocket serverSocket;

    @Override
    protected Object doInBackground(Object[] params) {

        try {

            serverSocket=new ServerSocket(8888);
            Socket client=serverSocket.accept();



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
