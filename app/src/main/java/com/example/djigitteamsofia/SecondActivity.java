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

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout, new_trip;
    private EditText tripStart, tripDestination;
    private long id_trip = 0;
    private DatabaseReference reff, rf;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


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
                final Loading loading =new Loading(SecondActivity.this);
                trip.setStart(tripStart.getText().toString());
                trip.setDestination(tripDestination.getText().toString());
                trip.setId(id_trip+1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                trip.setStart_date(currentDateandTime);
                trip.setStartDate(new Date());

                if (trip.getStart().isEmpty() || trip.getDestination().isEmpty()) {
                    //Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {

                    reff.child(String.valueOf(id_trip+1)).setValue(trip);
                    rf.child("over").setValue(id_trip+1);
                    loading.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismissDialog();
                        }
                    }, 5000);
                    Toast.makeText(SecondActivity.this, "Trip Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SecondActivity.this, SecondActivityEnd.class);
                    intent.putExtra("s_is_over",id_trip+1);
                    startActivity(intent);

                }

            }
        });
        logout = (Button)findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Hoe Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.leaderboard:
                        startActivity(new Intent(getApplicationContext(), LeaderBoard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.trips:
                        startActivity(new Intent(getApplicationContext(), Trips.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }
}