package com.augmntd.kayastaff;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Kaya Staff");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout,
                R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true; 
        }



        return super.onOptionsItemSelected(item);
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
