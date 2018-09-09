package com.augmntd.kayastaff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jcminarro.roundkornerlayout.RoundKornerFrameLayout;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mAuthUser;
    private StorageReference mStorage;

    private android.support.v7.widget.Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private  NavigationView mNavView;

    private TextView mNavHeaderName;
    private CircleImageView mNavImageView;
    private TextView mNavHeaderEmail;

    private static final int GALLERY_PICK = 1;

    //Progress Dialog
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");


        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout,
                R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //_____NAVIGATION DRAWER DRAMA STARTS HERE_______

        mNavView = (NavigationView) findViewById(R.id.mNavView);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_logout){
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    sendBack();
                }/*if(item.getItemId() == R.id.nav_attendance){
                    Intent mainIntent = new Intent(MainActivity.this, AttendanceActivity.class);
                    startActivity(mainIntent);
                }*/if(item.getItemId() == R.id.nav_students) {
                    Intent otherIntent = new Intent(MainActivity.this, StudentsActivity.class);
                    startActivity(otherIntent);
                }


                return true;
            }
        });

        View header = mNavView.getHeaderView(0);
        mNavHeaderName = (TextView) header.findViewById(R.id.nav_header_name);
        mNavImageView = (CircleImageView) header.findViewById(R.id.nav_header_photo);
        mNavHeaderEmail = (TextView) header.findViewById(R.id.nav_header_email);

        //Firebase
//        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        mAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mAuthUser != null){
            String uid = mAuthUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(uid);
        } else {
            Log.e("Null Pointer", "mAuthUser is null");
        }

        if(mDatabase != null){
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String email = mAuth.getCurrentUser().getEmail();

                    mNavHeaderName.setText(name);
                    mNavHeaderEmail.setText(email);
                    if(image.equals("default")){
                        mNavImageView.setImageResource(R.drawable.avatar_circle_blue_512dp);
                    }else {
                        Picasso.get().load(image).into(mNavImageView);

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Log.e("Null Pointer", "mDatabase is null");
        }

        mNavImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });


        //______NAVIGATION DRAWER DRAMA ENDS HERE______

        //FLOATING ACTION BUTTON INTENT

        mFab = findViewById(R.id.mFab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFab.setEnabled(false);
                Intent settingsIntent = new Intent(MainActivity.this,
                        AttendanceActivity.class);
                startActivity(settingsIntent);
                mFab.setEnabled(true);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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
