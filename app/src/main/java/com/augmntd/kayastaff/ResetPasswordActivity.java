package com.augmntd.kayastaff;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    //Firebase Auth
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    //Declaring Fields
    private EditText etPassEmail;
    private Button bSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface Helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.otf");
        Typeface Avenir = Typeface.createFromAsset(getAssets(), "fonts/Avenir.ttf");

        //Initialization of Fields
        etPassEmail = (EditText) findViewById(R.id.etPassEmail);
        bSendEmail = (Button) findViewById(R.id.bSendEmail);

        //Set it to the respective TextView
        bSendEmail.setTypeface(Helvetica);


        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //When the Button is clicked do this...
        bSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the Email from the Email field and store it
                String userEmail = etPassEmail.getText().toString();
                final String successText = "Password Reset Link Sent";
                //'Sending Email' Method and Checking for Success
                mAuth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), successText, Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
            }
        });


    }

}

