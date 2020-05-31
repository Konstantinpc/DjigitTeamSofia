package com.example.djigitteamsofia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.djigitteamsofia.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class Frag1 extends Fragment {

    private DatabaseReference reff;
    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1_layout, container, false);
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        reff= FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double mc=0.0;
                double td=0.0;
                double av=0.0;
                int bc=0;
                long stime=0, dtime=0;
                long diff=0;



                Date s_date = new Date();
                Date d_date = new Date();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("Trips").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                if (dataSnapshot.child("Trips").getValue() != null) {
                    // The child doesn't exist

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();

                        if(next.child("start_date").child("da"))
                        if(mc<next.child("maxSpeed").getValue(Double.class)){
                            mc=next.child("maxSpeed").getValue(Double.class);
                        }
                        bc+=next.child("burnedCalories").getValue(Integer.class);
                        td+=next.child("totalDistance").getValue(Double.class);

                        String s_de=next.child("start_date").getValue().toString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                        stime = next.child("startDate").child("time").getValue(Long.class);
                        dtime = next.child("destinationDate").child("time").getValue(Long.class);
                        diff=diff+(dtime-stime);



                    }

                    long seconds=1000;
                    long minutes=seconds*60;
                    long hours=minutes*60;
                    long days=hours*24;
                    double av_time=(double)diff/(double)hours;
                    av=td/av_time;
                    long total_days=diff/days;
                    diff=diff%days;
                    long total_hours=diff/hours;
                    diff=diff%hours;
                    long total_minutes=diff/minutes;
                    diff=diff%minutes;
                    long total_seconds=diff/seconds;
                    diff=diff%seconds;
                    td = (double) Math.round(td * 1000) / 1000;
                    av = (double) Math.round(av * 1000) / 1000;
                    mc = (double) Math.round(mc * 1000) / 1000;
                    String av_speed = Double.valueOf(av).toString();
                    String m_c=Double.valueOf(mc).toString();
                    String b_c=Integer.valueOf(bc).toString();
                    String t_d=Double.valueOf(td).toString();
                    String total_time_cycling= total_days+"d:"+total_hours+"h:"+total_minutes+"m:"+total_seconds+"s";

                    reff.child("History").child("maxSpeed").setValue(mc);
                    total_max_speed.setText(m_c+" km/h");
                    reff.child("History").child("totalDistance").setValue(td);
                    total_distance.setText(t_d+" km");
                    reff.child("History").child("burnedCalories").setValue(bc);
                    total_burned_calories.setText(b_c+" kcal");
                    reff.child("History").child("averageSpeed").setValue(av);
                    total_average_speed.setText(av_speed+" km/h");
                    reff.child("History").child("totalTime").setValue(total_time_cycling);
                    total_time.setText(total_time_cycling);
                }else{
                    reff.child("History").child("maxSpeed").setValue("0");
                    reff.child("History").child("totalDistance").setValue("0");
                    reff.child("History").child("burnedCalories").setValue("0");
                    reff.child("History").child("averageSpeed").setValue("0");
                    reff.child("History").child("totalTime").setValue("0d:0h:0m:0s");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 0),
                new DataPoint(5, 23),
                new DataPoint(6, 8)
        });
        graph.addSeries(series);



        return view;
    }
}