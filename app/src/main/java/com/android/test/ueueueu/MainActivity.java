package com.android.test.ueueueu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton toSettings = (ImageButton) findViewById(R.id.toSettings);
        final ImageButton toList = (ImageButton) findViewById(R.id.toList);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, new ListOfAlarm());
        fragmentTransaction.commit();

        toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ListOfAlarm())
                        .commit();
            }
        });

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new Settings())
                        .commit();
            }
        });
    }

    public static class ListOfAlarm extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View list_of_alarm = inflater.inflate(R.layout.list_of_alarm, container, false);
            FloatingActionButton newAlarm = list_of_alarm.findViewById(R.id.alarm_add);
            newAlarm.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    addAlarm(view);
                }
            });
            return list_of_alarm;
        }

        public void addAlarm(View view){
            Intent intent = new Intent(view.getContext(), AlarmEditor.class);
            startActivity(intent);
        }
    }

    public static class Settings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }

}

