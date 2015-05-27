package sk.com.filedownloader.Recievers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import sk.com.filedownloader.MainActivity;
import sk.com.filedownloader.Services.DownloadBackgroundService;
import sk.com.filedownloader.Services.DownloadService;

public class DownloadReciever extends BroadcastReceiver {
    public DownloadReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, DownloadBackgroundService.class));
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(0)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

}
