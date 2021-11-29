package com.example.dailyessential.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyessential.DashBoard;
import com.example.dailyessential.MainActivity;
import com.example.dailyessential.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This is an activity for log in
 * */
public class LogInActivity extends AppCompatActivity {

    private EditText email, password;
    private Button logInBtn;
    private TextView registerText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        logInBtn = findViewById(R.id.logInBtn);
        registerText = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        /**
         * If you don't have an account then this part will take you to the register activity
         * */
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser();
            }
        });
    }

    // Method for completing log in task
    private void logInUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if(emailText.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Please Provide a valid email");
            email.requestFocus();
            return;
        }

        if(passwordText.isEmpty() || passwordText.length() < 6) {
            password.setError("Enter password With minimum length 6");
            password.requestFocus();
            return;
        }

        if(passwordText.length() == 6) {
            password.setError(null);
        }

        progressBar.setVisibility(View.VISIBLE);

        //It's a firebase authentication
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(LogInActivity.this, DashBoard.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            Toast.makeText(LogInActivity.this,
                                    "Failed to LogIn! Try Again",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}