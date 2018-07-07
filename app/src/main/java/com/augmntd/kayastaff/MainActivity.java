package com.augmntd.kayastaff;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jcminarro.roundkornerlayout.RoundKornerFrameLayout;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Kaya Staff");

        mFab = (FloatingActionButton) findViewById(R.id.mFab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFab.setEnabled(false);
                Intent settingsIntent = new Intent(MainActivity.this,
                        AccountSettingsActivity.class);
                startActivity(settingsIntent);
                mFab.setEnabled(true);
            }
        });

    }



    @Override
    public void onStart() {
        mAuth = FirebaseAuth.getInstance();

        super.onStart();
        // Check if user is signed in.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Get the Email from the Firebase
            String emailDisplay = currentUser.getEmail();
            //Now display the received email
        }else {
            sendBack();
        }
    }

    private void sendBack() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

}
