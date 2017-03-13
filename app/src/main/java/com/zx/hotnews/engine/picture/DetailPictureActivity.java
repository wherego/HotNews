package com.zx.hotnews.engine.picture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zx.hotnews.R;

public class DetailPictureActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_picture);

       String url =  getIntent().getStringExtra("url");

        mImageView = (ImageView) findViewById(R.id.detail_img);
        Glide.with(this).load(url)
                .crossFade(700).into(mImageView);

    }
}
