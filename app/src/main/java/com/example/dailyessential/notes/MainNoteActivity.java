package com.example.dailyessential.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dailyessential.DashBoard;
import com.example.dailyessential.R;
import com.example.dailyessential.notes.model.EditNoteActivity;
import com.example.dailyessential.notes.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainNoteActivity extends AppCompatActivity {

    RecyclerView noteLists;
    FirebaseUser user;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Note,NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        noteLists = findViewById(R.id.notesRecyclerView);

        // query notes > uuid > myNotes
        Query query = fStore.collection("notes")
                .document(user.getUid())
                .collection("myNotes")
                .orderBy("title", Query.Direction.ASCENDING);

        //This is used for adding query in firebase database. You can access data from this allNotes
        FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        findViewById(R.id.imageAddNoteMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreateNoteActivity.class));
                // This overridePendingTransition function helps to create slide animation
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        // Initialize the noteAdapter for firebase. Here is used firebase recylcerView
        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
                String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();
                Log.d("SAD",docId);
                holder.textTitle.setText(note.getTitle());
                holder.textContent.setText(note.getContent());
                holder.textDateTime.setText(note.getDateAndTime());

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
                        Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                        intent.putExtra("title", note.getTitle());
                        intent.putExtra("content", note.getContent());
                        intent.putExtra("color", note.getColor());
                        intent.putExtra("noteId", docId);
                        intent.putExtra("dateTime", note.getDateAndTime());
                        v.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_container_note, parent, false);
                return new NoteViewHolder(view);
            }
        };

        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);
        noteAdapter.startListening();
        //Log.d("SAD",noteAdapter.getSnapshots().getSnapshot(0)+"");
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom NoteViewHolder).
     */
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textContent, textDateTime;
        LinearLayout layoutNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
        }
    }

    /**
     * This code will work when you press back button from this activity
     * **/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainNoteActivity.this, DashBoard.class);
        startActivity(intent);
        finish();
    }
}