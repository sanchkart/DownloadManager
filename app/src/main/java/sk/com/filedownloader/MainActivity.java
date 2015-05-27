package sk.com.filedownloader;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


import sk.com.filedownloader.Helper.Constants;
import sk.com.filedownloader.Recievers.ChangingFileReciever;
import sk.com.filedownloader.Tasks.DownloadFileAsync;
import sk.com.filedownloader.Recievers.DownloadReciever;
import sk.com.filedownloader.Tasks.GetVersionAsync;
import sk.com.filedownloader.Services.DownloadBackgroundService;
import sk.com.filedownloader.Services.DownloadService;


public class MainActivity extends ActionBarActivity {

    public Button downloadButton;
    public Button cancelButton;
    public Button secondAppButton;
    public Button secondCancelButton;
    public Button ministroButton;
    public Button ministroCancelButton;
    private Button startSeviceButton;
    private Button stopServiceButton;
    private TextView editTextMoblock;
    private TextView editTextIndigoTaxi;
    private TextView editTextMinstro;
    private TextView editTextNewMoblock;
    private TextView editTextNewIndigoTaxi;
    private TextView editTextNewMinstro;
    private ProgressBar progressBar;
    private DownloadFileAsync task;
    private DownloadFileAsync taskSecond;
    private DownloadFileAsync taskMinistro;
    public String currentTaxiIndigo;
    public String currentMobyLock;
    public String currentMinistro;
    public String taxiIndigo;
    public String mobyLock;
    public String ministro;
    private Intent serviceIntent;

    private final int START = 0;

