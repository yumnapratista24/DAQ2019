package anas.mtq.daq.ueueueu.alarm_main_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 642174 on 10/06/2019.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String waktu = intent.getStringExtra("waktu");
        int problem = intent.getIntExtra("problem",3);
        Intent i = new Intent("android.intent.action.MAIN");
        i.putExtra("problem",problem);
        i.putExtra("waktu",waktu);
        i.setClass(context, AnswerSheet.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
