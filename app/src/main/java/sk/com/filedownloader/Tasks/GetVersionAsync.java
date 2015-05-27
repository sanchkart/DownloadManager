package sk.com.filedownloader.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import sk.com.filedownloader.MainActivity;
import sk.com.filedownloader.R;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
public class GetVersionAsync extends AsyncTask<String,Void,String> {

    private Context context;
    private MainActivity activity;
    private int application;

    public GetVersionAsync (Context context, MainActivity activity, int application) {
        this.context  =context;
        this.activity = activity;
        this.application = application;
    }

    @Override
    protected String doInBackground(String... params) {
        String version = null;
        try {
            URL url = new URL(params[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                version = str;
            }
            if(application == 0)
            {
                activity.setMobyLock(version);
            }
            else if(application == 1)
            {
                activity.setTaxiIndigo(version);
            }
            else
            {
                activity.setMinistro(version);
            }

            activity.savePreferences(version, application);


            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return version;
    }
}
