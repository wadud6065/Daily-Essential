package com.example.dailyessential;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dailyessential.auth.LogInActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.firebase.auth.FirebaseAuth;

/**
 * It's mainly splash screen activity.
 * */

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;

    //Hook
    View first, second, third, fourth, fifth, sixth;
    TextView appName, slogan;

    //Animation
    Animation topAnimation, bottomAnimation, middleAnimation;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        //Hook
        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.fifth_line);
        sixth = findViewById(R.id.sixth_line);

        appName = findViewById(R.id.appName);
        slogan = findViewById(R.id.slogan);

        //Setting animation
        first.setAnimation(topAnimation);
        second.setAnimation(topAnimation);
        third.setAnimation(topAnimation);
        fourth.setAnimation(topAnimation);
        fifth.setAnimation(topAnimation);
        sixth.setAnimation(topAnimation);

        appName.setAnimation(middleAnimation);
        slogan.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startApp();
            }
        }, SPLASH_TIME_OUT);

    }

    /**
     * In this code there is some code for making animation. Example: Pair is array. It takes two
     * parameters. These two item make animation.
     * */
    public void startApp() {
        if (fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(appName, "logoName");
            pairs[1] = new Pair<View, String>(slogan, "sloganName");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(intent, options.toBundle());
            finish();

        } else {
            Intent intent = new Intent(MainActivity.this, DashBoard.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(appName, "logoName");
            pairs[1] = new Pair<View, String>(slogan, "sloganName");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(intent, options.toBundle());
            finish();
        }
    }
}