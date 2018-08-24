package gr.thekid.billy.newsapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CheckConnectionTask extends AsyncTask<String, Void, Boolean> {

    private boolean online;

    public CheckConnectionTask(boolean online){
        this.online = online;
    }


    @Override
    protected Boolean doInBackground(String... strings) {
        return isOnline();
    }

    @Override
    protected void onPostExecute(Boolean result){
        online = result;
    }


    public boolean isOnline(){
        try{
            int timeoutMilliseconds = 1500;
            Socket testSocket = new Socket();
            SocketAddress address = new InetSocketAddress(Constants.TEST_CONNECTION_ADDRESS, Constants.TEST_CONNECTION_PORT);
            testSocket.connect(address, timeoutMilliseconds);
            testSocket.close();

            return true;
        } catch(IOException exception){
            return false;
        }
    }
}
