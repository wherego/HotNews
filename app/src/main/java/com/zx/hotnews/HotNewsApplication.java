package com.zx.hotnews;

import android.app.Application;

import com.zx.hotnews.http.HttpUtils;

public class HotNewsApplication extends Application {
    private static HotNewsApplication hotNewsApplication;

    public static HotNewsApplication getInstance() {
        return hotNewsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        hotNewsApplication = this;
        //这里并不打算一开始就初始化网络加载模块,因为那个类太大了,我们的原则是能延时到是什么就什么时候用;
        HttpUtils.getInstance().setContext(getApplicationContext());
    }
}
