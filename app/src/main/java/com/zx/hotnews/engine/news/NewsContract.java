package com.zx.hotnews.engine.news;

import com.zx.hotnews.base.BasePresenter;
import com.zx.hotnews.base.BaseView;
import com.zx.hotnews.bean.news.AndroidNewsBean;

import java.util.List;

/**
 * Created by zhangxin on 2016/10/28 0028.
 * <p>
 * Description :
 */

public class NewsContract {
    interface View extends BaseView{
        void load(List<AndroidNewsBean.ResultBean> datas);

        void showError();

        void showNormal();

        //用来订阅;
        NewsFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getNewsFromNet(int page);
        void getNewsFromCache(String cacheName);
    }
}
