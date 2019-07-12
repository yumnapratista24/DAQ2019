package com.android.test.ueueueu.alarm_main_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.test.ueueueu.R;

import java.util.Calendar;

/**
 * Created by 642174 on 05/06/2019.
 */

public class AlarmEditor extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private TimePicker timePicker;
    private static AlarmEditor inst;

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

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.setAction(Intent.ACTION_MAIN);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
