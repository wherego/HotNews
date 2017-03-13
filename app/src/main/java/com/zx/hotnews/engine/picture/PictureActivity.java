package com.zx.hotnews.engine.picture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zx.hotnews.R;
import com.zx.hotnews.base.BaseActivity;
import com.zx.hotnews.base.BaseFragment;
import com.zx.hotnews.engine.movie.MovieActivity;
import com.zx.hotnews.engine.news.NewsActivity;

public class PictureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.iv_title_pic).setSelected(true);
        findViewById(R.id.iv_title_hot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PictureActivity.this, NewsActivity.class));
            }
        });
        findViewById(R.id.iv_title_movie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PictureActivity.this, MovieActivity.class));
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pictrue;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return PictureFragment.getInstance();
    }

    @Override
    protected int getFragmentContentId() {
        //这是布局activity_home中的主体部分,使用的是
        return R.id.picture_fragment;
    }
}
