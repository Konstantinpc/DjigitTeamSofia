package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class S1 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout, new_trip;
    private EditText tripStart, tripDestination;
    private long id_trip = 0;
    private DatabaseReference reff, rf;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();

        tripStart=(EditText) findViewById(R.id.startPlain);
        tripDestination=(EditText) findViewById(R.id.destinationPlain);
        trip=new Trip();


        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Trips");
        rf = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id_trip = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        new_trip = (Button)findViewById(R.id.btnNewTrip);
        new_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String idt = Long.toString(id_trip);
                final Loading loading =new Loading(S1.this);
                trip.setStart(tripStart.getText().toString());
                trip.setDestination(tripDestination.getText().toString());
                trip.setId(id_trip+1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                trip.setStart_date(currentDateandTime);
                trip.setStartDate(new Date());

                if (trip.getStart().isEmpty() || trip.getDestination().isEmpty()) {
                    Toast.makeText(S1.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {


                    loading.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismissDialog();
                        }
                    }, 50000);
                    reff.child(String.valueOf(id_trip+1)).setValue(trip);
                    rf.child("over").setValue(id_trip+1);
                    Toast.makeText(S1.this, "Trip Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(S1.this, SecondActivityEnd.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("s_is_over",id_trip+1);
                    startActivity(intent);

                }

            }
        });



    }
}