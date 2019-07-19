package com.android.test.ueueueu.home_page;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import java.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.alarm_main_app.AlarmReceiver;
import com.android.test.ueueueu.model.DataModel;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;
import static com.android.test.ueueueu.home_page.MainActivity.PREFS;

/**
 * Created by Mehul Garg on 09-10-2017.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private ArrayList<DataModel> dataSet;
    Context mContext;

    AlarmManager alarm_manager;
    PendingIntent pending_intent;
    LayoutInflater mInflator;
    Activity mActivity;

    // View lookup cache
    private static class ViewHolder {
        TextView time;
        Button alarm_on, alarm_off, delete_row;
        // initialize alarm manager
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context, LayoutInflater mInflator, Activity mActivity) {
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
        DataModel dataModel=(DataModel)object;
    }

    private int lastPosition = -1;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(final int position, View convertView, ViewGroup parent) {

        alarm_manager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        // Get the data item for this position
        final DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.alarm_on = (Button) convertView.findViewById(R.id.alarm_on);
            viewHolder.alarm_off = (Button) convertView.findViewById(R.id.alarm_off);
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

        String abcd = dataModel.getTime();

        int l = abcd.indexOf(':');
        int ll = abcd.lastIndexOf(':');
        int yui;

        for(yui = l+1; yui < abcd.length(); yui++)
            if(abcd.charAt(yui)==':')
                break;

        String opqw, button_on_or_off;

        final int hour = Integer.parseInt(abcd.substring(0,l));
        if(ll==l) {
            opqw = abcd.substring(l + 1);
            button_on_or_off = "";
        }
        else {
            opqw = abcd.substring(l + 1, yui);
            button_on_or_off = abcd.substring(yui + 1);
        }

        Log.d("Tag :: ", opqw);
        final int minute = Integer.parseInt(opqw);

        // convert int to string
        String hour_string = String.valueOf(hour);
        String minute_string = String.valueOf(minute);

        // String button_on_or_off = abcd.substring(ll+1);
        // if(button_on_or_off.equals(minute_string))
        // button_on_or_off = "";

        Log.d("Tag button :: ",button_on_or_off);

        if (hour > 12) {
            hour_string = String.valueOf(hour - 12);
        }
        if (hour == 0) {
            hour_string = "00";
        }
        if (minute < 10) {
            minute_string = "0" + String.valueOf(minute);
        }

        final String hjk = hour_string + ':' + minute_string;

        viewHolder.time.setText(hour_string + ':' + minute_string);

        if(button_on_or_off.equals("ONN")) {
            viewHolder.alarm_on.setVisibility(View.INVISIBLE);
            viewHolder.alarm_off.setVisibility(View.VISIBLE);
        }
        //if(button_on_or_off.equals("OFF")) {
        //    viewHolder.alarm_on.setVisibility(View.VISIBLE);
        //    viewHolder.alarm_off.setVisibility(View.INVISIBLE);
        //}


        viewHolder.delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences ex1 = getContext().getSharedPreferences(PREFS, 0);
                String c = ex1.getString("message","empty");

                    String ghi = dataModel.getTime();
                    SharedPreferences.Editor editor12 = ex1.edit();
                    String lm[] = c.split(" ");
                    String fs = "";
                    for (int y5 = 0; y5 < lm.length; y5++) {
                        if (!lm[y5].equals(ghi))
                            fs = fs + lm[y5] + " ";
                    }
                    editor12.putString("message", fs);
                    editor12.commit();

                    dataSet.remove(position);
                    notifyDataSetChanged();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
