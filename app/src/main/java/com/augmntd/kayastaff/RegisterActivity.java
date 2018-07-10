package com.augmntd.kayastaff;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private android.support.v7.widget.Toolbar mToolbar;

    //Layouts Declaration
    private TextInputLayout etName, etEmail, etPassword;
    private Button bRegister;

    //Progress Dialog
    private ProgressDialog mRegProgress;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        //Layouts Initialization
        etName = (TextInputLayout) findViewById(R.id.etNameLayout);
        etEmail = (TextInputLayout) findViewById(R.id.etEmailLayout);
        etPassword = (TextInputLayout) findViewById(R.id.etPasswordLayout);
        bRegister = (Button) findViewById(R.id.bRegister);

        //Define the Typeface
        Typeface Helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.otf");
        Typeface Avenir = Typeface.createFromAsset(getAssets(), "fonts/Avenir.ttf");
        //Set it to the respective TextView
        /*
        etEmail.setTypeface(Helvetica);
        etPassword.setTypeface(Helvetica);
        etName.setTypeface(Helvetica);
        */
        bRegister.setTypeface(Helvetica);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getEditText().getText().toString();
                String email = etEmail.getEditText().getText().toString();
                String password = etPassword.getEditText().getText().toString();

                //Check if any field is null
                if (email.matches("") && password.matches("")
                        && name.matches("")) {
                    Toast.makeText(getApplicationContext(), "Are you Crazy? Type Something",
                            Toast.LENGTH_SHORT).show();
                } else if (password.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "Who can live without a Password", Toast.LENGTH_SHORT).show();
                } else if (email.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "No Man has succeeded without an Email", Toast.LENGTH_SHORT).show();
                }else if (name.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "How do you not have a name", Toast.LENGTH_SHORT).show();
                }else {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we register you");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    registerUser(name, email, password);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void registerUser(final String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String showException = "Sign Up Failed\n" + task.getException();

                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("image", "default");
                            userMap.put("thumbnail", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgress.dismiss();
                                        mAuth.signOut();
                                        Intent mainIntent = new Intent(RegisterActivity.this,
                                                LoginActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }else {
                                        Toast.makeText(RegisterActivity.this,
                                                task.getException().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                        }else {
                            mRegProgress.hide();
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User already exists. Try Signing In",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, showException, Toast.LENGTH_LONG).show();
                            }

                        }


                    }
                });
      }
    }

