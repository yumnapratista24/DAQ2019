package com.example.cobadatabase.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Surah {

    public int no_surah;
    public String surah_name;
    public int number_of_ayah;
    public int juz;
    public boolean is_choosen;

    public Surah() {
    }

    public Surah(int no_surah, String surah_name, int number_of_ayah, int juz, boolean is_choosen) {
        this.no_surah = no_surah;
        this.surah_name = surah_name;
        this.number_of_ayah = number_of_ayah;
        this.juz = juz;
        this.is_choosen = is_choosen;
    }

    public Surah(Cursor c) {
        this.no_surah = c.getInt((c.getColumnIndex("no_surah")));
        this.surah_name = c.getString(c.getColumnIndex("surah_name"));
        this.number_of_ayah = c.getInt(c.getColumnIndex("number_of_ayah"));
        this.juz = c.getInt(c.getColumnIndex("juz"));
        this.is_choosen = c.getInt(c.getColumnIndex("is_choosen")) == 1;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("no_surah", this.no_surah);
        values.put("surah_name", this.surah_name);
        values.put("number_of_ayah", this.number_of_ayah);
        values.put("juz", this.juz);
        values.put("is_choosen", this.is_choosen);

        return values;
    }
}