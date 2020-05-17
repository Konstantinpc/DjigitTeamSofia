package com.example.djigitteamsofia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public interface FirebaseCallBack{
        void onCallback(Long i);
    }

    private EditText Email;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reff;
    private DatabaseReference rf;
    private ProgressDialog progressDialog;
    private long i=0;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        userRegistration = (TextView)findViewById(R.id.tvRegister);

        Info.setText("Number of attempts remaining: 5");
        final Loading loading =new Loading(MainActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null) {
            //finish();
            //loading.startLoadingDialog();
            loading.startLoadingDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading.dismissDialog();
                }
            }, 5000);
            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("over");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue(Long.class) > 0) {
                        //Log.i(TAG, "Value = " + dataSnapshot.child(String.valueOf(i)).child("is_over").getValue() +" pishka");
                        //Log.i(TAG, "Value = " + dataSnapshot.child(String.valueOf(i)).child("is_over").getValue() +" pishka"+id_t);
                        i=dataSnapshot.getValue(Long.class);

                        Intent intent = new Intent(MainActivity.this, SecondActivityEnd.class);
                        intent.putExtra("m_is_over", i);
                        startActivity(intent);
                        //.dismissDialog();


                    } else {


                        //loading.dismissDialog();

                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void validate(String userName, String userPassword){
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    if(getIntent().hasExtra("is_over")){
                        Bundle exBundle= getIntent().getExtras();
                        long is_over= exBundle.getLong("is_over");
                        Intent intent = new Intent(MainActivity.this, SecondActivityEnd.class);
                        intent.putExtra("m_is_over",is_over);
                        startActivity(intent);

                    }else {
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("Number of attempts remaining: " + counter);
                    if(counter == 0) {
                        Login.setEnabled(false);
                    }
                }
            }
        });
    }

}