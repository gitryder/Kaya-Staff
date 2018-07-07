package com.augmntd.kayastaff;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    //Firebase Auth
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;

    //Declaration of Fields
    private TextInputEditText etLoginEmail, etLoginPassword;
    private Button bLoginButton;
    private TextView tvForgotPass;

    //Progress Dialog
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Progress Dialog
        mLoginProgress = new ProgressDialog(this);

        //Initialization
        etLoginEmail = (TextInputEditText) findViewById(R.id.etPassEmail);
        etLoginPassword = (TextInputEditText) findViewById(R.id.etLoginPassword);
        bLoginButton = (Button) findViewById(R.id.bLoginButton);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);

        tvForgotPass.setPaintFlags(tvForgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Define the Typeface
        Typeface Helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.otf");
        Typeface Avenir = Typeface.createFromAsset(getAssets(), "fonts/Avenir.ttf");
        //Set it to the respective TextView
        /*
        etLoginEmail.setTypeface(Helvetica);
        etLoginPassword.setTypeface(Helvetica);
        */
        bLoginButton.setTypeface(Helvetica);


        //Login Button Functionality
        bLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                //Check if any field is null
                if(email.matches("") && password.matches("")){
                    Toast.makeText(getApplicationContext(), "Don't be Oversmart." +
                            "Type your Email and Password", Toast.LENGTH_SHORT).show();
                }else if(password.matches("")){
                    Toast.makeText(getApplicationContext(),
                            "Who has ever logged in without a Password", Toast.LENGTH_SHORT).show();
                }else if(email.matches("")){
                    Toast.makeText(getApplicationContext(),
                            "No Man has succeeded without an Email", Toast.LENGTH_SHORT).show();
                }else {
                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email, password);
                }
            }
        });

        //Forgot Password functionality
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToPasswordActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    private void switchToPasswordActivity(){
        Intent changeActivity = new Intent(LoginActivity.this,
                ResetPasswordActivity.class);
        startActivity(changeActivity);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String showException = "Login Failed\n" + task.getException();
                        //If Login is Successful go to the Main Activity
                        if (task.isSuccessful()) {
                            mLoginProgress.dismiss();
                            Intent mainIntent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }else {
                            mLoginProgress.hide();
                            //Check if E-mail or Pass is wrong, if yes...
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(), "Incorrect Email or Password",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                //if not, Check if User Exists, if yes...
                                if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(getApplicationContext(), "You are not Registered",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    //if not, Display what's the Problem, Yo?
                                    Toast.makeText(LoginActivity.this, showException, Toast.LENGTH_LONG).show();
                                }
                            }


                        }
                    }
                });
    }
}
