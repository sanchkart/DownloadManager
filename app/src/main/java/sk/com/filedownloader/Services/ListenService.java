package sk.com.filedownloader.Services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import sk.com.filedownloader.MainActivity;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
public class ListenService extends IntentService {

    private long downloadId;
    private DownloadManager dMgr;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public ListenService(String name) {
        super(name);
    }

    @Override
    public void onCreate()
    {
        dMgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Log.d("OPOOOOOOOOO","ffsada");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        Intent intents = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intents);

        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            long doneDownloadId =
                    extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);

            if(downloadId == doneDownloadId)
                Log.v("fsada", "Our download has completed.");
        }
    };
    @Override
    protected void onHandleIntent(Intent intent)
    {
        DownloadManager.Request dmReq = new DownloadManager.Request(
                Uri.parse("http://indigosystem.ru/taxi/indigotaxi.apk"));
        dmReq.setTitle("IndigoTaxi");
        dmReq.setDescription("Indigo Systems");
        dmReq.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        IntentFilter filter = new
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mReceiver, filter);
        downloadId = dMgr.enqueue(dmReq);
    }
}
