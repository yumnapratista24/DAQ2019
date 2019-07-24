package com.android.test.ueueueu.home_page;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.home_page.MainActFragment.ListOfAlarm;
import com.android.test.ueueueu.home_page.MainActFragment.AppSettings;
import com.android.test.ueueueu.R;

import java.util.List;

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


