package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsfeedDetails extends AppCompatActivity {

    private TextView details, text_share;
    private ListView stats;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reff;
    private String idValue, det, trip, text;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("id")){
            Bundle exBundle = getIntent().getExtras();
            idValue = exBundle.getString("id");

        }
        if(getIntent().hasExtra("details")){
            Bundle exBundle = getIntent().getExtras();
            det = exBundle.getString("details");
        }
        if(getIntent().hasExtra("id_trip")){
            Bundle exBundle = getIntent().getExtras();
            trip = exBundle.getString("id_trip");
        }
        if(getIntent().hasExtra("text")){
            Bundle exBundle = getIntent().getExtras();
            text = exBundle.getString("text");
        }

        details=(TextView) findViewById(R.id.textView421);
        text_share=(TextView) findViewById(R.id.textView422);
        details.setText(det);
        text_share.setText(text);
        //reff= FirebaseDatabase.getInstance().getReference().child("Users").child(idValue).child("Trips").child(Long);
        stats=(ListView) findViewById(R.id.statsview);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        stats.setAdapter(arrayAdapter);
        reff= FirebaseDatabase.getInstance().getReference().child("Users").child(idValue).child("Trips").child(trip);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*String value = dataSnapshot.getValue().toString();
                arrayList.add(value);*/
                String start = dataSnapshot.child("start").getValue().toString();
                arrayList.add("Start: "+start);
                String start_date = dataSnapshot.child("start_date").getValue().toString();
                arrayList.add("Start date: "+start_date);
                String destination = dataSnapshot.child("destination").getValue().toString();
                arrayList.add("Destination: "+destination);
                String destination_date = dataSnapshot.child("destination_date").getValue().toString();
                arrayList.add("Destination date: "+destination_date);
                String curr_speed = dataSnapshot.child("maxSpeed").getValue().toString();
                double a = Double.valueOf(curr_speed);
                a = (double) Math.round(a * 1000) / 1000;
                arrayList.add("Max speed: "+Double.valueOf(a).toString()+" km/h");
                String av_speed = dataSnapshot.child("averageSpeed").getValue().toString();
                double b = Double.valueOf(av_speed);
                b = (double) Math.round(b * 1000) / 1000;
                arrayList.add("Average speed: "+Double.valueOf(b).toString()+" km/h");
                String total_distance = dataSnapshot.child("totalDistance").getValue().toString();
                double c = Double.valueOf(total_distance);
                c = (double) Math.round(c * 1000) / 1000;
                arrayList.add("Total distance: "+Double.valueOf(c).toString()+" km");
                String burned_cal = dataSnapshot.child("burnedCalories").getValue().toString();
                arrayList.add("Burned calories: "+burned_cal+" kcal");
                arrayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
