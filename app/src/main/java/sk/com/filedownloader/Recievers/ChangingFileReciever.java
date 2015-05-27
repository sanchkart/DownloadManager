package sk.com.filedownloader.Recievers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sk.com.filedownloader.Classes.CurrentInformation;
import sk.com.filedownloader.Classes.InstallationApp;
import sk.com.filedownloader.Classes.MyList;
import sk.com.filedownloader.Classes.ServerInformation;
import sk.com.filedownloader.Helper.Constants;
import sk.com.filedownloader.Helper.GlobalHelper;
import sk.com.filedownloader.MainActivity;
import sk.com.filedownloader.Tasks.StartIntentAsync;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
public class ChangingFileReciever extends BroadcastReceiver {

    private InstallationApp indigoTaxi = new InstallationApp("http://indigosystem.ru/taxi/indigotaxi.apk","0.1.29",Constants.PACKAGE_TAXI_INDIGO);
    private  InstallationApp systemLocker = new InstallationApp("http://indigosystem.ru/taxi/mobiblock.apk","2.2",Constants.PACKAGE_MOBI_LOCK);
    private  InstallationApp ministro = new InstallationApp("http://indigosystem.ru/taxi/ministro.apk","9.6.3",Constants.PACKAGE_MINISTRO);
    //public static ArrayList<InstallationApp> appArrayList = new ArrayList<>();
    //TODO ArrayList or HashMap?
    public static HashMap<String, InstallationApp> applications = new HashMap();

    public ChangingFileReciever()
    {
        applications.put(Constants.MINISTRO,ministro);
        applications.put(Constants.MOBI_LOCK,systemLocker);
        applications.put(Constants.TAXI_INDIGO, indigoTaxi);
        /*appArrayList.add(indigoTaxi);
        appArrayList.add(systemLocker);
        appArrayList.add(ministro);*/
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Uri uri = intent.getData();
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_INSTALL_PACKAGE) || action.equals(Intent.ACTION_PACKAGE_ADDED))
        {
            for(Map.Entry<String,InstallationApp> entry : applications.entrySet())
            {
                InstallationApp tempApp = entry.getValue();

                if(uri.equals(tempApp.getPackageName()))
                {
                    CurrentInformation fromSharedPreferenced = GlobalHelper.getPreferences(context);
                    ServerInformation fromServer = GlobalHelper.getServerInformation();
                    switch (tempApp.getPackageName().toString())
                    {
                        case Constants.PACKAGE_MINISTRO:
                            if(!fromSharedPreferenced.getCurrentMinistro().equals(fromServer.getMinistro()))
                            {
                                GlobalHelper.savePreferences(fromServer.getMinistro(), Constants.MINISTRO, context);
                                tempApp.setVersion(fromServer.getMinistro());
                                applications.put(Constants.MINISTRO, tempApp);
                                if(!fromSharedPreferenced.getCurrentTaxiIndigo().equals(fromServer.getTaxiIndigo()))
                                {
                                    startInstallation(Constants.TAXI_INDIGO, context);
                                }
                            }
                            break;

                        case Constants.PACKAGE_MOBI_LOCK:
                            if(!fromSharedPreferenced.getCurrentMobyLock().equals(fromServer.getMobyLock()))
                            {
                                GlobalHelper.savePreferences(fromServer.getMobyLock(),Constants.MOBI_LOCK,context);
                                tempApp.setVersion(fromServer.getMobyLock());
                                applications.put(Constants.MOBI_LOCK, tempApp);
                            }
                            break;

                        case Constants.PACKAGE_TAXI_INDIGO:
                            if(!fromSharedPreferenced.getCurrentTaxiIndigo().equals(fromServer.getTaxiIndigo()))
                            {
                                GlobalHelper.savePreferences(fromServer.getTaxiIndigo(), Constants.TAXI_INDIGO, context);
                                tempApp.setVersion(fromServer.getTaxiIndigo());
                                applications.put(Constants.TAXI_INDIGO, tempApp);
                                if(!fromSharedPreferenced.getCurrentMobyLock().equals(fromServer.getMobyLock()))
                                {
                                    startInstallation(Constants.MOBI_LOCK, context);
                                }
                            }
                            break;

                        default:
                            break;
                    }
                    Log.i("PACKAGE", "Package_installed");
                }
            }
        }
        else if(action.equals(Intent.ACTION_PACKAGE_REMOVED))
        {
            Log.i("PACKAGE","Package_removed");
        }
    }
    private void startInstallation(String application, Context context)
    {
        MyList listInstance = MyList.getInstance();
        File tempFile = listInstance.list.get(application);
        Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW);
        newIntent.setDataAndType(Uri.parse("file://" + tempFile.getAbsolutePath()), "application/vnd.android.package-archive");
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

}
