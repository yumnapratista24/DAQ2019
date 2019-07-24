package com.android.test.ueueueu.pilih_surat;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.test.ueueueu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PilihSurat extends Activity {

    private SurahGetter surahGetter;
    private ArrayList<String> list_surat;
    private ListView lv;
    private

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = findViewById(R.id.list);

        surahGetter = new SurahGetter(this, list_surat);

        lv.setAdapter(surahGetter);
    }

    private ArrayList<String> populateData(){
        return null;
    }
}