package com.example.djigitteamsofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase myDataBase;
    private DatabaseReference reff;
    private String name, password, email;
    private long maxid = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        final Loading loading =new Loading(RegistrationActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        myDataBase = FirebaseDatabase.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reff.child(String.valueOf(maxid));
                if(validate()) {
                    final SignUpData singUpRecord = new SignUpData(name, email, password);
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                loading.startLoadingDialog();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.dismissDialog();
                                    }
                                }, 50000);
                                myDataBase.getReference().child("Users").child(firebaseAuth.getUid()).setValue(singUpRecord);
                                myDataBase.getReference().child("Users").child(firebaseAuth.getUid()).child("Followers").setValue(0);

                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, SecondActivity.class));
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });
    }

    private void setupUIViews(){
        userName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
    }

    private Boolean validate() {
        Boolean result = false;

        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();


        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }


        return result;
    }
}
