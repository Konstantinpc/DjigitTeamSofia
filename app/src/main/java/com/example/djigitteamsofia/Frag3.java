package com.example.djigitteamsofia;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class Frag3 extends Fragment {

    private DatabaseReference reff;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    GraphView graph;
    TextView stat_for;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag3_layout, container, false);
        graph = (GraphView) view.findViewById(R.id.graph3);



        firebaseAuth = FirebaseAuth.getInstance();
        listView = (ListView) view.findViewById(R.id.samo_Levski2);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
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

                //DateFormat format = new SimpleDateFormat("d");
                DateFormat formate = new SimpleDateFormat("M");
                DateFormat y = new SimpleDateFormat("yyyy");
                final Calendar calendar = Calendar.getInstance();
                String ye=y.format(calendar.getTime());


                //calendar.set(Calendar.M,Calendar.JANUARY);
                stat_for=(TextView) view.findViewById(R.id.textView4201);
                stat_for.setText(ye);
                //String[] days = new String[31];
                Integer[] months = new Integer[12];

                for (int i = 0; i < 12; i++)
                {
                    //days[i] = format.format(calendar.getTime());
                    months[i]=i;
                  //  calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                //arrayAdapter.add(Arrays.toString(days));
                int[] d_d=new int[12];
                for (int i = 0; i < 12; i++)
                {
                    d_d[i] = 0;
                }

                Date s_date = new Date();
                Date d_date = new Date();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("Trips").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                if (dataSnapshot.child("Trips").getValue() != null) {
                    // The child doesn't exist

                    while (iterator.hasNext() ) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        for(int i=0; i<12; i++) {
                            if (next.child("startDate").child("month").getValue(Integer.class)==months[i] &&
                                    next.child("destinationDate").child("month").getValue(Integer.class)==months[i]) {
                                if (mc < next.child("maxSpeed").getValue(Double.class)) {
                                    mc = next.child("maxSpeed").getValue(Double.class);
                                }
                                bc += next.child("burnedCalories").getValue(Integer.class);
                                d_d[i]+=next.child("totalDistance").getValue(Double.class);
                                td += next.child("totalDistance").getValue(Double.class);

                                String s_de = next.child("start_date").getValue().toString();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                stime = next.child("startDate").child("time").getValue(Long.class);
                                dtime = next.child("destinationDate").child("time").getValue(Long.class);
                                diff = diff + (dtime - stime);

                            }

                        }

                    }

                    long seconds=1000;
                    long minutes=seconds*60;
                    long hours=minutes*60;
                    long dayse=hours*24;
                    double av_time=(double)diff/(double)hours;
                    av=td/av_time;
                    long total_days=diff/dayse;
                    diff=diff%dayse;
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

                    arrayAdapter.add("Max speed = "+m_c+" km/h");
                    arrayAdapter.add("Total distance = "+t_d+" km");
                    arrayAdapter.add("Burned calories = "+b_c+" kcal");
                    arrayAdapter.add("Average speed = "+av_speed+" km/h");
                    arrayAdapter.add("Total time = "+ total_time_cycling);
                    //arrayAdapter.add(Arrays.toString(d_d));

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, 0),
                            new DataPoint(1, d_d[0]),
                            new DataPoint(2, d_d[1]),
                            new DataPoint(3, d_d[2]),
                            new DataPoint(4, d_d[3]),
                            new DataPoint(5, d_d[4]),
                            new DataPoint(6, d_d[5]),
                            new DataPoint(7, d_d[6]),
                            new DataPoint(8, d_d[7]),
                            new DataPoint(9, d_d[8]),
                            new DataPoint(10, d_d[9]),
                            new DataPoint(11, d_d[10]),
                            new DataPoint(12, d_d[11]),
                            new DataPoint(12, 0),
                            new DataPoint(20, 0)



                    });
                    graph.addSeries(series);

                }else{
                    arrayAdapter.add("nMax speed = "+"0");
                    arrayAdapter.add("nTotal distance = "+"0");
                    arrayAdapter.add("nBurned calories = "+"0");
                    arrayAdapter.add("nAverage speed = "+"0");
                    arrayAdapter.add("nTotal time = "+"0d:0h:0m:0s");

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, 0),
                            new DataPoint(1, 0),
                            new DataPoint(2, 0),
                            new DataPoint(3, 0),
                            new DataPoint(4, 0),
                            new DataPoint(5, 0),
                            new DataPoint(6, 0)
                    });
                    graph.addSeries(series);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;
    }
}