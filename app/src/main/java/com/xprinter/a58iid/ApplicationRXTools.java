package com.xprinter.a58iid;

import android.app.Application;

import com.vondear.rxtools.RxTool;

/**
 * Created by kylin on 2017/10/21.
 */

public class ApplicationRXTools extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);

    }
}
