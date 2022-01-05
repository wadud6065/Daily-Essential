package com.example.dailyessential.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.dailyessential.R;
import com.example.dailyessential.dailyroutine.CalendarActivity;
import com.example.dailyessential.db.ReminderDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String TAG = ReminderActivity.class.getSimpleName();
    private int min,hour;
    private String tittle, description;
    private ReminderDao database;
    private FloatingActionButton floatingActionButton;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        calendar = Calendar.getInstance();

        floatingActionButton = findViewById(R.id.idReminderAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDatePickerDialog();
            }
        });
    }
        private void showDatePickerDialog(){
            DatePickerDialog datePickerDialog= new DatePickerDialog(
                    this,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        AppCompatDialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
        calendar.set(year, month, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        startAlarm(calendar);
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, new Intent(this, AlarmReceiver.class), 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}