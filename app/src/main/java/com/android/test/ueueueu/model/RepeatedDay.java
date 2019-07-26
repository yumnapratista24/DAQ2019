package com.android.test.ueueueu.model;

import android.content.ContentValues;
import android.database.Cursor;

public class RepeatedDay {
    public int id_schedule;
    public int id_alarm;
    public int day;

    public RepeatedDay() {
    }

    public RepeatedDay(int id_alarm, int day) {
        this.id_alarm = id_alarm;
        this.day = day;
    }

    public RepeatedDay(int id_schedule, int id_alarm, int day) {
        this.id_schedule = id_schedule;
        this.id_alarm = id_alarm;
        this.day = day;
    }

    public RepeatedDay(Cursor c) {
        this.id_schedule = c.getInt((c.getColumnIndex("id_schedule")));
        this.id_alarm = c.getInt((c.getColumnIndex("id_alarm")));
        this.day = c.getInt((c.getColumnIndex("day")));
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("id_schedule", this.id_schedule);
        values.put("id_alarm", this.id_alarm);
        values.put("day", this.day);

        return values;
    }
}
