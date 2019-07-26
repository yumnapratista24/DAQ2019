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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.model.Surah;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 642174 on 12/06/2019.
 */

public class AnswerSheet extends Activity {

    private HashMap<String,String> dict = new HashMap<>();
    private DatabaseHelper dbHelper;
    private List<HashMap<String,String>> quizzes;
    private AlertDialog alertDialog;

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

        dbHelper = new DatabaseHelper(this);

        Surah surah = dbHelper.getAllSurah().get(0);

        surah.is_choosen = true;

        dbHelper.updateSurah(surah);
        dbHelper.updateSurah(dbHelper.getAllSurah().get(1));
        dbHelper.updateSurah(dbHelper.getAllSurah().get(2));
        dbHelper.updateSurah(dbHelper.getAllSurah().get(3));

        quizzes = dbHelper.getQuiz(1);

        dict = quizzes.get(0);

        String kunjaw = dict.get("answer");
        Log.i("KUNJAW CUKK",kunjaw);
        int jawaban;

        if(kunjaw.equals("0")) jawaban = R.id.A;
        else if(kunjaw.equals("1")) jawaban = R.id.B;
        else if(kunjaw.equals("2")) jawaban = R.id.C;
        else if(kunjaw.equals("3")) jawaban = R.id.D;
        else jawaban = 20000;

        Intent intent = getIntent();
        String waktu = intent.getStringExtra("waktu");

        View dialogView = layoutInflater.inflate(R.layout.alarm_answer_sheet, null);

        // set nama surat dan ayat
        TextView namaSuratAyat = (TextView) dialogView.findViewById(R.id.nama_surat_ayat);
        namaSuratAyat.setText("Surat " + dict.get("surah_name") + " ayat " + dict.get("no_ayah"));

        // set soal
        TextView soal = (TextView) dialogView.findViewById(R.id.soal);
        soal.setText(dict.get("question"));
        RadioButton radioA = (RadioButton) dialogView.findViewById((R.id.A));
        radioA.setText(dict.get("option_0"));
        RadioButton radioB = (RadioButton) dialogView.findViewById((R.id.B));
        radioB.setText(dict.get("option_1"));
        RadioButton radioC = (RadioButton) dialogView.findViewById((R.id.C));
        radioC.setText(dict.get("option_2"));
        RadioButton radioD = (RadioButton) dialogView.findViewById((R.id.D));
        radioD.setText(dict.get("option_3"));

        // set jawaban
        setJawaban(jawaban, dialogView);

        TextView tekswaktu = dialogView.findViewById(R.id.waktu);
        tekswaktu.setText(waktu);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
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
                    Toast.makeText(AnswerSheet.this, "BENER CUKK", Toast.LENGTH_SHORT).show();
                    Log.i("JAWABAN","SUCCESS");
                    alertDialog.dismiss();
                    finish();
                } else{
                    Toast.makeText(AnswerSheet.this, "SALAH CUKK " + jawaban + " bukan " + i, Toast.LENGTH_SHORT).show();
                    Log.i("JAWABAN","SALAHH CUKKK");
                }
            }
        });
    }

    private void bikinPertanyaan(){

        while()

        dict = quizzes.get(0);

        String kunjaw = dict.get("answer");
        Log.i("KUNJAW CUKK",kunjaw);
        int jawaban;

        if(kunjaw.equals("0")) jawaban = R.id.A;
        else if(kunjaw.equals("1")) jawaban = R.id.B;
        else if(kunjaw.equals("2")) jawaban = R.id.C;
        else if(kunjaw.equals("3")) jawaban = R.id.D;
        else jawaban = 20000;

        Intent intent = getIntent();
        String waktu = intent.getStringExtra("waktu");

        View dialogView = getLayoutInflater().inflate(R.layout.alarm_answer_sheet, null);

        // set nama surat dan ayat
        TextView namaSuratAyat = (TextView) dialogView.findViewById(R.id.nama_surat_ayat);
        namaSuratAyat.setText("Surat " + dict.get("surah_name") + " ayat " + dict.get("no_ayah"));

        // set soal
        TextView soal = (TextView) dialogView.findViewById(R.id.soal);
        soal.setText(dict.get("question"));
        RadioButton radioA = (RadioButton) dialogView.findViewById((R.id.A));
        radioA.setText(dict.get("option_0"));
        RadioButton radioB = (RadioButton) dialogView.findViewById((R.id.B));
        radioB.setText(dict.get("option_1"));
        RadioButton radioC = (RadioButton) dialogView.findViewById((R.id.C));
        radioC.setText(dict.get("option_2"));
        RadioButton radioD = (RadioButton) dialogView.findViewById((R.id.D));
        radioD.setText(dict.get("option_3"));

        // set jawaban
        setJawaban(jawaban, dialogView);

    }
}
