package anas.mtq.daq.ueueueu.alarm_main_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.test.ueueueu.R;
import anas.mtq.daq.ueueueu.helper.DatabaseHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 642174 on 12/06/2019.
 */

public class AnswerSheet extends Activity {

    private DatabaseHelper dbHelper;
    private AlertDialog alertDialog;
    private Vibrator vibrator;
    private Boolean userLeave = false;
    private List<HashMap<String,String>> quizzes;
    private int counter = 0;

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
        Log.i("Masuk", "onCreate");
        AsyncTaskRunner async = new AsyncTaskRunner(this);
        Intent intent = getIntent();
        int problem = intent.getIntExtra("problem",3);
        userLeave = intent.getBooleanExtra("userLeave", false);
        //if(!userLeave) async.execute(problem);
//        else{
//            ArrayList<android.os.Parcelable> map = intent.getParcelableArrayListExtra("arr_quizzes");
//            for
//            onTaskCompleted(quizzes);
//        }
        async.execute(problem);
    }

    @Override
    protected void onUserLeaveHint() {

        super.onUserLeaveHint();
        userLeave = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(AnswerSheet.this , AnswerSheet.class);
                mainIntent.putExtra("userLeave",true);
                mainIntent.putExtra("problem",quizzes.size());
                mainIntent.putExtra("waktu", getIntent().getStringExtra("waktu"));
//                ArrayList<HashMap<String,String>> arlist = new ArrayList<>();
//                for (HashMap<String,String> map:quizzes) {
//                    arlist.add(map);
//                }
//                mainIntent.putExtra("arr_quizzes",arlist);
                startActivity(mainIntent);
            }
        }, 5000);
        alertDialog.dismiss();
        vibrator.cancel();
        finish();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }

    public void onTaskCompleted() {
        LayoutInflater layoutInflater = getLayoutInflater();

        Intent intent = getIntent();

        String waktu = intent.getStringExtra("waktu");

//        Surah surah = dbHelper.getAllSurah().get(0);
//
//        surah.is_choosen = true;
//
//        dbHelper.updateSurah(surah);
//        dbHelper.updateSurah(dbHelper.getAllSurah().get(1));
//        dbHelper.updateSurah(dbHelper.getAllSurah().get(2));
//        dbHelper.updateSurah(dbHelper.getAllSurah().get(3));

        View dialogView = layoutInflater.inflate(R.layout.alarm_answer_sheet, null);

        TextView tekswaktu = dialogView.findViewById(R.id.waktu);
        tekswaktu.setText(waktu);
        bikinSoal(dialogView);


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

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 3000, 1000};
        vibrator.vibrate(pattern,0);
    }

    private void bikinSoal(View dialogView) {
        if (counter >= quizzes.size()) {
            alertDialog.dismiss();
            vibrator.cancel();
            finish();
        }
        else {
            HashMap<String, String> quiz = quizzes.get(counter);
            TextView namaSuratAyat = (TextView) dialogView.findViewById(R.id.nama_surat_ayat);
            namaSuratAyat.setText("Surat " + quiz.get("surah_name") + " ayat " + quiz.get("no_ayah"));

            String kunjaw = quiz.get("answer");
            int jawaban;

            if (kunjaw.equals("0")) jawaban = R.id.A;
            else if (kunjaw.equals("1")) jawaban = R.id.B;
            else if (kunjaw.equals("2")) jawaban = R.id.C;
            else if (kunjaw.equals("3")) jawaban = R.id.D;
            else jawaban = 20000;

            RadioGroup rg1 = (RadioGroup) dialogView.findViewById(R.id.jawaban1);
            rg1.clearCheck();
            RadioGroup rg2 = (RadioGroup) dialogView.findViewById(R.id.jawaban2);
            rg2.clearCheck();

            // set soal
            TextView soal = (TextView) dialogView.findViewById(R.id.soal);
            soal.setText(quiz.get("question"));
            RadioButton radioA = (RadioButton) dialogView.findViewById((R.id.A));
            radioA.setText(quiz.get("option_0"));
            RadioButton radioB = (RadioButton) dialogView.findViewById((R.id.B));
            radioB.setText(quiz.get("option_1"));
            RadioButton radioC = (RadioButton) dialogView.findViewById((R.id.C));
            radioC.setText(quiz.get("option_2"));
            RadioButton radioD = (RadioButton) dialogView.findViewById((R.id.D));
            radioD.setText(quiz.get("option_3"));

            // set jawaban
            setJawaban(jawaban, dialogView);
        }
    }

    private void setJawaban(final int jawaban, final View view){
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.jawaban1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == jawaban) {
                    Toast.makeText(AnswerSheet.this, "BENAR!", Toast.LENGTH_SHORT).show();
                    Log.i("JAWABAN", "SUCCESS");
                    counter++;
                    bikinSoal(view);

                } else {
                    Toast.makeText(AnswerSheet.this, "Kurang tepat, Coba lagi!", Toast.LENGTH_SHORT).show();
//                    try{
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
                }
            }
        });
        RadioGroup rg2 = (RadioGroup) view.findViewById(R.id.jawaban2);
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == jawaban) {
                    Toast.makeText(AnswerSheet.this, "BENAR!", Toast.LENGTH_SHORT).show();
                    Log.i("JAWABAN", "SUCCESS");
                    counter++;
                    bikinSoal(view);

                } else {
                    Toast.makeText(AnswerSheet.this, "Kurang tepat, Coba lagi!", Toast.LENGTH_SHORT).show();
//                    try{
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }
    protected class AsyncTaskRunner extends AsyncTask<Integer, Integer, List<HashMap<String, String>>> {

        private ProgressDialog progressDialog;

        AnswerSheet answerSheet;

        public AsyncTaskRunner(AnswerSheet answerSheet) {
            this.answerSheet = answerSheet;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Integer... integers) {
            Log.i("Masuk", "inBackground");
            DatabaseHelper db_helper = new DatabaseHelper(answerSheet);
            List<HashMap<String, String>> quizs = db_helper.getQuiz(integers[0]);
//            List<HashMap<String, String>> quizs = new ArrayList<HashMap<String, String>>();
//            HashMap<String, String> ancol = new HashMap<>();
//            ancol.put("surah_name", "Yumna");
//            ancol.put("no_ayah", Integer.toString(15));
//            ancol.put("question", "Berapa muka ente");
//            ancol.put("option_0", "14");
//            ancol.put("option_1", "17");
//            ancol.put("option_2", "24");
//            ancol.put("option_3", "68");
//            ancol.put("answer", "0");
//            quizs.add(ancol);
            return quizs;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("PREEXECUTE", "OTW");
//            progressDialog = ProgressDialog.show(AnswerSheet.this,"Logging in..","harap tunggu...");
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            Log.i("Masuk", "berhasil yey");
            answerSheet.quizzes = result;
            answerSheet.onTaskCompleted();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}

