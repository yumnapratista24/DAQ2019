package com.android.test.ueueueu.alarm_main_app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.home_page.MainActFragment.*;
import com.android.test.ueueueu.model.DataModel;
import com.android.test.ueueueu.model.DayModel;
import com.android.test.ueueueu.model.RepeatedDay;
import com.android.test.ueueueu.model.Schedule;
import com.travijuu.numberpicker.library.NumberPicker;

import java.math.BigInteger;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.util.Log.d;
import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

/**
 * Created by 642174 on 05/06/2019.
 */

public class AlarmEditor extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private TimePicker timePicker;
    private NumberPicker numberPicker;
    private static AlarmEditor inst;
    List<DayModel> list_day;
    DatabaseHelper dbHelper;

    public static AlarmEditor instance(){
        return inst;
    }

    @Override
    public void onStart(){
        super.onStart();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set_new_alarm);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        Button toggleButton = (Button) findViewById(R.id.toggle_button);

        // set number picker max and min value of problem
        numberPicker = (NumberPicker) findViewById(R.id.problem_number);
        numberPicker.setMax(10);
        numberPicker.setMin(1);

        // set button to show dialog to choose repeat day for alarm
        View daily_alarm = (View) findViewById(R.id.daily_alarm);

        // create a day list
        DayModel minggu = new DayModel(Calendar.SUNDAY, "Minggu");
        DayModel senin = new DayModel(Calendar.MONDAY, "Senin");
        DayModel selasa = new DayModel(Calendar.TUESDAY, "Selasa");
        DayModel rabu = new DayModel(Calendar.WEDNESDAY, "Rabu");
        DayModel kamis = new DayModel(Calendar.THURSDAY, "Kamis");
        DayModel jumat = new DayModel(Calendar.FRIDAY, "Jumat");
        DayModel sabtu = new DayModel(Calendar.SATURDAY, "Sabtu");

        list_day = Arrays.asList(minggu,senin,selasa,rabu,kamis,jumat,sabtu);


        alarmManager = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        getSupportFragmentManager().beginTransaction().add(R.id.alarm_preferences, new Alarm_editor_settings()).commit();
    }

    public void toggleOnCLick(View view){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int jam = calendar.get(Calendar.HOUR);
        int menit = calendar.get(Calendar.MINUTE);

        String jam_string = jam + "";
        String menit_string = menit + "";

        int kondisi = calendar.get(Calendar.AM_PM);
        if(kondisi == Calendar.PM){
            jam+=12;
        }


        Log.i("Sekarang nilai segini: ",numberPicker.getValue() + "");

        Date date = calendar.getTime();
        Log.i("Waktu",date + "");

        if(jam < 10) jam_string = "0"+jam;

        if(menit < 10) menit_string = "0"+menit;

        String waktu = jam_string + ":" + menit_string;
        Log.i("WAKTU",waktu);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("waktu",waktu);
        myIntent.setAction(Intent.ACTION_MAIN);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


        String hour = String.valueOf(timePicker.getCurrentHour());
        String minute = String.valueOf(timePicker.getCurrentMinute());
        String ans = hour + ':' + minute;

        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Time waktuALarm = new Time(jam);
        try {
            waktuALarm = new Time(formatter.parse(waktu).getTime());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("Waktu alarm adalah", waktuALarm.toString());

        // get all selected days and create repeated day
        List<DayModel> listSelectedDays = getSelectedDays();
        List<RepeatedDay> listAlarmRepeat = new ArrayList<>();
        for (int i = 0; i < listSelectedDays.size(); i++){
            int idAlarm = Integer.parseInt(String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16)).substring(0,9));
            RepeatedDay repeatedDay = new RepeatedDay(0, idAlarm, listSelectedDays.get(i).idDay);
            listAlarmRepeat.add(repeatedDay);
            pendingIntent = PendingIntent.getBroadcast(this, idAlarm, myIntent, 0);
            calendar.set(Calendar.DAY_OF_WEEK, repeatedDay.day);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }

        // create database schedule
        Schedule schedule = new Schedule(waktuALarm, true, true, listAlarmRepeat);
        dbHelper = new DatabaseHelper(this);
        dbHelper.createSchedule(schedule);

        List<Schedule> list_alarm = dbHelper.selectAllSchedule();

        // buat alarm

        ListOfAlarm.adapter.add(new DataModel(ans));

        ListOfAlarm.adapter.notifyDataSetChanged();

        Log.i("Hari yang dipilih: ", list_day.toString());


        finish();
    }

    public void showDialog(View view){
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.activity_pilih_surat, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update

        ListView listView = dialogView.findViewById(R.id.list);

        DayChooser dayChooser = new DayChooser(this, list_day);

        listView.setAdapter(dayChooser);

        alertDialog.setView(dialogView);
        alertDialog.setTitle("Pilih Hari");

        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Hari yang dipilih : ", list_day.toString());
            }
        });

        alertDialog.show();
    }

    public List<DayModel> getSelectedDays() {
        List<DayModel> listSelectedDays = new ArrayList<DayModel>();
        for (int i =0; i < list_day.size(); i++){
            if(list_day.get(i).isChoosen == true) {
                listSelectedDays.add(list_day.get(i));
            }

        }
        return listSelectedDays;
    }


    public static class Alarm_editor_settings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.alarm_config, rootKey);
            setup();
        }

        public void setup(){
            final Preference alarmMethod = findPreference("alarm_method");
            alarmMethod.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    alarmMethod(preference);
                    return true;
                }
            });
        }

        public void alarmMethod(Preference view){
            Intent intent = new Intent(view.getContext(), AlarmMethod.class);
            startActivity(intent);
        }
    }
}
