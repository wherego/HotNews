package com.zx.hotnews.engine.picture;

import com.zx.hotnews.base.BasePresenter;
import com.zx.hotnews.base.BaseView;
import com.zx.hotnews.bean.picture.PictureBean;

import java.util.List;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */

public class PictureContract {
    interface View extends BaseView {
        void load(List<PictureBean.ResultBean> datas);

        void showError();

        void showNormal();

        //用来订阅;
        PictureFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getPictures(int page);
    }
}
