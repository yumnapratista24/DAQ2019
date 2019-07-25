package com.android.test.ueueueu.helper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.test.ueueueu.R;
import com.android.test.ueueueu.model.RepeatedDay;
import com.android.test.ueueueu.model.Schedule;
import com.android.test.ueueueu.model.Surah;
import com.android.test.ueueueu.model.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "ingetQuran";

    // Table Names
    private static final String TABLE_SURAH = "surah";
    private static final String TABLE_WORD = "word";
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_SCHEDULE_REPEAT_DAY = "repeated_day";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO : loop over sql file
        try {
            int insertCount = insertFromFile(context, R.raw.surah, db);
            insertCount += insertFromFile(context, R.raw.word, db);
            insertCount += insertFromFile(context, R.raw.schedule, db);
            insertCount += insertFromFile(context, R.raw.repeated_day, db);
            System.out.println(insertCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int insertFromFile(Context context, int resourceId, SQLiteDatabase db) throws IOException {
        int result = 0;

        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
            result++;
        }
        insertReader.close();

        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURAH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_REPEAT_DAY);

        // create new tables
        onCreate(db);
    }




    // -------------------------------------------------------------- \\
    private List<Surah> selectSurah(String selectQuery) {
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<Surah> surahs = new ArrayList<Surah>();
        if (c.moveToFirst()) {
            do {
                Surah surah = new Surah(c);

                surahs.add(surah);
            } while (c.moveToNext());
        }

        return surahs;
    }

    // TODO : Get all surah
    public List<Surah> getAllSurah() {
        return selectSurah("SELECT * FROM " + TABLE_SURAH);
    }

    // TODO : Get choosen surah
    public List<Surah> getAllChoosenSurah() {
        return selectSurah("SELECT * FROM " + TABLE_SURAH + " WHERE is_choosen = 1");
    }

    // TODO : UPDATE is_choosen surah
    public int updateSurah(Surah surah) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = surah.getContentValues();
        return db.update(TABLE_SURAH, values, "no_surah = ?",
                new String[]{String.valueOf(surah.no_surah)});

