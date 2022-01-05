package com.example.dailyessential.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {Reminder.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class ReminderDatabase extends RoomDatabase {
    private static final String LOG_TAG = ReminderDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "reminderlist";
    private static ReminderDatabase sInstance;

    public static ReminderDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ReminderDatabase.class, ReminderDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract ReminderDao reminderDao();
}
