package com.android.test.ueueueu.home_page;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.alarm_main_app.AlarmEditor;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.model.DataModel;
import com.android.test.ueueueu.model.Schedule;
import com.android.test.ueueueu.pilih_surat.PilihSurat;

import org.w3c.dom.Text;

import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 642174 on 10/06/2019.
 */

public class MainActFragment {
    public static class ListOfAlarm extends Fragment {

        public static CustomAdapter adapter;
        DatabaseHelper dbHelper;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View list_of_alarm = inflater.inflate(R.layout.home_fragment_alarm, container, false);
            FloatingActionButton newAlarm = list_of_alarm.findViewById(R.id.alarm_add);
            newAlarm.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    addAlarm(view);
                }
            });

            ListView listView = (ListView) list_of_alarm.findViewById(R.id.list_of_alarm);

            dbHelper = new DatabaseHelper(this.getActivity());

            List<Schedule> list_alarm = dbHelper.selectAllSchedule();

            adapter = new CustomAdapter(list_alarm, this.getContext(), getLayoutInflater(), this.getActivity());

            listView.setAdapter(adapter);

            return list_of_alarm;
        }

        public void addAlarm(View view){
            Intent intent = new Intent(view.getContext(), AlarmEditor.class);
            startActivity(intent);
        }
    }

    public static class AppSettings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
            setPreferencesFromResource(R.xml.preferences, rootKey);

            final Preference pilih_surat = findPreference("pilih_surat");
            pilih_surat.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), PilihSurat.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}
