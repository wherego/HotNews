package com.zx.hotnews.engine.news;

import com.zx.hotnews.bean.news.AndroidNewsBean;
import com.zx.hotnews.http.RequestImpl;
import com.zx.hotnews.model.AndroidNewsModel;

import rx.Subscription;

/**
 * Created by zhangxin on 2016/10/28 0028.
 * <p>
 * Description :
 */

public class NewsPresenter implements NewsContract.Presenter {
    private NewsContract.View mView;
    private AndroidNewsModel mModel = new AndroidNewsModel();

    public NewsPresenter(NewsContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        getNews(1);
    }


    @Override
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
    }


}
