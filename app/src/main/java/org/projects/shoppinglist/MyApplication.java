package org.projects.shoppinglist;

import android.app.Application;
import android.util.Log;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by user on 20-04-2017.
 */

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Log.d("firebase","persistance enabled");
        }
        else
            Log.d("firebase","persistance not enabled");
    }



}
