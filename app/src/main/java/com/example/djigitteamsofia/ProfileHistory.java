package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

public class ProfileHistory extends AppCompatActivity {

    private TextView total_time, total_max_speed, total_average_speed, total_distance, total_burned_calories, name;
    private DatabaseReference reff, reff1;
    private FirebaseAuth firebaseAuth;
    private final String TAG="Na maika ti putkata = ";
    private String idValue=new String();
    private String profileValue=new String();
    private ToggleButton follow;
    private String userName;
    private long id_followers=0;
    private boolean y=false;
    private long followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        total_time=(TextView) findViewById(R.id.textView31);
        total_max_speed=(TextView) findViewById(R.id.textView32);
        total_average_speed=(TextView) findViewById(R.id.textView33);
        total_distance=(TextView) findViewById(R.id.textView34);
        total_burned_calories=(TextView) findViewById(R.id.textView36);
        name=(TextView) findViewById(R.id.textView18);
        follow=(ToggleButton) findViewById(R.id.followbutton);



        firebaseAuth = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("id")){
            Bundle exBundle = getIntent().getExtras();
            idValue = exBundle.getString("id");

        }else {
            idValue=firebaseAuth.getUid();
        }
        if(getIntent().hasExtra("profile")){
            Bundle exBundle = getIntent().getExtras();
            profileValue = exBundle.getString("profile");

        }

        reff=FirebaseDatabase.getInstance().getReference().child("Users").child(idValue);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double mc=0.0;
                double td=0.0;
                double av=0.0;
                int bc=0;
                long stime=0, dtime=0;
                long diff=0;
                followers=dataSnapshot.child("Followers").getValue(Long.class);
                Date s_date = new Date();
                Date d_date = new Date();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.child("Trips").getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                if (dataSnapshot.child("Trips").getValue() != null) {
                    // The child doesn't exist

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
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

                    userName=dataSnapshot.child("userName").getValue().toString();
                    name.setText(userName);
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
                    userName=dataSnapshot.child("userName").getValue().toString();
                    name.setText(userName);
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

        reff1=FirebaseDatabase.getInstance().getReference().child("Users").child(profileValue);

        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Following").exists()){
                    id_followers = (dataSnapshot.child("Following").getChildrenCount());
                    //if(dataSnapshot.child("Following").getValue().toString().equals(userName)) follow.setChecked(true);
                }
               if(dataSnapshot.child("Following").child(userName).exists()){
                   follow.setChecked(true);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(follow.isChecked()){
                    reff1.child("Following").child(userName).setValue(idValue);
                    reff.child("Followers").setValue(followers+1);
                }else{
                    reff1.child("Following").child(userName).removeValue();
                    reff.child("Followers").setValue(followers-1);
                    follow.setChecked(false);
                }
            }
        });

    }
}