    private DownloadReciever receiver = new DownloadReciever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.MINISTRO,"10.33");
        ed.putString(Constants.TAXI_INDIGO,"");
        ed.commit();

        taxiIndigo = "";
        mobyLock = "";
        ministro = "";
        getVersionsFromServer();

        editTextNewMoblock = (TextView) findViewById(R.id.textViewNewMobi);
        editTextNewIndigoTaxi = (TextView)findViewById(R.id.textViewNewIndigo);
        editTextNewMinstro = (TextView) findViewById(R.id.textViewNewMinistro);

        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        if(settings != null)
        {
            currentTaxiIndigo = settings.getString(Constants.TAXI_INDIGO,"");
            currentMobyLock = settings.getString(Constants.MOBI_LOCK,"");
            currentMinistro = settings.getString(Constants.MINISTRO,"");
        }
        else
        {
            currentTaxiIndigo = "";
            currentMobyLock = "";
            currentMinistro = "";
        }

        editTextNewMoblock.setText("Телефон: "+ currentMobyLock);
        editTextNewIndigoTaxi.setText("Телефон: "+currentTaxiIndigo);
        editTextNewMinstro.setText("Телефон: "+currentMinistro);

        progressBar = (ProgressBar)findViewById(R.id.taskProgress);
        progressBar.setProgress(START);

        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadButton.setEnabled(true);
                cancelButton.setEnabled(false);
                task.cancel(true);
                progressBar.setProgress(START);
            }

        });
        downloadButton = (Button)findViewById(R.id.buttonDownload);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton.setEnabled(true);
                downloadButton.setEnabled(false);
                if (task != null) {
                    AsyncTask.Status diStatus = task.getStatus();
                    if (diStatus != AsyncTask.Status.FINISHED) {
                        return;
                    }
                }
                task = new DownloadFileAsync(getApplicationContext(), MainActivity.this,0);
                task.setProgressBar(progressBar);
                task.execute("http://indigosystem.ru/taxi/mobilock.apk");
            }
        });
        secondAppButton = (Button)findViewById(R.id.secondAppButton);
        secondAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondCancelButton.setEnabled(true);
                secondAppButton.setEnabled(false);
                if (taskSecond != null) {
                    AsyncTask.Status diStatus = task.getStatus();
                    if (diStatus != AsyncTask.Status.FINISHED) {
                        return;
                    }
                }
                taskSecond = new DownloadFileAsync(getApplicationContext(), MainActivity.this, 1);
                progressBar.setProgress(START);
                taskSecond.setProgressBar(progressBar);
                taskSecond.execute("http://indigosystem.ru/taxi/indigotaxi.apk");

            }
        });
        secondCancelButton = (Button) findViewById(R.id.cancelSecondButton);
        secondCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondAppButton.setEnabled(true);
                secondCancelButton.setEnabled(false);
                taskSecond.cancel(true);
            }
        });
        ministroButton = (Button) findViewById(R.id.ministroButton);
        ministroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ministroCancelButton.setEnabled(true);
                ministroButton.setEnabled(false);
                if (taskMinistro != null) {
                    AsyncTask.Status diStatus = task.getStatus();
                    if (diStatus != AsyncTask.Status.FINISHED) {
                        return;
                    }
                }
                taskMinistro = new DownloadFileAsync(getApplicationContext(), MainActivity.this,2);
                progressBar.setProgress(START);
                taskMinistro.setProgressBar(progressBar);
                taskMinistro.execute("http://indigosystem.ru/taxi/ministro.apk");
            }
        });
        ministroCancelButton = (Button)findViewById(R.id.cancelThirdButton);
        ministroCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ministroCancelButton.setEnabled(false);
                ministroButton.setEnabled(true);
                taskMinistro.cancel(true);
            }
        });
        startSeviceButton = (Button)findViewById(R.id.startServiceButton);
        startSeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSeviceButton.setEnabled(false);
                stopServiceButton.setEnabled(true);
                serviceIntent = new Intent(getApplicationContext(), DownloadBackgroundService.class);
                startService(serviceIntent);
            }
        });

        stopServiceButton = (Button)findViewById(R.id.cancelServiceButton);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServiceButton.setEnabled(false);
                stopService(serviceIntent);
                startSeviceButton.setEnabled(true);
            }
        });
        editTextIndigoTaxi = (TextView)findViewById(R.id.textViewIndigo);
        editTextMoblock = (TextView) findViewById(R.id.textViewMobi);
        editTextMinstro = (TextView) findViewById(R.id.textViewMinistro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setEnabled();
            }
        }, 5000);

    }


    private void getVersionsFromServer() {
        GetVersionAsync getVersionAsync = new GetVersionAsync(getApplicationContext(), MainActivity.this,1);
        getVersionAsync.execute("http://indigosystem.ru/taxi/indigotaxi-version.txt");

        GetVersionAsync getVersionAsyncFirst = new GetVersionAsync(getApplicationContext(), MainActivity.this,0);
        getVersionAsyncFirst.execute("http://indigosystem.ru/taxi/mobilock-version.txt");

        GetVersionAsync getVersionAsyncThird = new GetVersionAsync(getApplicationContext(), MainActivity.this,2);
        getVersionAsyncThird.execute("http://indigosystem.ru/taxi/ministro-version.txt");
    }

    public void setMobyLock(String mobyLock)
    {
        this.mobyLock = mobyLock;

    }
    public void setEnabled()
    {
        downloadButton.setEnabled(true);
        secondAppButton.setEnabled(true);
        ministroButton.setEnabled(true);
        startSeviceButton.setEnabled(true);
        editTextMoblock.setText("Cервер: " + mobyLock);
        editTextIndigoTaxi.setText("Cервер: "+taxiIndigo);
        editTextMinstro.setText("Cервер: "+ministro);

    }
    public void setTaxiIndigo(String taxiIndigo)
    {
        this.taxiIndigo = taxiIndigo;
    }

    public void savePreferences(String preference, int application)
    {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        if(application == 0)
        {
            ed.putString(Constants.MOBI_LOCK, preference);
        }
        else if(application == 1)
        {
            ed.putString(Constants.TAXI_INDIGO, preference);
        }
        else
        {
            ed.putString(Constants.MINISTRO,preference);
        }

        ed.commit();
    }

    public void startInstallation(File result) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Log.d("FILE", result.getAbsolutePath());
        intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
       registerReceiver(receiver, new IntentFilter(DownloadService.NOTIFICATION));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMinistro(String version) {
        this.ministro = version;

    }
}
