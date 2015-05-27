package sk.com.filedownloader.Tasks;

import android.os.AsyncTask;

import java.io.File;

import sk.com.filedownloader.Classes.ServerInformation;
import sk.com.filedownloader.Helper.GlobalHelper;
import sk.com.filedownloader.Services.DownloadBackgroundService;

/**
 * Created by sanchirkartiev on 03/04/15.
 */
public class SetServerInfoAsync extends AsyncTask<String, Integer,String> {

    DownloadBackgroundService service;
    public SetServerInfoAsync(DownloadBackgroundService service)
    {
        this.service = service;
    }
    @Override
    protected String doInBackground(String... params) {
        ServerInformation serverInformation = new ServerInformation(service.getMobyLock(),service.getMinistro(),service.getTaxiIndigo());
        GlobalHelper.setServerInformation(serverInformation);
        return null;
    }
}
