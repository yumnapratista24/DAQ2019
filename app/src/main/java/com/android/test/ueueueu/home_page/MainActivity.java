package com.android.test.ueueueu.home_page;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
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

import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.home_page.MainActFragment.ListOfAlarm;
import com.android.test.ueueueu.home_page.MainActFragment.AppSettings;
import com.android.test.ueueueu.R;
import com.android.test.ueueueu.model.DataModel;
import com.android.test.ueueueu.pilih_surat.PilihSurat;

import java.util.ArrayList;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS = "savedAlarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db_helper = new DatabaseHelper(this);
//        db_helper.selectAllSchedule();
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("onboarding",MODE_PRIVATE);

        // Dev mode biar selalu muncul yang pertama kali
        sp.edit().clear();

        Boolean first_timer = sp.getBoolean("first_timer",false);

        // On boarding app
        if(!first_timer) {
            showOnBoarding();
            sp.edit().putBoolean("first_timer",true);
        }
        // Not first timer
        setupApp();
    }


    private void showOnBoarding(){
        showAlertBeforeBoarding(this);
    }

    private void showAlertBeforeBoarding(final Activity activity){
        final PilihSurat ps = new PilihSurat();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Silahkan isi surat hafalanmu terlebih dahulu!");
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ps.showFirstTimer(activity);
                    }
                });
        alertDialogBuilder.show();
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


