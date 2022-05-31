package com.rw.colormemory;

import android.app.Application;

import com.rw.com.rw.database.DatabaseHelper;

/**
 * Created by Rakhita on 2/18/2018.
 */

public class InitApp extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
