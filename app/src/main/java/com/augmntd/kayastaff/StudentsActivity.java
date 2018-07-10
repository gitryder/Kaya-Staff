package com.augmntd.kayastaff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class StudentsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        mToolbar = findViewById(R.id.users_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUserList = findViewById(R.id.userList);

    }
}
