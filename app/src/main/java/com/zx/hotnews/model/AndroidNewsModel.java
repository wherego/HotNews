package com.zx.hotnews.model;

import android.util.Log;

import com.zx.hotnews.HotNewsApplication;
import com.zx.hotnews.bean.news.AndroidNewsBean;
import com.zx.hotnews.http.HttpUtils;
import com.zx.hotnews.http.RequestImpl;
import com.zx.hotnews.utils.ACache;
import com.zx.hotnews.utils.Constants;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AndroidNewsModel {
    private int page = 1;      //当前页面
    private int per_page = 20;  //每页加载20张吧,不提供对外设置的接口了;
    private ACache mCache;

    public AndroidNewsModel() {
        mCache = ACache.get(HotNewsApplication.getInstance());
    }

    public void setData(String id, int page, int per_page) {
        this.page = page;
        this.per_page = per_page;
    }

    public void getNews(int page, final RequestImpl request) {
        Log.e("###", "执行getNews");
        HttpUtils.getInstance().getAndroidNewClient()
                .getAndroidNews(page, per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AndroidNewsBean>() {
                    @Override
                    public void onCompleted() {
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("###", e.getMessage());
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(AndroidNewsBean androidNewsBean) {
                        System.out.println("----------------");
                        System.out.println(androidNewsBean);
                        System.out.println("----------------");
                        //执行到这一步是在UI线程;
                        request.loadSuccess(androidNewsBean);
                    }
                });
    }

    /***
     * 从缓存中获取新闻,这是在没有网络的情况下执行的,其实也很无奈,毕竟后台不是咱自己写的,所以只能用这种逻辑来处理了;
     *
     * @param name
     */
    public void getNewsFromCache(String name, final RequestImpl request) {
        Log.e("###", "执行getNewsFromCache");
        HttpUtils.getInstance().getAndroidNewClient()
                .getAndroidNews(page, per_page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AndroidNewsBean>() {
                    @Override
                    public void onCompleted() {
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("###", e.getMessage());
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(AndroidNewsBean androidNewsBean) {
                        System.out.println("----------------");
                        System.out.println(androidNewsBean);
                        System.out.println("----------------");
                        mCache.put(Constants.NEWS_ALL,androidNewsBean);
                        //执行到这一步是在UI线程;
                        request.loadSuccess(androidNewsBean);
                    }
                });


        AndroidNewsBean bean = (AndroidNewsBean) mCache.getAsObject(Constants.NEWS_ALL);
        if (bean != null) {
            request.loadSuccess(bean);
            request.loadComplete();
        } else {
            request.loadFailed();
        }
    }
}
