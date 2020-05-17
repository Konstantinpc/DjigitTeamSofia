package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticTrips extends AppCompatActivity {

    private TextView title, start_res, start_date_res, destination_res,
            destination_date_res, max_speed_res, av_speed_res, total_distance_res,
            burned_cal_res;
    private DatabaseReference reff;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_trips);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title=(TextView) findViewById(R.id.textView);
        start_res=(TextView) findViewById(R.id.textView10);
        start_date_res=(TextView) findViewById(R.id.textView11);
        destination_res=(TextView) findViewById(R.id.textView13);
        destination_date_res=(TextView) findViewById(R.id.textView14);
        max_speed_res=(TextView) findViewById(R.id.textView16);
        av_speed_res=(TextView) findViewById(R.id.textView12);
        total_distance_res=(TextView) findViewById(R.id.textView15);
        burned_cal_res=(TextView) findViewById(R.id.textView17);

        firebaseAuth = FirebaseAuth.getInstance();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Trips");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bundle exBundle= getIntent().getExtras();
                int intValue= exBundle.getInt("intValue");
                String title_t = dataSnapshot.child(String.valueOf(intValue+1)).child("id").getValue().toString();
                title.setText(title_t+".");
                String start = dataSnapshot.child(String.valueOf(intValue+1)).child("start").getValue().toString();
                start_res.setText(start);
                String start_date = dataSnapshot.child(String.valueOf(intValue+1)).child("start_date").getValue().toString();
                start_date_res.setText(start_date);
                String destination = dataSnapshot.child(String.valueOf(intValue+1)).child("destination").getValue().toString();
                destination_res.setText(destination);
                String destination_date = dataSnapshot.child(String.valueOf(intValue+1)).child("destination_date").getValue().toString();
                destination_date_res.setText(destination_date);
                String curr_speed = dataSnapshot.child(String.valueOf(intValue+1)).child("maxSpeed").getValue().toString();
                double a = Double.valueOf(curr_speed);
                a = (double) Math.round(a * 1000) / 1000;
                max_speed_res.setText(Double.valueOf(a).toString()+" km/h");
                String av_speed = dataSnapshot.child(String.valueOf(intValue+1)).child("averageSpeed").getValue().toString();
                double b = Double.valueOf(av_speed);
                b = (double) Math.round(b * 1000) / 1000;
                av_speed_res.setText(Double.valueOf(b).toString()+" km/h");
                String total_distance = dataSnapshot.child(String.valueOf(intValue+1)).child("totalDistance").getValue().toString();
                double c = Double.valueOf(total_distance);
                c = (double) Math.round(c * 1000) / 1000;
                total_distance_res.setText(Double.valueOf(c).toString()+" km");
                String burned_cal = dataSnapshot.child(String.valueOf(intValue+1)).child("burnedCalories").getValue().toString();
                burned_cal_res.setText(burned_cal+" kcal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
