package com.android.test.ueueueu.home_page;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.alarm_main_app.AlarmEditor;
import com.android.test.ueueueu.model.DataModel;
import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

import java.util.ArrayList;

/**
 * Created by 642174 on 10/06/2019.
 */

public class MainActFragment {
    public static class ListOfAlarm extends Fragment {

        public static CustomAdapter adapter;

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

            ArrayList<DataModel> dataModels = new ArrayList<>();

            SharedPreferences alarm_db = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            String db_checker = alarm_db.getString("message", "empty");
            if (!db_checker.equals("empty")) {

                String alarm_items[] = db_checker.split(" ");


                for (int ij = 0; ij < alarm_items.length; ij++){
                    if (!alarm_items[ij].equals(""))
                        dataModels.add(new DataModel(alarm_items[ij]));

                }

                adapter = new CustomAdapter(dataModels, this.getActivity().getApplicationContext(), getLayoutInflater(), this.getActivity());

                listView.setAdapter(adapter);

            }

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
        }
    }
}
