package com.android.test.ueueueu.pilih_surat;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.model.Surah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PilihSurat extends AppCompatActivity {

    private SurahGetter surahGetter;
    private ArrayList<String> list_surat;
    private ListView lv;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_surat);

        dbHelper = new DatabaseHelper(this);

        List<Surah> surahs = dbHelper.getAllSurah();
        Log.i("CEK CEK",surahs.toString());

        lv = (ListView) findViewById(R.id.list);

        surahGetter = new SurahGetter(this, surahs, dbHelper);

        lv.setAdapter(surahGetter);
    }

}