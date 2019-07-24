package com.android.test.ueueueu.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Word {

    public int id;
    public int no_surah;
    public int no_ayah;
    public String word;
    public String translate;

    public Word() {
    }

    public Word(int id, int no_surah, int no_ayah, String word, String translate) {
        this.id = id;
        this.no_surah = no_surah;
        this.no_ayah = no_ayah;
        this.word = word;
        this.translate = translate;
    }

    public Word(Cursor c) {
        this.id = c.getInt((c.getColumnIndex("id")));
        this.no_surah = c.getInt(c.getColumnIndex("no_surah"));
        this.no_ayah = c.getInt(c.getColumnIndex("no_ayah"));

        try {
            this.word = new String(c.getString(c.getColumnIndex("word")).getBytes(), "UTF-8");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.translate = c.getString(c.getColumnIndex("translate"));
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this.id);
        values.put("no_surah", this.no_surah);
        values.put("no_ayah", this.no_ayah);
        values.put("word", this.word);
        values.put("translate", this.translate);

        return values;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Word) {
            return word.equals(((Word) obj).word);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }
}
