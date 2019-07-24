package com.android.test.ueueueu.alarm_main_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.home_page.MainActFragment.*;
import com.android.test.ueueueu.model.DataModel;

import java.util.Calendar;
import java.util.Date;

import static android.util.Log.d;
import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

/**
 * Created by 642174 on 05/06/2019.
 */

public class AlarmEditor extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private TimePicker timePicker;
    private static AlarmEditor inst;
    public static int id = 0 ;

    public static AlarmEditor instance(){
        return inst;
    }

    @Override
    public void onStart(){
        super.onStart();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set_new_alarm);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        Button toggleButton = (Button) findViewById(R.id.toggle_button);
        alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        getSupportFragmentManager().beginTransaction().add(R.id.alarm_preferences, new Alarm_editor_settings()).commit();
    }

    public void toggleOnCLick(View view){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int jam = calendar.get(Calendar.HOUR);
        int menit = calendar.get(Calendar.MINUTE);

        String jam_string = jam + "";
        String menit_string = menit + "";

        int kondisi = calendar.get(Calendar.AM_PM);
        if(kondisi == Calendar.PM){
            jam+=12;
        }


        Date date = calendar.getTime();
        Log.i("Waktu",date + "");

        if(jam < 10) jam_string = "0"+jam;

        if(menit < 10) menit_string = "0"+menit;

        String waktu = jam_string + ":" + menit_string;
        Log.i("WAKTU",waktu);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("waktu",waktu);
        myIntent.setAction(Intent.ACTION_MAIN);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);



        SharedPreferences saveAlarm = getSharedPreferences(PREFS, 0);
        String stringSaveAlarm = saveAlarm.getString("message", "not found");

        String hour = String.valueOf(timePicker.getCurrentHour());
        String minute = String.valueOf(timePicker.getCurrentMinute());
        String ans = hour + ':' + minute;

        if (stringSaveAlarm.equals("not found")) {
            SharedPreferences.Editor editor = saveAlarm.edit();
            editor.putString("message", ans + " ");
            editor.commit();
        } else {
            SharedPreferences.Editor editor = saveAlarm.edit();
            editor.putString("message", stringSaveAlarm + ans + " ");
            editor.commit();
        }

        saveAlarm = getSharedPreferences(PREFS, 0);
        stringSaveAlarm = saveAlarm.getString("message", "not found");

        pendingIntent = PendingIntent.getBroadcast(this, stringSaveAlarm.split(" ").length, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmEditor.id++;
        Log.i("WAKTU", stringSaveAlarm.split(" ").length + "");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        ListOfAlarm.adapter.add(new DataModel(ans));

        ListOfAlarm.adapter.notifyDataSetChanged();

        SharedPreferences example = getSharedPreferences(PREFS, 0);
        String testprint = example.getString("message", "not found");

        Log:d("Tag :: ", testprint);


        finish();
    }

    public static class Alarm_editor_settings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.alarm_config, rootKey);
            setup();
        }

        public void setup(){
            final Preference alarmMethod = findPreference("alarm_method");
            alarmMethod.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    alarmMethod(preference);
                    return true;
                }
            });
        }

        public void alarmMethod(Preference view){
            Intent intent = new Intent(view.getContext(), AlarmMethod.class);
            startActivity(intent);
        }
    }
}
