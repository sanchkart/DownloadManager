package sk.com.filedownloader.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

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

import sk.com.filedownloader.Classes.HttpClientDownload;
import sk.com.filedownloader.Helper.UiHelper;
import sk.com.filedownloader.MainActivity;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
public class DownloadFileAsync extends AsyncTask<String,Integer, File> {

    private Context context;
    private ProgressBar progressBar;
    private MainActivity activity;
    int application;

    public DownloadFileAsync(Context context,MainActivity activity, int application) {
        this.context = context;
        this.activity = activity;
        this.application = application;
    }
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        switch (application) {
            case 0:
                activity.cancelButton.setEnabled(false);
                activity.downloadButton.setEnabled(true);
                break;
            case 1:
                activity.secondCancelButton.setEnabled(false);
                activity.secondAppButton.setEnabled(true);
                break;
            case 2:
                activity.ministroCancelButton.setEnabled(false);
                activity.ministroButton.setEnabled(true);
        }
        //TODO Нам необходимо либо запустить устанвоку всех программ либо отфильтровать уже существующие


        switch (application) {
            case 0: //{
                if(activity.mobyLock.equals(activity.currentMobyLock)) {
                    UiHelper.showToast(context, "This is new version");
                }
                else {
                    activity.startInstallation(result);
                }
                break;
            case 1:
                if(activity.taxiIndigo.equals(activity.currentTaxiIndigo)) {
                    UiHelper.showToast(context, "This is new version");
                }
                else {
                    activity.startInstallation(result);
                }
                break;
            case 2:
                if(activity.taxiIndigo.equals(activity.currentTaxiIndigo)) {
                    UiHelper.showToast(context, "This is new version");
                }
                else {
                    activity.startInstallation(result);
                }
                break;
            default:
                break;

        }

    }
    @Override
    protected void onProgressUpdate(Integer...values) {
        super.onProgressUpdate(values);
        if(this.progressBar != null)
        {
            progressBar.setProgress(values[0]);
        }
    }

    @Override
    public void onCancelled() {
        super.onCancelled();
        UiHelper.showToast(context, "File has been cancelled");
    }

    @Override
    protected File doInBackground(String... params) {
        HttpClient httpClient = HttpClientDownload.getHttpClient();
        File file = new File("");
        try {
            HttpGet request = new HttpGet(params[0]);
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(httpParams, 60000);
            request.setParams(httpParams);

            publishProgress(25);

            HttpResponse response = httpClient.execute(request);

            publishProgress(50);

            byte[] stream = EntityUtils.toByteArray(response.getEntity());

            publishProgress(75);
            file = streamToFile(stream);

            publishProgress(100);
            Log.d("FILECOMPLETED", "COMPLETE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
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


