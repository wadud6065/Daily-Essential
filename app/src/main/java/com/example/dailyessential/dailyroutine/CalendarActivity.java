package com.example.dailyessential.dailyroutine;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TimePicker;

import com.example.dailyessential.MainActivity;
import com.example.dailyessential.R;
import com.example.dailyessential.db.AppExecutors;
import com.example.dailyessential.db.Reminder;
import com.example.dailyessential.db.ReminderDao;
import com.example.dailyessential.db.ReminderDatabase;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ReminderDao database;
    int hour, min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        database = ReminderDatabase.getInstance(getApplicationContext()).reminderDao();

        calendarView = (CalendarView) findViewById(R.id.idcalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CalendarActivity.this,
                        AlertDialog.THEME_HOLO_DARK,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    hour= hourOfDay;
                                    min= minute;
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(0,0,0,hourOfDay,minute);

                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.insert(new Reminder("Test", "des 1", 2345L));
                database.insert(new Reminder("Test 2", "des 2", 34545L));
                database.insert(new Reminder("Test 3", "des 3", 34565L));
            }
        });
    }
}