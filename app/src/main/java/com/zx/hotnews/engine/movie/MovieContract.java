package com.zx.hotnews.engine.movie;

import com.zx.hotnews.base.BasePresenter;
import com.zx.hotnews.base.BaseView;
import com.zx.hotnews.bean.movie.SubjectsBean;
import com.zx.hotnews.engine.picture.PictureFragment;

import java.util.List;

/**
 * Created by zhangxin on 2016/11/26.
 * <p>
 * Description :
 */

public interface MovieContract {
    interface View extends BaseView {
        void load(List<SubjectsBean> datas);

        void showError();

        void showNormal();

        //用来订阅;
        PictureFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getMovie();
    }
}
