package com.example.dailyessential.notes.listeners;

import com.example.dailyessential.notes.model.Note;

public interface NoteListeners {
    void onNoteClicked(Note note, int position);
}
