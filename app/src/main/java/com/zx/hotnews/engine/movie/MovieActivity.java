package com.zx.hotnews.engine.movie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zx.hotnews.R;
import com.zx.hotnews.base.BaseActivity;
import com.zx.hotnews.base.BaseFragment;
import com.zx.hotnews.engine.news.NewsActivity;
import com.zx.hotnews.engine.picture.PictureActivity;

public class MovieActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_movie;
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return MovieFragment.getInstance();
    }


    @Override
    protected int getFragmentContentId() {
        return R.id.movie_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.iv_title_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieActivity.this, PictureActivity.class));
            }
        });
        findViewById(R.id.iv_title_hot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieActivity.this, NewsActivity.class));
            }
        });
        findViewById(R.id.iv_title_movie).setSelected(true);
    }
}
