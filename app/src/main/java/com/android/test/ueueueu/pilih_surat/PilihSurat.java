package com.android.test.ueueueu.pilih_surat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.helper.DatabaseHelper;
import com.android.test.ueueueu.model.Surah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PilihSurat extends AppCompatActivity {

    private SurahGetter surahGetter;
    private ArrayList<String> list_surat;
    private ListView lv;
    private DatabaseHelper dbHelper;

    public PilihSurat(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_surat);
        View view = getLayoutInflater().inflate(R.layout.activity_pilih_surat, null);
        renderSurah(view);
    }

    public void showFirstTimer(Activity activity){
        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.layout_onboarding, null);

        renderSurah(dialogView);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        // Dialog if user didnt choose any surah
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle("Anda harus memilih minimal 1 surat!");
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        Button saveButton = (Button) dialogView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSurahChoosed(view)){
                    alertDialog.dismiss();
                    Toast.makeText(view.getContext(), "Surah saved!", Toast.LENGTH_SHORT).show();
                } else{
                    alertDialogBuilder.show();
                }
            }
        });
        alertDialog.show();
    }

    private void renderSurah(View view){
        dbHelper = new DatabaseHelper(view.getContext());

        List<Surah> surahs = dbHelper.getAllSurah();
        Log.i("CEK CEK",surahs.toString());

        lv = (ListView) view.findViewById(R.id.list);

        surahGetter = new SurahGetter(view.getContext(), surahs, dbHelper);

        lv.setAdapter(surahGetter);
    }

    private Boolean checkSurahChoosed(View view){
        dbHelper = new DatabaseHelper(view.getContext());

        if(dbHelper.getAllChoosenSurah().size() < 1) return false;
        return true;
    }
}