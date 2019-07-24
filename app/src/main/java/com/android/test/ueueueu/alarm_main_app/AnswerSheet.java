package com.android.test.ueueueu.alarm_main_app;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.test.ueueueu.R;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 642174 on 12/06/2019.
 */

public class AnswerSheet extends Activity {

    Map<String,String> dict = new HashMap<>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_APP_SWITCH){
            return false;
        } else if(keyCode == KeyEvent.KEYCODE_HOME){
            return false;
        } else if(keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();

        int jawaban;

        dict.put("A","BAMBANG");
        dict.put("B","BAMBANG");
        dict.put("C","BAMBANG");
        dict.put("D","BAMBANG");
        dict.put("kunjaw","A");
        String kunjaw = dict.get("kunjaw");

        if(kunjaw == "A") jawaban = R.id.A;
        else if(kunjaw == "B") jawaban = R.id.B;
        else if(kunjaw == "C") jawaban = R.id.C;
        else if(kunjaw == "D") jawaban = R.id.D;
        else jawaban = 0;

//
//        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
//        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created

        Intent intent = getIntent();
        String waktu = intent.getStringExtra("waktu");

        View dialogView = layoutInflater.inflate(R.layout.alarm_answer_sheet, null);

        setJawaban(jawaban, dialogView);

        TextView tekswaktu = dialogView.findViewById(R.id.waktu);
        tekswaktu.setText(waktu);

//        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
//        buttonOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        getWindow().setBackgroundDrawable(wallpaperDrawable);
        alertDialog.show();
    }

    private void setJawaban(final int jawaban, View view){
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.jawaban);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == jawaban){
                    Log.i("JAWABAN","SUCCESS");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else{
                    Log.i("JAWABAN","SALAHH CUKKK");
                }
            }
        });
    }
}
