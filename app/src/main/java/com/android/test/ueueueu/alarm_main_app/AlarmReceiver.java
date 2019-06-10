package com.android.test.ueueueu.alarm_main_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by 642174 on 10/06/2019.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "bambang", Toast.LENGTH_SHORT).show();
    }
}
