package com.zx.hotnews.engine.movie;

import com.zx.hotnews.bean.movie.HotMovieBean;
import com.zx.hotnews.engine.picture.PictureContract;
import com.zx.hotnews.http.RequestImpl;
import com.zx.hotnews.model.MovieModel;
import com.zx.hotnews.model.PictureModel;

import rx.Subscription;

/**
 * Created by zhangxin on 2016/11/26.
 * <p>
 * Description :
 */
public class MoviePresenter  implements MovieContract.Presenter{

    private MovieContract.View mView;
    private MovieModel mModel = new MovieModel();

    MoviePresenter(MovieContract.View view) {
        mView = view;
    }
    @Override
    public void getMovie() {
        mModel.getMovie(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mView.load(((HotMovieBean)object).getSubjects());
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

    @Override
    public void start() {
        getMovie();
    }
}
