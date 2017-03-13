package com.zx.hotnews.engine.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zx.hotnews.R;
import com.zx.hotnews.base.BaseActivity;
import com.zx.hotnews.base.BaseFragment;
import com.zx.hotnews.engine.movie.MovieActivity;
import com.zx.hotnews.engine.picture.PictureActivity;
import com.zx.hotnews.rx.RxBus;
import com.zx.hotnews.rx.RxCodeConstants;
import com.zx.hotnews.utils.TimeUtil;

import rx.functions.Action1;

public class NewsActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return NewsFragment.getInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.news_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.iv_title_hot).setSelected(true);
        findViewById(R.id.iv_title_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsActivity.this, PictureActivity.class));
            }
        });
        findViewById(R.id.iv_title_movie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsActivity.this, MovieActivity.class));
            }
        });

        ((TextView)findViewById(R.id.tv_daily_text)).setText(TimeUtil.getTodayTime().get(2));
        findViewById(R.id.daily_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TO, RxCodeConstants.JUMP_TO_PICTURE);
            }
        });

        findViewById(R.id.ib_movie_hot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(RxCodeConstants.JUMP_TO, RxCodeConstants.JUMP_TO_MOVIE);
            }
        });
        initRxBus();
    }

    private void initRxBus() {
        RxBus.getDefault().toObservable(RxCodeConstants.JUMP_TO, Integer.class)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer index) {
                        if (index == RxCodeConstants.JUMP_TO_MOVIE) {
                            startActivity(new Intent(NewsActivity.this, MovieActivity.class));
                        } else if (index == RxCodeConstants.JUMP_TO_PICTURE) {
                            startActivity(new Intent(NewsActivity.this, PictureActivity.class));
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
