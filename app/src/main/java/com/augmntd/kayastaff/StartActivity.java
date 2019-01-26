package com.augmntd.kayastaff;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    //ui components
    private TextView tvWelcome, tvWelcome2;
    private Button bRegister, bLogin;
    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bRegister = findViewById(R.id.bRegister);
        bLogin = findViewById(R.id.bLogin);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome2 = findViewById(R.id.tvWelcome2);

        //setting the status bar translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Typeface Autumn = Typeface.createFromAsset(getAssets(), "fonts/Autumn.ttf");
        Typeface Helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.otf");

        //Set it to the respective TextView
        tvWelcome.setTypeface(Autumn);
        tvWelcome2.setTypeface(Autumn);
        bRegister.setTypeface(Helvetica);
        bLogin.setTypeface(Helvetica);

        bRegister.setVisibility(View.VISIBLE);

        WifiManager wifiman = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiman.isWifiEnabled()){
            WifiInfo wifiInfo = wifiman.getConnectionInfo();
            if(wifiInfo != null){
                String name = ("Connected to Wifi Network:\t" + wifiInfo.getSSID().toString());
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                if(!name.contains("Vidyalankar Campus")){
                    //showNotConnectedDialog();
                }
            }

        }else {
            showNotConnectedDialog();
        }

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

    private void showNotConnectedDialog() {
        final android.app.AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (alert != null) {
            alert.setTitle("Vidyalankar Campus")
                    .setMessage("You cannot use the app unless you are connected to the Vidyalankar Network"
                            + "\n\n" + "Please close the app, connect and reopen the app")
                    .setCancelable(false)
                    .setPositiveButton("Close app", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            System.exit(1);
                        }
                    });

            alert.create();
            alert.show();
        }
    }
}
