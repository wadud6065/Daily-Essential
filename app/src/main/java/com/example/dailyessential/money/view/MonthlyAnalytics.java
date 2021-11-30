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
import com.anychart.charts.Funnel;
import com.anychart.charts.Pie;
import com.anychart.charts.Waterfall;
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
import org.joda.time.Months;
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
 * Use the {@link MonthlyAnalytics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyAnalytics extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthlyAnalytics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyAnalytics.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyAnalytics newInstance(String param1, String param2) {
        MonthlyAnalytics fragment = new MonthlyAnalytics();
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
        View view = inflater.inflate(R.layout.fragment_monthly_analytics, container, false);
        // Inflate the layout for this fragment
        anyChartView = view.findViewById(R.id.anyChartView);
        totalBudgetAmountTextView = view.findViewById(R.id.totalBudgetAmountTextView);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        updateForGraph();
        getTotalMonthSpending();

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
        String itemNmonth, child;
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        //Set value in personalRef for transport
        itemNmonth = "Transport"+months.getMonths();
        child = "monthTrans";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for food
        itemNmonth = "Food"+months.getMonths();
        child = "monthFood";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for House Expenses
        itemNmonth = "House Expenses"+months.getMonths();
        child = "monthHouse";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Entertainment
        itemNmonth = "Entertainment"+months.getMonths();
        child = "monthEnt";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Education
        itemNmonth = "Education"+months.getMonths();
        child = "monthEdu";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Charity
        itemNmonth = "Charity"+months.getMonths();
        child = "monthCha";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Apparel and Services
        itemNmonth = "Apparel and Services"+months.getMonths();
        child = "monthApp";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Health
        itemNmonth = "Health"+months.getMonths();
        child = "monthHea";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Personal Expenses
        itemNmonth = "Personal Expenses"+months.getMonths();
        child = "monthPer";
        FirebaseFunction(itemNmonth, child);

        //Set value in personalRef for Other
        itemNmonth = "Other"+months.getMonths();
        child = "monthOther";
        FirebaseFunction(itemNmonth, child);
    }

    private void FirebaseFunction(String itemNMonth, String child) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNmonth").equalTo(itemNMonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Log.d("Month", "In the snapshot");
                    int totalAmount = 0;
                    for (DataSnapshot ds :  snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    personalRef.child(child).setValue(totalAmount);
                } else {
                    personalRef.child(child).setValue(0);
//                    String k =itemNMonth + String.valueOf(0);
//                    Log.d("Month", k);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalMonthSpending() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("expenses")
                .child(onlineUserId);
        Query query = databaseReference.orderByChild("month").equalTo(months.getMonths());
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
                    totalBudgetAmountTextView.setText("Total Month's spending: "+ totalAmount);
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
                    if(snapshot.hasChild("monthTrans")) {
                        traTotal = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    } else {
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("monthFood")){
                        foodTotal = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    }else {
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("monthHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    }else {
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("monthEnt")){
                        entTotal = Integer.parseInt(snapshot.child("monthEnt").getValue().toString());
                    }else {
                        entTotal=0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("monthEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("monthEdu").getValue().toString());
                    }else {
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("monthChar")){
                        chaTotal = Integer.parseInt(snapshot.child("monthChar").getValue().toString());
                    }else {
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("monthApp")){
                        appTotal = Integer.parseInt(snapshot.child("monthApp").getValue().toString());
                    }else {
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("monthHea")){
                        heaTotal = Integer.parseInt(snapshot.child("monthHea").getValue().toString());
                    }else {
                        heaTotal =0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("monthPer")){
                        perTotal = Integer.parseInt(snapshot.child("monthPer").getValue().toString());
                    }else {
                        perTotal=0;
                    }
                    int othTotal;
                    if (snapshot.hasChild("monthOther")){
                        othTotal = Integer.parseInt(snapshot.child("monthOther").getValue().toString());
                    }else {
                        othTotal = 0;
                    }

                    Waterfall waterfall = AnyChart.waterfall();

                    waterfall.title("Monthly Analysis");

                    waterfall.yScale().minimum(0d);

                    waterfall.yAxis(0).labels().format("Tk {%Value}{scale:(1000000)(1)|(mln)}");
                    waterfall.labels().enabled(true);
                    waterfall.labels().format(
                            "function() {\n" +
                                    "      if (this['isTotal']) {\n" +
                                    "        return anychart.format.number(this.absolute, {\n" +
                                    "          scale: true\n" +
                                    "        })\n" +
                                    "      }\n" +
                                    "\n" +
                                    "      return anychart.format.number(this.value, {\n" +
                                    "        scale: true\n" +
                                    "      })\n" +
                                    "    }");
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
                    data.add(new ValueDataEntry("Other", othTotal));

                    DataEntry end = new DataEntry();
                    end.setValue("x", "End");
                    end.setValue("isTotal", true);
                    data.add(end);

                    waterfall.data(data);

                    anyChartView.setChart(waterfall);
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