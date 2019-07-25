package com.android.test.ueueueu.alarm_main_app;

import android.content.Context;
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
import com.android.test.ueueueu.model.DayModel;
import com.android.test.ueueueu.model.RepeatedDay;
import com.android.test.ueueueu.model.Surah;

import java.util.List;

public class DayChooser extends BaseAdapter {

    private Context context;
    private List<DayModel> list_day;

    public DayChooser(Context context, List<DayModel> list_day){
        this.context = context;
        this.list_day = list_day;
    }

    @Override
    public long getItemId(int i) {
        return list_day.get(i).idDay;
    }

    @Override
    public Object getItem(int i) {
        return list_day.get(i);
    }

    @Override
    public int getCount() {
        return list_day.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_baris_surat,null);

        CheckBox surat = view.findViewById(R.id.nama_surat);
        if(list_day.get(position).isChoosen) surat.setChecked(true);

        surat.setText(list_day.get(position).dayName);

        surat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {

                if(bool == false) {
                    list_day.get(position).isChoosen = false;
                }
                else {
                    list_day.get(position).isChoosen = true;
                }

            }
        });
        return view;
    }
}
