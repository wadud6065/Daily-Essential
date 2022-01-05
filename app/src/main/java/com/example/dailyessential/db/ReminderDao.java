package com.example.dailyessential.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Query("SELECT * FROM REMINDER ORDER BY ID")
    List<Reminder> loadAllReminders();

    @Query("SELECT * FROM REMINDER WHERE alarmId = :alarmId")
    Reminder deletePersonById(int alarmId);
}
