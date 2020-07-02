package com.xiao.eblog;
import androidx.multidex.MultiDex;
import android.app.Application;

public class EBlogApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
