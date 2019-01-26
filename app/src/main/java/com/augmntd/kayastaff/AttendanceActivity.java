package com.augmntd.kayastaff;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AttendanceActivity";

    //ui components
    private android.support.v7.widget.Toolbar mToolbar;
    private Button bSubmit;
    private TextInputLayout oneTimePassLayout;

    //vars
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Log.d(TAG, "onCreate: started");

        mToolbar = findViewById(R.id.attendance_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bSubmit = findViewById(R.id.bSubmit);
        oneTimePassLayout = findViewById(R.id.OneTimePassLayout);
    
        getUsernameFromFirebase();
    }

    private void getUsernameFromFirebase() {
        Log.d(TAG, "getUsernameFromFirebase: retrieving userid and name");
        String uid;

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid(); 
            String name = mAuth.getCurrentUser().getDisplayName();

            Log.d(TAG, "uid: " + uid);
            Log.d(TAG, "name: " + name);
        }
    }

    private void sendOTPToFirebase(String OTP, String username) {
        Log.d(TAG, "sendOTPToFirebase: sending");
        
        

    }
}
