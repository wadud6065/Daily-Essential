package com.example.dailyessential.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "reminder")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "time")
    private Calendar time;
    @ColumnInfo(name = "alarmId")
    private int alarmId;

    public Reminder(Long id, String title, String description, Calendar time, int alarmId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.alarmId = alarmId;
    }

    @Ignore
    public Reminder(String title, String description, Calendar time, int alarmId) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.alarmId = alarmId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }
}
