package sk.com.filedownloader.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import sk.com.filedownloader.Helper.Constants;
import sk.com.filedownloader.Services.DownloadBackgroundService;

/**
 * Created by sanchirkartiev on 02/04/15.
 */
public class GetVersionServiceAsync extends AsyncTask<String,Void,String> {

    private DownloadBackgroundService activity;
    private String  application;

    public GetVersionServiceAsync (DownloadBackgroundService activity, String application) {
        this.activity = activity;
        this.application = application;
    }
    @Override
    protected String doInBackground(String... params) {
        String version = null;
        Log.i("GET_INFO","INFO");
        try {
            URL url = new URL(params[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                version = str;
            }
            switch (application) {
                case Constants.TAXI_INDIGO:
                    activity.setTaxiIndigo(version);
                    Log.i("SET TAXI INDIGO",version);
                    break;
                case Constants.MOBI_LOCK:
                    activity.setMobyLock(version);
                    Log.i("MOBYLOCK", version);
                    break;
                case Constants.MINISTRO:
                    activity.setMinistro(version);
                    Log.i("MINISTRO",version);
                    break;
                default:
                    break;
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return version;

    }
}
