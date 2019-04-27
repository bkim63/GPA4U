package com.bumjinkim.GPA4U;

import android.app.Application;

import io.realm.Realm;

public class KimApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
