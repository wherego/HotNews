package com.zx.hotnews.base;

/**
 * 基础接口Presenter类型,这里只有一个start()方法
 * 一般的使用场景是进入界面之后从网络或者数据库加载数据用.
 */

public interface BasePresenter {
    void start();
}
