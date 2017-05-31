package com.binopt.imageuploader;

import android.app.Application;
import android.support.v4.app.AppLaunchChecker;

/**
 * Created by vladimir.masyuk on 5/31/2017.
 */

public class UploaderApplication extends Application {

    private static UploaderApplication instance;
    public UploaderApplication() {
        super();
        instance = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static UploaderApplication getInstance() {
        return instance;
    }
}
