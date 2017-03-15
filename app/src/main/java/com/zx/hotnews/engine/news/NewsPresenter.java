package com.zx.hotnews.engine.news;

import com.zx.hotnews.HotNewsApplication;
import com.zx.hotnews.bean.movie.HotMovieBean;
import com.zx.hotnews.bean.news.AndroidNewsBean;
import com.zx.hotnews.http.RequestImpl;
import com.zx.hotnews.model.AndroidNewsModel;
import com.zx.hotnews.utils.Constants;
import com.zx.hotnews.utils.NetWorkUtil;

import rx.Subscription;

/**
 * Created by zhangxin on 2016/10/28 0028.
 * <p>
 * Description :
 */

public class NewsPresenter implements NewsContract.Presenter {
    private NewsContract.View mView;
    private AndroidNewsModel mModel = new AndroidNewsModel();

    private RequestImpl mRequest;

    public NewsPresenter(NewsContract.View view) {
        mView = view;
        mRequest = new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mView.load(((AndroidNewsBean) object).getResults());
            }

            @Override
            public void loadFailed() {
                mView.showError();
            }

            @Override
            public void loadComplete() {
                mView.showNormal();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                mView.getFragment().addSubscription(subscription);
            }
        };
    }

    @Override
    public void start() {
        if (NetWorkUtil.isNetworkConnected(HotNewsApplication.getInstance())) {
            //TODO: 硬编码了,这里强制获取第一页最新的;
            getNewsFromNet(1);
        } else {
            getNewsFromCache(Constants.NEWS_ALL);
        }
    }


    /*@Override
    public void getNews(int page) {
        if (page < 1) {
            page = 1;
        }
        mModel.getNews(page, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mView.load(((AndroidNewsBean) object).getResults());
            }

            @Override
            public void loadFailed() {
                mView.showError();
            }

            @Override
            public void loadComplete() {
                mView.showNormal();
            }

            @Override
            public void addSubscription(Subscription subscription) {
                mView.getFragment().addSubscription(subscription);
            }
        });
    }*/


    @Override
    public void getNewsFromNet(int page) {
        if (page < 1) {
            page = 1;
        }
        mModel.getNews(page, mRequest);
    }

    @Override
    public void getNewsFromCache(String cacheName) {
        mModel.getNewsFromCache(cacheName, mRequest);

    }


}
