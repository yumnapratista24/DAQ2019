package com.android.test.ueueueu.model;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

public class Schedule {
    public int id;
    public Time time;
    public boolean is_repeat;
    public int id_method;
    public boolean is_active;
    public int[] schedule_repeat_day;

    Schedule() {
    }

    public Schedule(int id, Time time, boolean is_repeat, int id_method, boolean is_active, int[] schedule_repeat_day) {
        this.id = id;
        this.time = time;
        this.is_repeat = is_repeat;
        this.id_method = id_method;
        this.is_active = is_active;
        this.schedule_repeat_day = schedule_repeat_day;
    }
}
