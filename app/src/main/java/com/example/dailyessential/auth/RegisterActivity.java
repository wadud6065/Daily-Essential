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
import com.example.dailyessential.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextInputLayout emailTextLayout, passwordTextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);
        Button registerBtn = findViewById(R.id.btnRegister);
        TextView logInText = findViewById(R.id.gotoRegister);
        progressBar = findViewById(R.id.progressBar);
        emailTextLayout = findViewById(R.id.usernameTextLayout);
        passwordTextLayout = findViewById(R.id.passwordTextLayout);
        mAuth = FirebaseAuth.getInstance();

        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if(emailText.isEmpty()) {
            emailTextLayout.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailTextLayout.setError("Please Provide a valid email");
            email.requestFocus();
            return;
        }

        if(Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailTextLayout.setError(null);
        }

        if(passwordText.isEmpty() || passwordText.length() < 6) {
            passwordTextLayout.setError("Enter password With minimum length 6");
            password.requestFocus();
            return;
        }

        if(passwordText.length() == 6) {
            passwordTextLayout.setError(null);
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(RegisterActivity.this, DashBoard.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to Register! Try Again",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}