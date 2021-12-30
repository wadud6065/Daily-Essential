package com.example.dailyessential;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dailyessential.auth.LogInActivity;
import com.example.dailyessential.money.MoneyDashBoard;
import com.example.dailyessential.notes.CreateNoteActivity;
import com.example.dailyessential.notes.MainNoteActivity;
import com.example.dailyessential.todo.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoard extends AppCompatActivity {

    CardView keepNotes;

    FirebaseUser user;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        keepNotes = findViewById(R.id.keepNotesId);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();;

        /**
         * This will redirect you to keep notes activity. You can see there saved notes. You can make
         * notes and save it to online
         * */
        keepNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, MainNoteActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        /**
         * You can add budget for month. You can check your daily, weekly and monthly spendings.
         * */
        findViewById(R.id.moneyTracking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, MoneyDashBoard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        /**
         * It will log you out from the this app.
         * */
        findViewById(R.id.logOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                finish();
                Intent intent = new Intent(DashBoard.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.idTodo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, Todo.class);
                startActivity(intent);
                //    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //  finish();
            }
        });

    }

    /**
     * This is simple dialog for exiting app. This dialog will take permission from you if you want
     * to exit this app or not. This will happen if you pressed back button from dashboard activity
     * */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(DashBoard.this);
        alertDialogBuilder.setTitle("Daily Essential");
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}