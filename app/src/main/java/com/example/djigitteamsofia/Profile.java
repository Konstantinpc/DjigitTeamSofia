package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private TextView followers, following, name;
    private Button logout;
    private DatabaseReference reff;
    private FirebaseAuth firebaseAuth;
    private long id_followers=0;
    //private final String TAG="Na maika ti putkata = ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        followers=(TextView) findViewById(R.id.followersNum);
        following=(TextView) findViewById(R.id.followingNum);
        name=(TextView) findViewById(R.id.name);

        firebaseAuth = FirebaseAuth.getInstance();
        reff= FirebaseDatabase.getInstance().getReference().child("Users");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child(firebaseAuth.getUid()).child("userName").getValue().toString();
                name.setText(n);
                if(dataSnapshot.child(firebaseAuth.getUid()).child("Following").exists()){
                    id_followers = (dataSnapshot.child(firebaseAuth.getUid()).child("Following").getChildrenCount());
                }
                String f=dataSnapshot.child(firebaseAuth.getUid()).child("Followers").getValue().toString();
                following.setText(String.valueOf(id_followers));
                followers.setText(f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout = (Button)findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Hoe Selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.trips:
                startActivity(new Intent(getApplicationContext(), Trips.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.statistic:
                startActivity(new Intent(getApplicationContext(), History.class));
                overridePendingTransition(0,0);
                return true;
            case R.id.statistic_by_dell:
                startActivity(new Intent(getApplicationContext(), Statistics.class));
                overridePendingTransition(0,0);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}