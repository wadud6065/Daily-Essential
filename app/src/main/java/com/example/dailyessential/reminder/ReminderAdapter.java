package com.example.dailyessential.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyessential.R;
import com.example.dailyessential.db.AppExecutors;
import com.example.dailyessential.db.Reminder;
import com.example.dailyessential.db.ReminderDao;

import java.util.List;

public class ReminderAdapter extends ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder> {

    private final Context context;
    private final ReminderDao dao;
    private final ReminderDeleteListener listener;

    protected ReminderAdapter(@NonNull DiffUtil.ItemCallback<Reminder> diffCallback, Context context, ReminderDao dao) {
        super(diffCallback);
        this.context = context;
        this.dao = dao;
        this.listener = (ReminderDeleteListener) context;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = getItem(position);
        holder.title.setText(reminder.getTitle());
        holder.description.setText(reminder.getDescription());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(reminder.getAlarmId());

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dao.delete(reminder);
                        listener.deleteReminder();
                    }
                });
            }
        });
    }

    public void submitReminderList(List<Reminder> reminders) {
        submitList(reminders);
    }

    private void cancelAlarm(int alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(PendingIntent.getBroadcast(context, alarmId, new Intent(context, AlarmReceiver.class), 0));
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder{

        TextView title, description;
        ImageButton button;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.idTitleBox);
            description = itemView.findViewById(R.id.idDescriptionBox);
            button = itemView.findViewById(R.id.idReminderClose);
        }
    }
}
