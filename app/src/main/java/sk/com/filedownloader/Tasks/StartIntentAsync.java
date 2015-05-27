package sk.com.filedownloader.Tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import sk.com.filedownloader.Classes.MyList;
import sk.com.filedownloader.Services.DownloadBackgroundService;

/**
 * Created by sanchirkartiev on 02/04/15.
 */
public class StartIntentAsync extends AsyncTask<File,Integer,File> {
    DownloadBackgroundService service;
    String  application;
    public StartIntentAsync(DownloadBackgroundService service, String application)
    {
        this.service = service;
        this.application = application;
    }

    @Override
    protected File doInBackground(File... params) {
        MyList list = MyList.getInstance();
        if(list.list.size() == 0)
        {
            return null;
        }
        else if(list.list.size() > 1)
        {
            //TODO Проход по двум листам
            File newFile = list.list.get(application);
            if(newFile != null)
            {
                Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW);
                newIntent.setDataAndType(Uri.parse("file://" + newFile.getAbsolutePath()), "application/vnd.android.package-archive");
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                service.startActivity(newIntent);
                return newFile;
            }
        }
        else
        {
            File newFile = list.list.get(0);
            if(newFile != null)
            {
                Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW);
                newIntent.setDataAndType(Uri.parse("file://" + newFile.getAbsolutePath()), "application/vnd.android.package-archive");
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                service.startActivity(newIntent);
                return newFile;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(File file) {

    }
}
