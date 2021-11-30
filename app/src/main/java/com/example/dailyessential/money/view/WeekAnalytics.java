package com.example.dailyessential.money.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
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
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekAnalytics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekAnalytics extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekAnalytics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekAnalytics.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekAnalytics newInstance(String param1, String param2) {
        WeekAnalytics fragment = new WeekAnalytics();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView totalBudgetAmountTextView;

    AnyChartView anyChartView;
    DatabaseReference personalRef;
    String onlineUserId = "";
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_analytics, container, false);
        // Inflate the layout for this fragment

        anyChartView = view.findViewById(R.id.anyChartView);
        totalBudgetAmountTextView = view.findViewById(R.id.totalBudgetAmountTextView);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        updateForGraph();
        getTotalWeekSpending();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        loadGraph();
                    }
                },
                2
        );

        return view;
    }

    private void updateForGraph() {
        String itemNweek, child;
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        //Set value in personalRef for transport
        itemNweek = "Transport"+weeks.getWeeks();
        child = "weekTrans";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for food
        itemNweek = "Food"+weeks.getWeeks();
        child = "weekFood";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for House Expenses
        itemNweek = "House Expenses"+weeks.getWeeks();
        child = "weekHouse";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Entertainment
        itemNweek = "Entertainment"+weeks.getWeeks();
        child = "weekEnt";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Education
        itemNweek = "Education"+weeks.getWeeks();
        child = "weekEdu";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Charity
        itemNweek = "Charity"+weeks.getWeeks();
        child = "weekCha";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Apparel and Services
        itemNweek = "Apparel and Services"+weeks.getWeeks();
        child = "weekApp";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Health
        itemNweek = "Health"+weeks.getWeeks();
        child = "weekHea";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Personal Expenses
        itemNweek = "Personal Expenses"+weeks.getWeeks();
        child = "weekPer";
        FirebaseFunction(itemNweek, child);

        //Set value in personalRef for Other
        itemNweek = "Other"+weeks.getWeeks();
        child = "weekOther";
        FirebaseFunction(itemNweek, child);
    }

    private void FirebaseFunction(String itemNweek, String child) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNweek").equalTo(itemNweek);
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

    private void getTotalWeekSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Weeks weeks = Weeks.weeksBetween(epoch, now);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("expenses")
                .child(onlineUserId);
        Query query = databaseReference.orderByChild("week").equalTo(weeks.getWeeks());
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
                    totalBudgetAmountTextView.setText("Total week's spending: "+ totalAmount);
                } else {
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
                    if(snapshot.hasChild("weekTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("weekTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("weekFood")){
                        foodTotal = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("weekHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("weekEnt")){
                        entTotal = Integer.parseInt(snapshot.child("weekEnt").getValue().toString());
                    }else {
                        entTotal=0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("weekEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("weekEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("weekCha")){
                        chaTotal = Integer.parseInt(snapshot.child("weekCha").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("weekApp")){
                        appTotal = Integer.parseInt(snapshot.child("weekApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("weekHea")){
                        heaTotal = Integer.parseInt(snapshot.child("weekHea").getValue().toString());
                    }else {
                        heaTotal =0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("weekPer")){
                        perTotal = Integer.parseInt(snapshot.child("weekPer").getValue().toString());
                    }else {
                        perTotal=0;
                    }
                    int othTotal;
                    if (snapshot.hasChild("weekOther")){
                        othTotal = Integer.parseInt(snapshot.child("weekOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    Cartesian cartesian = AnyChart.column();
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

                    Column column = cartesian.column(data);

                    column.tooltip()
                            .titleFormat("{%X}")
                            .position(Position.CENTER_BOTTOM)
                            .anchor(Anchor.CENTER_BOTTOM)
                            .offsetX(0d)
                            .offsetY(5d)
                            .format("Tk {%Value}{groupsSeparator: }");

                    cartesian.animation(true);
                    cartesian.title("Daily Analytics");

                    cartesian.yScale().minimum(0d);

                    cartesian.yAxis(0).labels().format("Tk {%Value}{groupsSeparator: }");

                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                    cartesian.interactivity().hoverMode(HoverMode.BY_X);

                    cartesian.xAxis(0).title("Item");
                    cartesian.yAxis(0).title("Week Spending");

                    anyChartView.setChart(cartesian);
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