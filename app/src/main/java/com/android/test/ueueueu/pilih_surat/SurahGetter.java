package com.android.test.ueueueu.pilih_surat;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.test.ueueueu.R;

import java.util.ArrayList;

public class SurahGetter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list_surah;

    public SurahGetter(Context context, ArrayList<String> list_surah){
        this.context = context;
        this.list_surah = list_surah;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_baris_surat,null);

        CheckBox surat = view.findViewById(R.id.nama_surat);

        surat.setText(list_surah.get(position));

        surat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {
                String surah = list_surah.get(position);
                // Do something
            }
        });
        return null;
    }
}
