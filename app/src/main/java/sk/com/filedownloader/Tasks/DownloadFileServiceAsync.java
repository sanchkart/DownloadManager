package sk.com.filedownloader.Tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sk.com.filedownloader.Classes.MyList;
import sk.com.filedownloader.Helper.Constants;
import sk.com.filedownloader.Classes.HttpClientDownload;
import sk.com.filedownloader.Services.DownloadBackgroundService;

/**
 * Created by sanchirkartiev on 02/04/15.
 */
public class DownloadFileServiceAsync extends AsyncTask<String,Integer, File> {

    private DownloadBackgroundService service;

    public DownloadFileServiceAsync(Context context,DownloadBackgroundService service) {
        this.service = service;

    }

    @Override
    protected File doInBackground(String... params) {
        String currentVersion = "";
        String serverVersion = "";

        // нужно добавить в параметры версию текущую и версию на сервере
        if(params.length < 2)
        {
            return null;
        }

        String application = params[1];
        switch (application) {
            case Constants.TAXI_INDIGO:
                serverVersion = service.getTaxiIndigo();
                currentVersion = service.getCurrentTaxiIndigo();
                Log.i("SET TAXI INDIGO",serverVersion);
                break;
            case Constants.MOBI_LOCK:
                serverVersion = service.getMobyLock();
                currentVersion = service.getCurrentMobyLock();
                Log.i("MOBYLOCK", serverVersion);
                break;
            case Constants.MINISTRO:
                serverVersion = service.getMinistro();
                currentVersion = service.getCurrentMinistro();
                Log.i("MINISTRO",serverVersion);
                break;
            default:
                break;
        }

        // если текущая версия больше или равна той, что на сервер
        if(currentVersion.compareToIgnoreCase(serverVersion) >= 0) {
            return null;
        }

        HttpClient httpClient = HttpClientDownload.getHttpClient();
        File file = new File("");
        HttpGet request = null;
        try {
            request = new HttpGet(params[0]);
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(httpParams, 60000);
            request.setParams(httpParams);

            publishProgress(25);

            HttpResponse response = httpClient.execute(request);

            publishProgress(50);

            byte[] stream = EntityUtils.toByteArray(response.getEntity());

            publishProgress(75);
            file = streamToFile(stream,application);

            publishProgress(100);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //MyList.getInstance().list.put(application,file);
        /*StartIntentAsync intentAsync = new StartIntentAsync(service,file);
        intentAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/


        return file;
    }
    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if(result != null)
        {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(Uri.parse("file://" + result.getAbsolutePath()), "application/vnd.android.package-archive");
            Log.i("FILE_ABSOLUT", result.getAbsolutePath());
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            service.startActivity(newIntent);
        }
        //mainActivity.startInstallation(result);
    }
    private File streamToFile(byte[] stream, String application) throws IOException {
        String fileName = null;
        switch (application) {
            case Constants.TAXI_INDIGO:
                fileName = "/indigo.apk";
                break;
            case Constants.MOBI_LOCK:
                fileName = "/mobilock.apk";
                break;
            case Constants.MINISTRO:
                fileName = "/ministro.apk";
                break;
            default:
                break;
        }
        String pathToUpdate = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getPath() + fileName;
        File file = new File(pathToUpdate);
        FileOutputStream out = new FileOutputStream(file);
        out.write(stream);
        out.flush();
        out.close();
        return file;
    }
}
