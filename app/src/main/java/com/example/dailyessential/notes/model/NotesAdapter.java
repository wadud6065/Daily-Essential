package com.example.dailyessential.notes.model;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyessential.R;
import com.example.dailyessential.money.model.Data;
import com.example.dailyessential.money.model.TodayItemsAdapter;
import com.example.dailyessential.notes.listeners.NoteListeners;

import java.util.List;
import java.util.Timer;

public class NotesAdapter extends  RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private final Context mContext;
    private final List <Note> myNoteList;
    private Timer timer;
//    private NoteListeners noteListeners;

    public NotesAdapter(Context mContext, List <Note> myNoteList) {
        this.mContext = mContext;
        this.myNoteList = myNoteList;
//        this.noteListeners = noteListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_container_note, parent,false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = myNoteList.get(position);

        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(note.getContent());
        holder.noteDateAndTime.setText(note.getDateAndTime());

        GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutNote.getBackground();
        if(note.getColor() != null) {
            gradientDrawable.setColor(Color.parseColor(note.getColor()));
        } else {
            gradientDrawable.setColor(Color.parseColor("#333333"));
        }

        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// Passing data for editing note..
//                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();
                Intent intent = new Intent(mContext, EditNoteActivity.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("content", note.getContent());
                intent.putExtra("color", note.getColor());
//                intent.putExtra("noteId", docId);
                intent.putExtra("dateTime", note.getDateAndTime());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myNoteList.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom NoteViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView noteTitle, noteContent, noteDateAndTime;
        LinearLayout layoutNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.textTitle);
            noteContent = itemView.findViewById(R.id.textContent);
            noteDateAndTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
        }
    }
}
