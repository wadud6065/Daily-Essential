package com.example.dailyessential.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.dailyessential.R;
import com.example.dailyessential.dailyroutine.CalendarActivity;
import com.example.dailyessential.db.ReminderDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private int min,hour;
    private String tittle, description;
    private ReminderDao database;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                ReminderActivity.this,
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
}