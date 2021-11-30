package com.example.dailyessential.money.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.dailyessential.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayAnalytics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayAnalytics extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayAnalytics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayAnalytics.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayAnalytics newInstance(String param1, String param2) {
        TodayAnalytics fragment = new TodayAnalytics();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView totalBudgetAmountTextView;

    AnyChartView anyChartView;
    DatabaseReference personalRef;
    String onlineUserId = "";
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_analytics, container, false);
        // Inflate the layout for this fragment

        anyChartView = view.findViewById(R.id.anyChartView);
        totalBudgetAmountTextView = view.findViewById(R.id.totalBudgetAmountTextView);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        updateForGraph();
        getTotalDaySpending();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {

                    @Override
                    public void run() {
                            loadGraph();
                    }
                }, 2
        );

        return view;
    }

    private void updateForGraph() {
        String itemNday, child;
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        //Set value in personalRef for transport
        itemNday = "Transport"+date;
        child = "dayTrans";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for food
        itemNday = "Food"+date;
        child = "dayFood";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for House Expenses
        itemNday = "House Expenses"+date;
        child = "dayHouse";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Entertainment
        itemNday = "Entertainment"+date;
        child = "dayEnt";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Education
        itemNday = "Education"+date;
        child = "dayEdu";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Charity
        itemNday = "Charity"+date;
        child = "dayCha";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Apparel and Services
        itemNday = "Apparel and Services"+date;
        child = "dayApp";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Health
        itemNday = "Health"+date;
        child = "dayHea";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Personal Expenses
        itemNday = "Personal Expenses"+date;
        child = "dayPer";
        FirebaseFunction(itemNday, child);

        //Set value in personalRef for Other
        itemNday = "Other"+date;
        child = "dayOther";
        FirebaseFunction(itemNday, child);
    }

    private void FirebaseFunction(String itemNday, String child) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    personalRef.child(child).setValue(totalAmount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDaySpending() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("expenses")
                .child(onlineUserId);
        Query query = databaseReference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    int totalAmount = 0;
                    for(DataSnapshot ds: snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>)ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    totalBudgetAmountTextView.setText("Total day's spending: "+ totalAmount);
                } else {
                    totalBudgetAmountTextView.setText("You've not spent today");
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGraph() {
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int traTotal;
                    if(snapshot.hasChild("dayTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("dayTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("dayFood")){
                        foodTotal = Integer.parseInt(snapshot.child("dayFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("dayHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("dayEnt")){
                        entTotal = Integer.parseInt(snapshot.child("dayEnt").getValue().toString());
                    }else {
                        entTotal=0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("dayEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("dayEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("dayCha")){
                        chaTotal = Integer.parseInt(snapshot.child("dayCha").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("dayApp")){
                        appTotal = Integer.parseInt(snapshot.child("dayApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("dayHea")){
                        heaTotal = Integer.parseInt(snapshot.child("dayHea").getValue().toString());
                    }else {
                        heaTotal =0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("dayPer")){
                        perTotal = Integer.parseInt(snapshot.child("dayPer").getValue().toString());
                    }else {
                        perTotal=0;
                    }
                    int othTotal;
                    if (snapshot.hasChild("dayOther")){
                        othTotal = Integer.parseInt(snapshot.child("dayOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", traTotal));
                    data.add(new ValueDataEntry("House exp", houseTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("Entertainment", entTotal));
                    data.add(new ValueDataEntry("Education", eduTotal));
                    data.add(new ValueDataEntry("Charity", chaTotal));
                    data.add(new ValueDataEntry("Apparel", appTotal));
                    data.add(new ValueDataEntry("Health", heaTotal));
                    data.add(new ValueDataEntry("Personal", perTotal));
                    data.add(new ValueDataEntry("other", othTotal));

                    pie.data(data);
                    pie.title("Daily Analytics");
                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);

                    pie.legend().title()
                            .text("Items Spent On")
                            .padding(0d, 0d, 10d, 0d);

                    pie.legend()
                            .position("center-bottom")
                            .itemsLayout(LegendLayout.HORIZONTAL)
                            .align(Align.CENTER);

                    anyChartView.setChart(pie);
                }
                else {
                    Toast.makeText(getContext(),"Child does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Child does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}