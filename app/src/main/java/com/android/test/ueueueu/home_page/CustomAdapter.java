package com.android.test.ueueueu.home_page;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.android.test.ueueueu.model.RepeatedDay;
import com.android.test.ueueueu.model.Schedule;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

/**
 * Created by Mehul Garg on 09-10-2017.
 */

public class CustomAdapter extends ArrayAdapter<Schedule> implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private List<Schedule> dataSet;
    Context mContext;

    AlarmManager alarm_manager;
    PendingIntent pending_intent;
    LayoutInflater mInflator;
    Activity mActivity;

    // View lookup cache
    private static class ViewHolder {
        TextView time;
        Button delete_row;
        Switch alarm_on_off;
    }

    public CustomAdapter(List<Schedule> data, Context context, LayoutInflater mInflator, Activity mActivity) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mInflator = mInflator;
        this.mActivity = mActivity;

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
        final Schedule alarm = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.alarm_on_off = (Switch) convertView.findViewById(R.id.alarm_on_off);
            viewHolder.delete_row = (Button) convertView.findViewById(R.id.delete);

            result=convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        // create an instance of an calendar
        final Calendar calendar = Calendar.getInstance();

        //create an intent to Alarm receiver class
        final Intent my_intent = new Intent(getContext(), AlarmReceiver.class);


        DateFormat format = new SimpleDateFormat("HH:mm");

        String alarm_time_text = format.format(alarm.time);


        viewHolder.time.setText(alarm_time_text);

        viewHolder.alarm_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.alarm_on_off.setChecked(true);
                } else {
                    viewHolder.alarm_on_off.setChecked(false);
                }
            }
        });

        Log.i("Ancol2", mActivity.toString());
        viewHolder.delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Schedule alarm_delete = getItem(position);

                List<RepeatedDay> list_delete_repeatDay = alarm_delete.schedule_repeat_day;
                for (int i = 0 ; i < list_delete_repeatDay.size(); i++) {

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity.getApplicationContext(),
                            list_delete_repeatDay.get(i).id_alarm, my_intent, PendingIntent.FLAG_CANCEL_CURRENT);

                    alarm_manager.cancel(pendingIntent);
                }

                DatabaseHelper dbHelper = new DatabaseHelper(mActivity.getApplicationContext());

                for (Schedule data : dbHelper.selectAllSchedule()){
                    if (data.id == getItem(position).id){
                        dbHelper.deleteSchedule(data);
                    }
                }

                dataSet.remove(position);
                notifyDataSetChanged();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
