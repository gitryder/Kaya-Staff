package com.augmntd.kayastaff;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button bRegister, bLogin;
    private android.support.v7.widget.Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bRegister = (Button) findViewById(R.id.bRegister);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.start_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        WifiManager wifiman = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiman.isWifiEnabled()){
            WifiInfo wifiInfo = wifiman.getConnectionInfo();
            if(wifiInfo != null){
                String name = ("Connected to Wifi Network:\t" + wifiInfo.getSSID().toString());
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                if(name.contains("h4ze") || name.contains("Vidyalankar Campus")){

                } else {
                    String pleaseDude = "Please connect to Vidyalankar Campus";
                    //bLogin.setEnabled(false);
                    bRegister.setEnabled(false);
                    Toast.makeText(getApplicationContext(), pleaseDude,
                            Toast.LENGTH_LONG).show();
                }
            }

        }else {
            Toast.makeText(getApplicationContext(), "Please enable Wi-fi", Toast.LENGTH_LONG).show();
        }



        //Define the Typeface
        Typeface AvenirDemi = Typeface.createFromAsset(getAssets(), "fonts/Avenir_Demi.ttf");
        Typeface Helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.otf");
        Typeface Avenir = Typeface.createFromAsset(getAssets(), "fonts/Avenir.ttf");
        //Set it to the respective TextView
        tvWelcome.setTypeface(AvenirDemi);
        bRegister.setTypeface(Helvetica);
        bLogin.setTypeface(Helvetica);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(registerIntent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
    }
}
