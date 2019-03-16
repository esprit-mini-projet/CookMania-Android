package com.esprit.cookmania.helpers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetCheckTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "InternetCheckTask";

    private Consumer mConsumer;

    public interface Consumer {
        void accept(Boolean internet);
    }

    public InternetCheckTask(Consumer consumer) {
        mConsumer = consumer;
        execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1000);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean internet) {
        mConsumer.accept(internet);
    }
}
