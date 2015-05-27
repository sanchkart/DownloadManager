package sk.com.filedownloader.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import sk.com.filedownloader.Helper.Constants;
import sk.com.filedownloader.Tasks.DownloadFileServiceAsync;
import sk.com.filedownloader.Tasks.GetVersionServiceAsync;
import sk.com.filedownloader.Tasks.SetServerInfoAsync;
import sk.com.filedownloader.Tasks.StartIntentAsync;


public class DownloadBackgroundService extends Service {

    private String currentTaxiIndigo;
    private String currentMobyLock;
    private String currentMinistro;
    private String taxiIndigo;
    private String mobyLock;
    private String ministro;

    public DownloadBackgroundService() {
    }
    public class LocalBinder extends Binder {
        DownloadBackgroundService getService() {
            return DownloadBackgroundService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;

    }

    private final IBinder mBinder = new LocalBinder();

    public void setMobyLock(String mobyLock) {
        this.mobyLock = mobyLock;

    }
    public void setMinistro(String version) {
        this.ministro = version;
    }

    public String getCurrentTaxiIndigo() {
        return currentTaxiIndigo;
    }

    public void setCurrentTaxiIndigo(String currentTaxiIndigo) {
        this.currentTaxiIndigo = currentTaxiIndigo;
    }

    public String getCurrentMobyLock() {
        return currentMobyLock;
    }

    public void setCurrentMobyLock(String currentMobyLock) {
        this.currentMobyLock = currentMobyLock;
    }

    public String getCurrentMinistro() {
        return currentMinistro;
    }

    public void setCurrentMinistro(String currentMinistro) {
        this.currentMinistro = currentMinistro;
    }

    public String getTaxiIndigo() {
        return taxiIndigo;
    }

    public String getMobyLock() {
        return mobyLock;
    }

    public String getMinistro() {
        return ministro;
    }

    public void setTaxiIndigo(String taxiIndigo) {
        this.taxiIndigo = taxiIndigo;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if(settings != null)
        {
            currentTaxiIndigo = settings.getString(Constants.TAXI_INDIGO,"");
            currentMobyLock = settings.getString(Constants.MOBI_LOCK,"");
            currentMinistro = settings.getString(Constants.MINISTRO,"");
        }

        GetVersionServiceAsync getVersionAsync = new GetVersionServiceAsync(this, Constants.TAXI_INDIGO);
        GetVersionServiceAsync getVersionAsyncFirst = new GetVersionServiceAsync(this,Constants.MOBI_LOCK);
        GetVersionServiceAsync getVersionAsyncThird = new GetVersionServiceAsync(this,Constants.MINISTRO);

        SetServerInfoAsync setServerInfoAsync = new SetServerInfoAsync(this);

        getVersionAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,"http://indigosystem.ru/taxi/indigotaxi-version.txt");
        getVersionAsyncFirst.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "http://indigosystem.ru/taxi/mobilock-version.txt");
        getVersionAsyncThird.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "http://indigosystem.ru/taxi/ministro-version.txt");
        setServerInfoAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        DownloadFileServiceAsync downloadFileServiceAsyncFirst = new DownloadFileServiceAsync(getApplicationContext(), this);
        DownloadFileServiceAsync downloadFileServiceAsyncSecond = new DownloadFileServiceAsync(getApplicationContext(),this);
        DownloadFileServiceAsync downloadFileServiceAsyncThird = new DownloadFileServiceAsync(getApplicationContext(),this);

        downloadFileServiceAsyncFirst.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "http://indigosystem.ru/taxi/ministro.apk", Constants.MINISTRO);
        downloadFileServiceAsyncSecond.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "http://indigosystem.ru/taxi/indigotaxi.apk", Constants.TAXI_INDIGO);
        downloadFileServiceAsyncThird.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "http://indigosystem.ru/taxi/mobilock.apk", Constants.MOBI_LOCK);


        StartIntentAsync intentAsync = new StartIntentAsync(this,Constants.MINISTRO);

        intentAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        return START_STICKY;
    }



}
