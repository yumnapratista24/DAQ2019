package com.android.test.ueueueu.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.sql.Time;
import java.util.List;

public class Schedule {
    public int id;
    public Time time;
    public boolean is_repeat;
    public int id_method;
    public boolean is_active;
    public List<RepeatedDay> schedule_repeat_day;

    Schedule() {
    }

    public Schedule(Time time, boolean is_repeat, boolean is_active) {
        this.time = time;
        this.is_repeat = is_repeat;
        this.id_method = 1;
        this.is_active = is_active;
    }

    public Schedule(Time time, boolean is_repeat, boolean is_active, List<RepeatedDay> schedule_repeat_day) {
        this.time = time;
        this.is_repeat = is_repeat;
        this.id_method = 1;
        this.is_active = is_active;
        this.schedule_repeat_day = schedule_repeat_day;
    }

    public Schedule(int id, Time time, boolean is_repeat, int id_method, boolean is_active, List<RepeatedDay> schedule_repeat_day) {
        this.id = id;
        this.time = time;
        this.is_repeat = is_repeat;
        this.id_method = id_method;
        this.is_active = is_active;
        this.schedule_repeat_day = schedule_repeat_day;
    }

    public Schedule(Cursor c) {
        this.id = c.getInt((c.getColumnIndex("id")));
        this.time = Time.valueOf(c.getString(c.getColumnIndex("time")));
        this.is_repeat = c.getInt(c.getColumnIndex("is_repeat")) == 1;
        this.id_method = c.getInt(c.getColumnIndex("id_method"));
        this.is_active = c.getInt(c.getColumnIndex("is_active")) == 1;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this.id);
        values.put("time", this.time.toString());
        values.put("is_repeat", this.is_repeat);
        values.put("id_method", this.id_method);
        values.put("is_active", this.is_active);

        return values;
    }
}
