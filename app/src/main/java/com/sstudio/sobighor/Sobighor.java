package com.sstudio.sobighor;

import com.firebase.client.Firebase;

/**
 * Created by Alan on 9/4/2017.
 */

public class Sobighor extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
