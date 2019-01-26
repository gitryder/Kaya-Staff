package com.augmntd.kayastaff;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class Kaya extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

