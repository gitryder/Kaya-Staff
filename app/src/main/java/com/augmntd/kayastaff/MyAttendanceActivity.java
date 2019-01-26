package com.augmntd.kayastaff;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MyAttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyAttendanceActivity";

    //ui components
    private Toolbar mToolbar;
    private TextView tvSubjectName, tvTeacherName;
    private EditText etOTP;
    private Button bSubmit;

    //vars
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);
        Log.d(TAG, "onCreate: started");

        mToolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvSubjectName = findViewById(R.id.tvSubjectName);
        tvTeacherName = findViewById(R.id.tvTeacherName);
        etOTP = findViewById(R.id.etOTP);
        bSubmit = findViewById(R.id.bSubmit);

        setupTextViewsWithData();

        bSubmit.setOnClickListener(this);
    }

    private void setupTextViewsWithData() {
        Log.d(TAG, "setupTextViewsWithData: setting up data");

        String subject_name = getIntent().getExtras().get("subject_name").toString();
        String teacher_name = getIntent().getExtras().get("teacher_name").toString();

        tvSubjectName.setText(subject_name);
        tvTeacherName.setText(teacher_name);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: submit button clicked");


    }

    public void validateOTP() {
        String OTP = etOTP.getText().toString();


    }

    public void sendDataToFirebase(){
        Log.d(TAG, "sendDataToFirebase: sending data to firebase");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String uid =  mAuth.getCurrentUser().getUid();
        String subject_name = tvSubjectName.getText().toString();
        String time = getTimeorDate(0);
        String date = getTimeorDate(1);

        HashMap<String, String> mSubjectData = new HashMap<>();
        mSubjectData.put("subject", subject_name);
        mSubjectData.put("time", time);

        Log.d(TAG, "subject: " + subject_name);
        Log.d(TAG, "time: " + time);
        Log.d(TAG, "date: " + date);



        if (mDatabase != null) {
            mDatabase.child("Attendance").child(date).child(uid).setValue(mSubjectData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Successfully marked", Toast.LENGTH_LONG)
                                        .show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException()
                                        .getLocalizedMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
        }
    }

    private String getTimeorDate(int i){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");

        String formattedTime = tf.format(c.getTime());
        String formattedDate = df.format(c.getTime());

        if (i == 0){return formattedTime;}
        else if (i == 1){return formattedDate;}

        return null;
    }
}
