package com.example.dailyessential.reminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.dailyessential.R;
import com.example.dailyessential.db.AppExecutors;
import com.example.dailyessential.db.Reminder;
import com.example.dailyessential.db.ReminderDao;
import com.example.dailyessential.db.ReminderDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class ReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ReminderDeleteListener {
    public static final String TAG = ReminderActivity.class.getSimpleName();
    private ReminderDao database;
    private FloatingActionButton floatingActionButton;
    private Calendar calendar;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        calendar = Calendar.getInstance();
        database = ReminderDatabase.getInstance(getApplicationContext()).reminderDao();

        floatingActionButton = findViewById(R.id.idReminderAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDatePickerDialog();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.idReminderRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter(new ReminderDiffCallback(), this, database);
        recyclerView.setAdapter(adapter);

        populateReminders();
    }

    private void populateReminders() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Reminder> reminders = database.loadAllReminders();
                Log.d(TAG, "run: "+reminders);
                adapter.submitReminderList(reminders);
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

        reminderInfoDialog();
    }

    private void reminderInfoDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.fragment_reminder, null);
        EditText title = dialogView.findViewById(R.id.idReminderTittle), description = dialogView.findViewById(R.id.idReminderDes);

        new AlertDialog.Builder(this)
                .setTitle("Reminder Info")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String titleText = title.getText().toString(), descriptionText = description.getText().toString();
                        int alarmId = startAlarm(titleText, descriptionText);

                        final Reminder reminder = new Reminder(titleText, descriptionText, calendar, alarmId);
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                database.insert(reminder);

                                populateReminders();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();
    }

    private int startAlarm(String title, String description) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int alarmID = Long.valueOf(System.currentTimeMillis()).intValue();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("alarmId", alarmID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmID, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        return alarmID;
    }

    @Override
    public void deleteReminder() {
        populateReminders();
    }
}