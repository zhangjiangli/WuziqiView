package com.gdcp.wuziqiview;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by asus- on 2017/6/17.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"7c6787e1cf575fc20765bed4d8f2cc96");
    }
}
