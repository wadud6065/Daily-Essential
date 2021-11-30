package com.example.dailyessential.money;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dailyessential.DashBoard;
import com.example.dailyessential.R;
import com.example.dailyessential.money.model.Data;
import com.example.dailyessential.money.model.TodayItemsAdapter;
import com.example.dailyessential.money.view.BudgetFragment;
import com.example.dailyessential.money.view.DailyCostFragment;
import com.example.dailyessential.money.view.MonthlyAnalytics;
import com.example.dailyessential.money.view.TodayAnalytics;
import com.example.dailyessential.money.view.WeekAnalytics;
import com.example.dailyessential.money.view.WeeklyCostFragment;
import com.example.dailyessential.notes.MainNoteActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Objects;

public class MoneyDashBoard extends AppCompatActivity {

    private NavigationView nav;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private TextView totalAmountSpentOn, toolBarText;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef;

    private TodayItemsAdapter todayItemsAdapter;
    private List<Data> myDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_dash_board);

        // A customized toolbar...
        Toolbar toolbar = findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        nav = findViewById(R.id.navId);
        nav.setItemIconTintList(null);
        drawerLayout = findViewById(R.id.drawerLayoutId);
        toolBarText = findViewById(R.id.toolBarText);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /**
         * If any menu item is not selected then it'll open TodayFragment by default
         * */
        if(savedInstanceState == null) {
            DailyCostFragment todayFragment = new DailyCostFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, todayFragment, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
            nav.setCheckedItem(R.id.today);
        }

        /**
         * Navigation function for selecting menu item from navigation menu. Here is always used
         * */
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.today:
                        DailyCostFragment todayFragment = new DailyCostFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, todayFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.mybudget:
                        BudgetFragment budgetFragment = new BudgetFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, budgetFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.this_week:
                        Bundle bundle = new Bundle();
                        bundle.putString("Tag", "week");
                        WeeklyCostFragment weeklyCostFragment = new WeeklyCostFragment();
                        weeklyCostFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, weeklyCostFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.this_month:
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("Tag", "month");
                        WeeklyCostFragment monthlyCostFragment = new WeeklyCostFragment();
                        monthlyCostFragment.setArguments(bundle2);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, monthlyCostFragment)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.history:
                        break;

                    case R.id.today_analytics:
                        TodayAnalytics todayAnalytics = new TodayAnalytics();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, todayAnalytics)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.week_analytics:
                        WeekAnalytics weekAnalytics = new WeekAnalytics();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, weekAnalytics)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.month_analytics:
                        MonthlyAnalytics monthlyAnalytics = new MonthlyAnalytics();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, monthlyAnalytics)
                                .addToBackStack(null)
                                .commit();
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });
    }

    /**
     * On pressing back you'll go to the dash board activity..
     * */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(MoneyDashBoard.this, DashBoard.class);
            startActivity(intent);
            finish();
        }
    }
}