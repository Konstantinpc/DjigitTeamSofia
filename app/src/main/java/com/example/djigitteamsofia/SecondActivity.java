package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logout, new_trip, start_trip;
    private EditText tripStart, tripDestination;
    private ListView newsfeed;
    private long id_trip = 0;
    private DatabaseReference reff, rf;
    private Trip trip;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> following = new ArrayList<>();
    Hashtable<String, String> id_following = new Hashtable<>();
    Hashtable<String, String> id_t = new Hashtable<>();
    Hashtable<String, String> id_text = new Hashtable<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        start_trip=(Button) findViewById(R.id.stbutton);
        start_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, S1.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        newsfeed=(ListView) findViewById(R.id.newsfeed);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        newsfeed.setAdapter(arrayAdapter);
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Following");
        reff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    String name = dataSnapshot.getKey();
                    String id = dataSnapshot.getValue().toString();
                    rf = FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("SharedTrips");
                    rf.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if (dataSnapshot.exists()) {
                                //id_following.put()
                                String wow=dataSnapshot.child("Name_of_following").getValue().toString()+"\n\n"+
                                        dataSnapshot.child("TripName").getValue().toString()+
                                        "\n\nDate: "+dataSnapshot.child("shared_date").getValue().toString()+"\n";
                                id_following.put(wow, dataSnapshot.getRef().getParent().getParent().getKey());
                                id_t.put(wow, dataSnapshot.child("TripId").getValue().toString());
                                id_text.put(wow, dataSnapshot.child("Text").getValue().toString());
                                arrayAdapter.add(wow);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //following.add(title_t);
                    //id_following.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                    //arrayAdapter.add(dataSnapshot.getValue().toString());
                    //arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        newsfeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SecondActivity.this, id_following.get(arrayAdapter.getItem(position)) , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SecondActivity.this, NewsfeedDetails.class);
                intent.putExtra("id", id_following.get(arrayAdapter.getItem(position)));
                intent.putExtra("id_trip", id_t.get(arrayAdapter.getItem(position)));
                intent.putExtra("text", id_text.get(arrayAdapter.getItem(position)));
                intent.putExtra("details", arrayAdapter.getItem(position));
                startActivity(intent);
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
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }
}