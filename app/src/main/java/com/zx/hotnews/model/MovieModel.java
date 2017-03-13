package com.zx.hotnews.model;

import com.zx.hotnews.bean.movie.HotMovieBean;
import com.zx.hotnews.bean.picture.PictureBean;
import com.zx.hotnews.http.HttpUtils;
import com.zx.hotnews.http.RequestImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MovieModel {
    public void getMovie(final RequestImpl request) {
        HttpUtils.getInstance().getMovieClient().getHotMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotMovieBean>() {
                    @Override
                    public void onCompleted() {
                        //可以在这里通知加载完成,暂时用不到,如果使用的第三方的RecyclerView的话,可能会有一个刷新完成的通知;
                        request.loadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        request.loadFailed();
                    }

                    @Override
                    public void onNext(HotMovieBean hotMovieBean) {
                        //执行到这一步是在UI线程;
                        request.loadSuccess(hotMovieBean);
                    }
                });
    }
}
