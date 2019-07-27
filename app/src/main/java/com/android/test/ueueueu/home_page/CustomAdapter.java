package com.android.test.ueueueu.home_page;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.alarm_main_app.AlarmReceiver;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.model.DataModel;
import com.android.test.ueueueu.model.DayModel;
import com.android.test.ueueueu.model.RepeatedDay;
import com.android.test.ueueueu.model.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;
import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

/**
 * Created by Mehul Garg on 09-10-2017.
 */

public class CustomAdapter extends ArrayAdapter<Schedule> implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private List<Schedule> dataSet;
    Intent my_intent;
    Context mContext;

    AlarmManager alarm_manager;
    PendingIntent pending_intent;
    LayoutInflater mInflator;
    Activity mActivity;
    DatabaseHelper dbHelper;


    // View lookup cache
    private static class ViewHolder {
        TextView time;
        Button delete_row;
        Switch alarm_on_off;
        // text of days properties
        TextView sunday;
        TextView monday;
        TextView tuesday;
        TextView wednesday;
        TextView thursday;
        TextView friday;
        TextView saturday;
    }

    public CustomAdapter(List<Schedule> data, Context context, LayoutInflater mInflator, Activity mActivity) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mInflator = mInflator;
        this.mActivity = mActivity;
        dbHelper = new DatabaseHelper(mActivity.getApplicationContext());
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Schedule dataModel= (Schedule)object;
    }


    private int lastPosition = -1;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(final int position, View convertView, ViewGroup parent) {

        alarm_manager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        // Get the data item for this position
        Schedule alarm = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        final ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_item, parent, false);
        viewHolder.time = (TextView) convertView.findViewById(R.id.textView);
        viewHolder.alarm_on_off = (Switch) convertView.findViewById(R.id.alarm_on_off);
        viewHolder.delete_row = (Button) convertView.findViewById(R.id.delete);

        // viewholder of day properties
        viewHolder.sunday = (TextView) convertView.findViewById(R.id.sunday);
        viewHolder.monday = (TextView) convertView.findViewById(R.id.monday);
        viewHolder.tuesday = (TextView) convertView.findViewById(R.id.tuesday);
        viewHolder.wednesday = (TextView) convertView.findViewById(R.id.wednesday);
        viewHolder.thursday = (TextView) convertView.findViewById(R.id.thursday);
        viewHolder.friday = (TextView) convertView.findViewById(R.id.friday);
        viewHolder.saturday = (TextView) convertView.findViewById(R.id.saturday);

        convertView.setTag(viewHolder);

        for (RepeatedDay repeatedDay : alarm.schedule_repeat_day){
            TextView textView = viewHolder.sunday;
            switch (repeatedDay.day){
                case Calendar.SUNDAY :
                    textView = viewHolder.sunday;
                    break;
                case Calendar.MONDAY :
                    textView = viewHolder.monday;
                    break;
                case Calendar.TUESDAY:
                    textView = viewHolder.tuesday;
                    break;
                case Calendar.WEDNESDAY :
                    textView = viewHolder.wednesday;
                    break;
                case Calendar.THURSDAY :
                    textView = viewHolder.thursday;
                    break;
                case Calendar.FRIDAY :
                    textView = viewHolder.friday;
                    break;
                case Calendar.SATURDAY :
                    textView = viewHolder.saturday;
                    break;

            }
            textView.setTextColor(Color.parseColor("#16A613"));
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }


        lastPosition = position;

        // create an instance of an calendar
        final Calendar calendar = Calendar.getInstance();

        //create an intent to Alarm receiver class
        my_intent = new Intent(getContext(), AlarmReceiver.class);


        DateFormat format = new SimpleDateFormat("HH:mm");

        String alarm_time_text = format.format(alarm.time);

        viewHolder.time.setText(alarm_time_text);

        viewHolder.alarm_on_off.setChecked(getItem(position).is_active);

        viewHolder.alarm_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Schedule update_schedule = getItem(position);
                    Log.i("AncolMasuk1" , update_schedule.id +"");
                    update_schedule.is_active = true;
                    dbHelper.updateSchedule(update_schedule);
                    createAlarmAgain(getItem(position));
                    viewHolder.alarm_on_off.setChecked(getItem(position).is_active);
                } else {
                    Schedule update_schedule = getItem(position);
                    Log.i("AncolMasuk1" , update_schedule.id +"");
                    update_schedule.is_active = false;
                    dbHelper.updateSchedule(update_schedule);
                    cancelSchedule(getItem(position), position, false);
                    viewHolder.alarm_on_off.setChecked(getItem(position).is_active);
                }
            }
        });

        viewHolder.delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Schedule alarm_delete = getItem(position);
            cancelSchedule(alarm_delete, position, true);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public void cancelSchedule(Schedule alarm_delete, int position, boolean delete) {
        List<RepeatedDay> list_delete_repeatDay = alarm_delete.schedule_repeat_day;
        for (int i = 0 ; i < list_delete_repeatDay.size(); i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity.getApplicationContext(),
                    list_delete_repeatDay.get(i).id_alarm, my_intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarm_manager.cancel(pendingIntent);
        }

        if (delete){
            dbHelper.deleteSchedule(getSchedule(alarm_delete.id));

            dataSet.remove(position);
            notifyDataSetChanged();

        }
    }

    public Schedule getSchedule(int id) {
        dbHelper = new DatabaseHelper(mActivity.getApplicationContext());

        for (Schedule data : dbHelper.selectAllSchedule()){
            if (data.id == id){
                return data;
            }
        }
        return null;
    }

    public void createAlarmAgain(Schedule alarm) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.time.getHours());
        calendar.set(Calendar.MINUTE, alarm.time.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent myIntent = new Intent(mActivity, AlarmReceiver.class);
        myIntent.putExtra("waktu",alarm.time.getHours() +":" + alarm.time.getMinutes());
        myIntent.setAction(Intent.ACTION_MAIN);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        // get all repeated day
        List<RepeatedDay> listAlarmRepeat = alarm.schedule_repeat_day;
        for (int i = 0; i < listAlarmRepeat.size(); i++){

            Calendar today = Calendar.getInstance();
            if (listAlarmRepeat.get(i).day < today.get(Calendar.DAY_OF_WEEK)) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            else if (listAlarmRepeat.get(i).day == today.get(Calendar.DAY_OF_WEEK) && (calendar.get(Calendar.HOUR_OF_DAY) < today.get(Calendar.HOUR_OF_DAY) || today.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY)
                    && calendar.get(Calendar.MINUTE) <= today.get(Calendar.MINUTE))) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity.getApplicationContext(), listAlarmRepeat.get(i).id_alarm, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            calendar.set(Calendar.DAY_OF_WEEK, listAlarmRepeat.get(i).day);
            Log.i("sekarang tanggal: ", calendar.toString());
            alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }
    }
}
