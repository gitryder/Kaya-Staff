package com.augmntd.kayastaff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mStudentList;

    private DatabaseReference mStudentDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        mToolbar = findViewById(R.id.students_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Students");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mStudentList = findViewById(R.id.studentList);
        mStudentList.setHasFixedSize(true);
        mStudentList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Students, StudentsViewHolder>  firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Students, StudentsViewHolder>(
                Students.class,
                R.layout.user_list_layout,
                StudentsViewHolder.class,
                mStudentDatabase

        ) {
            @Override
            protected void populateViewHolder(StudentsViewHolder viewHolder, Students model, int position) {

                StudentsViewHolder.setName(model.getName());



            }
        };

        mStudentList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class StudentsViewHolder extends RecyclerView.ViewHolder{
        static View mView;

        public StudentsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public static void setName(String name){
            TextView studentNameView = mView.findViewById(R.id.user_list_name);
            studentNameView.setText(name);

        }


    }
}
