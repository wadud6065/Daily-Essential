package com.example.dailyessential.reminder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.dailyessential.db.Reminder;

public class ReminderDiffCallback extends DiffUtil.ItemCallback<Reminder> {

    @Override
    public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
        return oldItem.getTime().equals(newItem.getTime());
    }
}
