package com.example.dailyessential.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.dailyessential.DailyEssential;
import com.example.dailyessential.R;
import com.example.dailyessential.db.AppExecutors;
import com.example.dailyessential.db.ReminderDatabase;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManagerCompat = NotificationManagerCompat.from(context);

        int alarmId = intent.getIntExtra("alarmId", 0);

        Notification notification = new NotificationCompat.Builder(context, DailyEssential.CHANNEL_ID)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("description"))
                .setSmallIcon(R.drawable.notification_active)
                .build();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));

        notificationManagerCompat.notify(1, notification);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ReminderDatabase.getInstance(context.getApplicationContext()).reminderDao().deletePersonById(alarmId);
            }
        });
    }
}
