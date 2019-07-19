package com.android.test.ueueueu.home_page;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import com.android.test.ueueueu.home_page.MainActFragment.ListOfAlarm;
import com.android.test.ueueueu.home_page.MainActFragment.AppSettings;
import com.android.test.ueueueu.R;
import com.android.test.ueueueu.model.DataModel;

import java.util.ArrayList;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS = "savedAlarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupApp();

    }

    public void setupApp(){
        //First time app running
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
                        .replace(R.id.fragment_container, new AppSettings())
                        .commit();
            }
        });


    }
}


