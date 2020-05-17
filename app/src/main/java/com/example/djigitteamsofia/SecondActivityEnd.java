package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SecondActivityEnd extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private long id_trip = 0;
    private DatabaseReference reff, rf;
    private Trip trip;
    private Button end_trip, logout;
    private long for_main=0;
    private TextView start, destination, max_speed, av_speed, total_distance, burned_calories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_end);

        if(!getIntent().hasExtra("m_is_over")){
            Bundle exBundle = getIntent().getExtras();
            long intValue = exBundle.getLong("s_is_over");
            for_main = intValue;
        }else {
            Bundle exBundle = getIntent().getExtras();
            long intValue = exBundle.getLong("m_is_over");
            for_main = intValue;
        }

        start=(TextView) findViewById(R.id.textView21);
        destination=(TextView) findViewById(R.id.textView23);
        max_speed=(TextView) findViewById(R.id.textView46);
        av_speed=(TextView) findViewById(R.id.textView40);
        total_distance=(TextView) findViewById(R.id.textView45);
        burned_calories=(TextView) findViewById(R.id.textView47);


        firebaseAuth = FirebaseAuth.getInstance();
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Trips");
        rf = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String st = dataSnapshot.child(String.valueOf(for_main)).child("start").getValue().toString();
                start.setText(st);
                String dest = dataSnapshot.child(String.valueOf(for_main)).child("destination").getValue().toString();
                destination.setText(dest);
                String max = dataSnapshot.child(String.valueOf(for_main)).child("maxSpeed").getValue().toString();
                double a = Double.valueOf(max);
                a = (double) Math.round(a * 1000) / 1000;
                max_speed.setText(Double.valueOf(a).toString()+" km");
                String av = dataSnapshot.child(String.valueOf(for_main)).child("averageSpeed").getValue().toString();
                double b = Double.valueOf(av);
                b = (double) Math.round(b * 1000) / 1000;
                av_speed.setText(Double.valueOf(b).toString()+" km");
                String dist = dataSnapshot.child(String.valueOf(for_main)).child("totalDistance").getValue().toString();
                double c = Double.valueOf(dist);
                c = (double) Math.round(c * 1000) / 1000;
                total_distance.setText(Double.valueOf(c).toString()+" km");
                String cal = dataSnapshot.child(String.valueOf(for_main)).child("burnedCalories").getValue().toString();
                burned_calories.setText(cal +" kcal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Exception
            }
        });
        end_trip=(Button)findViewById(R.id.btnEnd);
        end_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setDestination_date(Calendar.getInstance().getTime());
                reff.child(String.valueOf(for_main)).child("is_over").setValue(1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                reff.child(String.valueOf(for_main)).child("destination_date").setValue(currentDateandTime);
                reff.child(String.valueOf(for_main)).child("destinationDate").setValue(new Date());
                rf.child("over").setValue(0);
                startActivity(new Intent(SecondActivityEnd.this, SecondActivity.class));
            }
        });
        logout = (Button)findViewById(R.id.btnLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Intent intent = new Intent(SecondActivityEnd.this, MainActivity.class);
                intent.putExtra("is_over",for_main);
                startActivity(intent);

            }
        });

    }
}
