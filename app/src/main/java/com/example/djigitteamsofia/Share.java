package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Share extends AppCompatActivity {

    private ListView stats;
    private EditText text_for_followers;
    private Button share_to_followers;
    private DatabaseReference reff, rf;
    private FirebaseAuth firebaseAuth;
    private long num_shared_trips=0, idSharedTrips=0;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private String start, destination;
    private TextView title, start_res, start_date_res, destination_res,
            destination_date_res, max_speed_res, av_speed_res, total_distance_res,
            burned_cal_res;
    private String name_for_newsfeed = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        text_for_followers=(EditText) findViewById(R.id.shared_text);
        share_to_followers=(Button) findViewById(R.id.buttonshare);
        title=(TextView) findViewById(R.id.textView);



        final Loading loading =new Loading(Share.this);

        firebaseAuth = FirebaseAuth.getInstance();


        if(getIntent().hasExtra("index")){
            Bundle exBundle = getIntent().getExtras();
            idSharedTrips = exBundle.getInt("index");

        }

        rf = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid());


        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("SharedTrips").exists()){
                    num_shared_trips=dataSnapshot.child("SharedTrips").getChildrenCount();

                }
                name_for_newsfeed=dataSnapshot.child("userName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        stats=(ListView) findViewById(R.id.stats_view);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        stats.setAdapter(arrayAdapter);
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("Trips").child(String.valueOf(idSharedTrips+1));
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*String value = dataSnapshot.getValue().toString();
                arrayList.add(value);*/
                String title_t = dataSnapshot.child("id").getValue().toString();
                arrayList.add("Id: "+title_t+".");
                start = dataSnapshot.child("start").getValue().toString();
                arrayList.add("Start: "+start);
                String start_date = dataSnapshot.child("start_date").getValue().toString();
                arrayList.add("Start date: "+start_date);
                destination = dataSnapshot.child("destination").getValue().toString();
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



        stats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(Share.this, "Stats", Toast.LENGTH_SHORT).show();



            }
        });

        share_to_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rf.child("SharedTrips").child(String.valueOf(num_shared_trips+1)).child("TripName").setValue("Trip: "+start+" to "+destination);
                rf.child("SharedTrips").child(String.valueOf(num_shared_trips+1)).child("Name_of_following").setValue(name_for_newsfeed);
                rf.child("SharedTrips").child(String.valueOf(num_shared_trips+1)).child("Text").setValue(text_for_followers.getText().toString());
                rf.child("SharedTrips").child(String.valueOf(num_shared_trips+1)).child("TripId").setValue(String.valueOf(idSharedTrips+1));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                rf.child("SharedTrips").child(String.valueOf(num_shared_trips+1)).child("shared_date").setValue(currentDateandTime);
                loading.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismissDialog();
                    }
                }, 50000);
                Intent intent = new Intent(Share.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                //intent.putExtra("index", intValue);
                startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });

    }
}
