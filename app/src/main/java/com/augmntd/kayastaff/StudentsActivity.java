package com.augmntd.kayastaff;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

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
                StudentsViewHolder.setUserImage(model.getImage(), getApplicationContext());
                StudentsViewHolder.setRollId(model.getRoll_no());

                final String user_id = getRef(position).getKey();

                StudentsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profile_intent = new Intent(StudentsActivity.this, ProfileActivity.class);
                        profile_intent.putExtra("user_id", user_id);
                        startActivity(profile_intent);

                    }
                });

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

        public static void setRollId(String roll_no){
            TextView user_list_rollid = mView.findViewById(R.id.user_list_rollid);
            user_list_rollid.setText(roll_no);

        }

        public static void setUserImage(String thumb_image, Context ctx){
            CircleImageView userImageView = mView.findViewById(R.id.user_list_imageview);
            Picasso.get().load(thumb_image).placeholder(R.drawable.avatar_default).into(userImageView);
        }



    }
}