//        db.execSQL("UPDATE " + TABLE_SURAH + " SET is_choosen = '" + surah.is_choosen +
//        "' WHERE no_surah = '" + surah.no_surah + "'");
    }

    public int updateSurah(List<Surah> surahs) {
        int error_message = 1;
        for (Surah surah: surahs) {
            if (updateSurah(surah) != 1) {
                error_message = 0;
            }
        }
        return error_message;
    }



    // WORD
    private List<Word> selectWord(String selectQuery) {
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<Word> words = new ArrayList<Word>();
        if (c.moveToFirst()) {
            do {
                Word word = new Word(c);

                words.add(word);
            } while (c.moveToNext());
        }

        return words;
    }

    public List<Word> selectAllSurahWord(int no_surah) {
        return selectWord("SELECT * FROM " + TABLE_WORD + " WHERE no_surah = " + no_surah);
    }

    public List<Word> selectAllWord() {
        return selectWord("SELECT * FROM " + TABLE_WORD);
    }

    public List<Word> selectAyah(int no_surah, int no_ayah) {
        return selectWord(String.format("SELECT * FROM %s WHERE no_surah = %s AND no_ayah = %s", TABLE_WORD, no_surah, no_ayah));
    }

    public Word selectWordById(int id) {
        return selectWord("SELECT * FROM " + TABLE_WORD + " WHERE id = " + id).get(0);
    }

    public List<Word> selectAllSurahFirstAyahWord(int no_surah) {
        String selectQuery = "SELECT * FROM " + TABLE_WORD + " WHERE no_surah = " + no_surah;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<Word> words = new ArrayList<Word>();
        int counter = 0;
        if (c.moveToFirst()) {
            do {
                int no_ayah = c.getInt(c.getColumnIndex("no_ayah"));
                if (counter < no_ayah) {
                    Word word = new Word(c);
                    words.add(word);

                    counter++;
                }
            } while (c.moveToNext());
        }

        return words;
    }

    // TODO : Get soal and option and kunjaw
    public List<HashMap<String, String>> getQuiz(int quizCount) {
        List<Surah> surahs = getAllChoosenSurah();

        AbstractCollection<Integer> choosenSurah;
        if (surahs.size() <= quizCount) {
            choosenSurah = new HashSet<>();
        } else {
            choosenSurah = new ArrayList<>();
        }

        Random random = new Random();
        while (choosenSurah.size() < quizCount) {
            choosenSurah.add(random.nextInt(surahs.size()));
        }

        List<HashMap<String, String>> quizs = new ArrayList<>();
        for (int i : choosenSurah) {
            Surah surah = surahs.get(i);
            quizs.add(getQuiz(surah));
        }

        return quizs;
    }

    // Implementasinya mungkin bisa mainin cursor? Good algorithm but bad maintainable hmm?
    public HashMap<String, String> getQuiz(Surah surah) {
        List<Word> first_words = selectAllSurahFirstAyahWord(surah.no_surah);

        Collections.shuffle(first_words);
        LinkedHashSet<Word> uniqueWord = new LinkedHashSet<Word>(first_words);

        Word wordToFill = (Word) uniqueWord.toArray()[0];

        String question = "";

        if (wordToFill.no_ayah == 1) {
            List<Word> currAyah = selectAyah(wordToFill.no_surah, wordToFill.no_ayah);

            int counter = 0;
            for (int i = 1; i <= 4; i++) {
                if (counter > 3) {
                    break;
                }
                question += " " + currAyah.get(i).word;
                counter++;
            }

            if (currAyah.size() > 4) {
                try {
                    question += new String("٠٠٠".getBytes(), "UTF-8");
                } catch (Exception e) {
                }
            }
            else {
                try {
                    question += new String("۝".getBytes(), "UTF-8");
                } catch (Exception e) {
                }
            }

            // PERTANYAANNYA PAKE TITIK2?
            try {
                question = new String("ـــ".getBytes(), "UTF-8") + question;
            } catch (Exception e) {
            }

        } else {
            List<Word> precedenceAyah = selectAyah(wordToFill.no_surah, wordToFill.no_ayah - 1);

            try {
                question = new String("۝".getBytes(), "UTF-8");
            } catch (Exception e) {
            }

            int counter = 0;
            for (int i = precedenceAyah.size() - 1; i >= 0; i--) {
                if (counter > 3) {
                    break;
                }
                question = precedenceAyah.get(i).word + " " + question;
                counter++;
            }

            if (precedenceAyah.size() > 4) {
                try {
                    question = new String("٠٠٠".getBytes(), "UTF-8") + question;
                } catch (Exception e) {
                }
            }

            // PERTANYAANNYA PAKE TITIK2?
            try {
                question += new String("ـــ".getBytes(), "UTF-8");
            } catch (Exception e) {
            }
        }

        List<Word> options = new ArrayList<Word>();
        int counter = 0;
        for (Word word:uniqueWord) {
            if (counter > 3) {
                break;
            }
            options.add(word);
            counter ++;
        }

        for (int i = options.size(); i < 4; i++) {
            int fillerId = new Random().nextInt(77431) + 1;
            Word filler = selectWordById(fillerId);
            options.add(filler);
        }

        Collections.shuffle(options);
        int answerIndex = 0;
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).equals(wordToFill)) {
                answerIndex = i;
                break;
            }
        }

        HashMap<String, String> result = new HashMap<String, String>();
        result.put("surah_name", surah.surah_name);
        result.put("no_ayah", Integer.toString(wordToFill.no_ayah));
        result.put("question", question);
        result.put("option_0", options.get(0).word);
        result.put("option_1", options.get(1).word);
        result.put("option_2", options.get(2).word);
        result.put("option_3", options.get(3).word);
        result.put("answer", Integer.toString(answerIndex));
        return result;
    }



    // TODO : CRUD Schedule
    // CREATE
    public long createSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = schedule.getContentValues();

        // insert row
        long id = db.insert(TABLE_SCHEDULE, null, values);

        // insert day
        for (RepeatedDay repeatedDay : schedule.schedule_repeat_day) {
            repeatedDay.id_schedule = schedule.id;
            ContentValues repeated_day = repeatedDay.getContentValues();
            db.insert(TABLE_SCHEDULE_REPEAT_DAY, null, repeated_day);
        }

        return id;
    }

    // Dipake anas
    public long createRepeatedDay(Schedule schedule, RepeatedDay repeated_day) {
        SQLiteDatabase db = this.getWritableDatabase();
        repeated_day.id_schedule = schedule.id;
        ContentValues values = repeated_day.getContentValues();
        long id = db.insert(TABLE_SCHEDULE_REPEAT_DAY, null, values);
        return id;
    }

    // Read
    private List<Schedule> selectSchedule(String selectQuery) {
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<Schedule> schedules = new ArrayList<Schedule>();
        if (c.moveToFirst()) {
            do {
                Schedule schedule = new Schedule(c);
                schedule.schedule_repeat_day = selectScheduleRepeatedDays(schedule.id, db);

                schedules.add(schedule);
            } while (c.moveToNext());
        }

        return schedules;
    }

    public List<Schedule> selectAllSchedule() {
        return selectSchedule("SELECT * FROM " + TABLE_SCHEDULE);
    }

    private List<RepeatedDay> selectRepeatedDay(String selectQuery, SQLiteDatabase db) {
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        List<RepeatedDay> repeatedDays = new ArrayList<RepeatedDay>();
        if (c.moveToFirst()) {
            do {
                RepeatedDay repeatedDay = new RepeatedDay(c);

                repeatedDays.add(repeatedDay);
            } while (c.moveToNext());
        }

        return repeatedDays;
    }

    private List<RepeatedDay> selectScheduleRepeatedDays(int id_schedule, SQLiteDatabase db) {
        return selectRepeatedDay("SELECT * FROM " + TABLE_SCHEDULE_REPEAT_DAY + " WHERE id_schedule = " + id_schedule, db);
    }

    // Update
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = schedule.getContentValues();
        return db.update(TABLE_SCHEDULE, values, "id = ?",
                new String[]{String.valueOf(schedule.id)});
    }

    // DELETE
    public void deleteSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE, "id = ?",
                new String[]{String.valueOf(schedule.id)});

        for (RepeatedDay repeated_day: schedule.schedule_repeat_day) {
            db.delete(TABLE_SCHEDULE_REPEAT_DAY, "id_alarm = ?",
                    new String[]{String.valueOf(repeated_day.id_alarm)});
        }
    }

    // Buat Anas
    public void deleteRepeatedDay(RepeatedDay repeated_day) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE_REPEAT_DAY, "id_alarm = ?",
                new String[]{String.valueOf(repeated_day.id_alarm)});
    }


    // TODO : !!!CHALLANGE!!! Kalo udh sabi, bikin dah user_log
    // TODO : !!!CHALLANGE!!! Alarm

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}

    /**
     * get datetime
     * */
//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
//}

//    String ayam = "abc";
//    Class ayam2 = ayam.getClass();
//        ayam2.getDeclaredFields()

//    HashSet<String> a = new HashSet<>();
//        try{
//                a.add(new String("تعطي يونيكود رقما فريدا لكل حرف".getBytes(), "UTF-8"));
//                }
//                catch (Exception e){
//                System.out.println("Failed");
//                }
//
//                try{
//                a.add(new String("تعطي يونيكود رقما فريدا لكل حرف".getBytes(), "UTF-8"));
//                }
//                catch (Exception e){
//                System.out.println("Failed");
//                }
//
//                System.out.println(a);