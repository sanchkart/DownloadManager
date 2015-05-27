package sk.com.filedownloader.Services;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import sk.com.filedownloader.Classes.HttpClientDownload;
import sk.com.filedownloader.Helper.UiHelper;

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        String currentElement = intent.getStringExtra("CURRENT");
        String newElement = intent.getStringExtra("NEW");
        HttpClient httpClient = HttpClientDownload.getHttpClient();
        File file = new File("");
        try {
            HttpGet request = new HttpGet(urlPath);
            Log.d("NETWORK REQUEST", urlPath);
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(httpParams, 60000);
            request.setParams(httpParams);


            HttpResponse response = httpClient.execute(request);


            byte[] stream = EntityUtils.toByteArray(response.getEntity());

            file = streamToFile(stream);

            Log.d("FILECOMPLETED", "COMPLETE");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("CURRENT ELEMENT",currentElement + " "+ newElement);
        if(!currentElement.equalsIgnoreCase(newElement) || currentElement.compareTo(newElement) >0)
        {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
            Log.d("FILE", file.getAbsolutePath());
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    UiHelper.showToast(getApplicationContext(), "This application has a new version");
                }
            });

        }

    }
    private File streamToFile(byte[] stream) throws IOException {

        String pathToUpdate = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getPath() + "/update.apk";
        File file = new File(pathToUpdate);
        FileOutputStream out = new FileOutputStream(file);
        out.write(stream);
        out.flush();
        out.close();
        return file;
    }
}