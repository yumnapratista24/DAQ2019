package com.android.test.ueueueu.pilih_surat;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.model.Surah;

import java.util.ArrayList;
import java.util.List;

public class SurahGetter extends BaseAdapter {

    private Context context;
    private List<Surah> list_surah;
    private DatabaseHelper dbHelper;

    public SurahGetter(Context context, List<Surah> list_surah, DatabaseHelper dbHelper){
        this.context = context;
        this.list_surah = list_surah;
        this.dbHelper = dbHelper;
    }

    @Override
    public long getItemId(int i) {
        return list_surah.get(i).no_surah;
    }

    @Override
    public Object getItem(int i) {
        return list_surah.get(i);
    }

    @Override
    public int getCount() {
        return list_surah.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_baris_surat,null);

        CheckBox surat = view.findViewById(R.id.nama_surat);
        if(list_surah.get(position).is_choosen) surat.setChecked(true);

        surat.setText(list_surah.get(position).surah_name);

        surat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                Surah surah = list_surah.get(position);
                // Do something
                surah.is_choosen = true;
                dbHelper.updateSurah(surah);
                if(bool == false) {
                    Log.i("CEK FALSE", surah.surah_name + " " + surah.is_choosen);
                    Toast.makeText(context, surah.surah_name + " is not choosen", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("CEK TRUE", surah.surah_name + " " + surah.is_choosen);
                    Toast.makeText(context, surah.surah_name + " is choosen", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}
