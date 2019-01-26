package com.augmntd.kayastaff;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AttendanceAdapter.OnSubjectClickListener {

    private static final String TAG = "MainActivity";

    //ui components
    private android.support.v7.widget.Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavView;
    private TextView mNavHeaderName, mNavHeaderEmail, tvDate;
    private CircleImageView mNavImageView;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;

    //vars
    String userEmail, uid;
    private static final int GALLERY_PICK = 1;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> mSubjectNames = new ArrayList<>();
    private ArrayList<String> mTeacherNames = new ArrayList<>();
    private ArrayList<String> mClassroomNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");


        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.open, R.string.close);
        mNavView = findViewById(R.id.mNavView);
        View nav_header = mNavView.getHeaderView(0);
        NavigationView navigationView = findViewById(R.id.mNavView);
        mNavHeaderName = nav_header.findViewById(R.id.nav_header_name);
        mNavImageView = nav_header.findViewById(R.id.nav_header_photo);
        mNavHeaderEmail = nav_header.findViewById(R.id.nav_header_email);
        tvDate = findViewById(R.id.tvDate);

        mAuth = FirebaseAuth.getInstance();
        mFab = findViewById(R.id.mFab);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        setupData();

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        Menu nav_menu = navigationView.getMenu();
        if (mAuth.getCurrentUser() != null) {
            if (!userEmail.equals("teacher@vp.com")) {
                MenuItem  nav_students = nav_menu.findItem(R.id.nav_students).setVisible(false);
                mFab.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.GONE);
                MenuItem nav_attendance = nav_menu.findItem(R.id.nav_my_attendance).setVisible(false);
            }
        }

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        logoutUser();
                        break;

                    case R.id.nav_students:
                        Intent studentsActivity = new Intent(MainActivity.this,
                                StudentsActivity.class);
                        startActivity(studentsActivity);
                        break;

                    default:
                        break;
                }

                return true;
            }
        });

        mNavImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewOTPDialog(mNavHeaderEmail.getText().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void setupNavHeader() {
        Log.d(TAG, "setupNavHeader: setting up");

        if(mAuth.getCurrentUser() != null){
            uid = mAuth.getCurrentUser().getUid();
            String email = mAuth.getCurrentUser().getEmail();

            if (email.contains("@vp.com")) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Teachers")
                        .child(uid);
            } else {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(uid);
            }
            mDatabase.keepSynced(true);
        } else {
            Log.d(TAG, "mAuthUser is null");
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
                    Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                }});
        } else {
            Log.d(TAG, "setupNavHeader: mDatabase is null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");

        if(mAuth.getCurrentUser() == null){
            sendBackToStart();
        }
    }

    private void getUserEmail() {
        if (mAuth.getCurrentUser() != null) {
            userEmail = mAuth.getCurrentUser().getEmail();
        } else {
            sendBackToStart();
        }
    }

    private void logoutUser() {
        Log.d(TAG, "logoutUser: logging out user");

        FirebaseAuth.getInstance().signOut();
        finish();
        sendBackToStart();
    }

    private String getTimeorDate(int i){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a");

        String formattedTime = tf.format(c.getTime());
        String formattedDate = df.format(c.getTime());

        if (i == 0){return formattedTime;}
        else if (i == 1){return formattedDate;}

        return null;
    }

    private void sendBackToStart() {
        Log.d(TAG, "sending back to StartActivity");

        Intent startActivityIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startActivityIntent);
        finish();
    }

    private void showAddNewOTPDialog(final String teacher_name) {
        final android.app.AlertDialog.Builder alert = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialog_view = LayoutInflater.from(this).inflate(R.layout.dialog_mark_attendance, viewGroup, false);

        final TextView tvSubject = dialog_view.findViewById(R.id.tvSubject);
        final TextView tvTime = dialog_view.findViewById(R.id.tvTime);
        final EditText etSubject = dialog_view.findViewById(R.id.etSubject);
        final TextInputLayout etOTPLayout = dialog_view.findViewById(R.id.etOTPLayout);

        etSubject.setVisibility(View.VISIBLE);
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String subject = etSubject.getText().toString().toUpperCase();

                if (subject.equals("")) {
                    subject = "Subject";
                }
                tvSubject.setText(subject);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvTime.setText(getTimeorDate(0));

        alert.setView(dialog_view);
        if (alert != null) {
            alert.setTitle(null)
                    .setCancelable(true)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String OTP = etOTPLayout.getEditText().getText().toString();
                            String subject = etSubject.getText().toString();

                            if (OTP.isEmpty() || subject.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "fields cannot be empty",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("OTPs").child(teacher_name).child("otp")
                                        .setValue(OTP);
                            }
                        }
                    });

            alert.create();
            alert.show();
        }
    }

    private void showMarkAttendanceDialog(String subject) {
        final android.app.AlertDialog.Builder alert = new AlertDialog.Builder(this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialog_view = LayoutInflater.from(this).inflate(R.layout.dialog_mark_attendance, viewGroup, false);

        final TextView tvSubject = dialog_view.findViewById(R.id.tvSubject);
        final TextView tvTime = dialog_view.findViewById(R.id.tvTime);
        final TextInputLayout etOTPLayout = dialog_view.findViewById(R.id.etOTPLayout);

        tvSubject.setText(subject);
        tvTime.setText(getTimeorDate(0));

        alert.setView(dialog_view);
        if (alert != null) {
            alert.setTitle(null)
                    .setCancelable(true)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String OTP = etOTPLayout.getEditText().getText().toString();
                            if (!OTP.isEmpty()) {
                                Toast.makeText(getApplicationContext(), OTP,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Cannot be empty",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            alert.create();
            alert.show();
        }
    }

    private void setupDate() {
        Log.d(TAG, "setupDate: setting up");

        tvDate.setText(getTimeorDate(1));
    }

    private void setupData(){
        Log.d(TAG, "setupData: setting up data");

        mSubjectNames.add("STE");
        mTeacherNames.add("Rupali Sheth");
        mClassroomNames.add("LOO1C");

        mSubjectNames.add("OMD");
        mTeacherNames.add("Archana Gopnarayan");
        mClassroomNames.add("V002");

        mSubjectNames.add("MCO");
        mTeacherNames.add("Trupti Patel");
        mClassroomNames.add("V002");

        mSubjectNames.add("MAN");
        mTeacherNames.add("Kshama Wagh");
        mClassroomNames.add("V118");

        mSubjectNames.add("AJP");
        mTeacherNames.add("Samidha Shinde");
        mClassroomNames.add("L004B");

        getUserEmail();
        setupDate();
        setupNavHeader();
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        Log.d(TAG, "setupRecyclerView: setting up recyclerview");
        AttendanceAdapter adapter = new AttendanceAdapter(this, mSubjectNames, mTeacherNames, mClassroomNames, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSubjectClick(int position) {
        Log.d(TAG, "onSubjectClick: clicked");

        String subject = mSubjectNames.get(position);
        showMarkAttendanceDialog(subject);
    }
}
