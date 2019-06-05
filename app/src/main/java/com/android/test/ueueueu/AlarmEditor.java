package com.android.test.ueueueu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by 642174 on 05/06/2019.
 */

public class AlarmEditor extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_edit);
        getSupportFragmentManager().beginTransaction().add(R.id.alarm_preferences, new Alarm_editor_settings()).commit();

    }

    public static class Alarm_editor_settings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.alarm_config, rootKey);
        }
    }
}
