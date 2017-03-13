package com.zx.hotnews.engine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zx.hotnews.R;
import com.zx.hotnews.engine.news.NewsActivity;
import com.zx.hotnews.utils.Constants;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private ImageView mBg;
    private TextView mJump;
//    private boolean isEnter;  //默认false;


    private int ENTER_HOME = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            toHomeActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();

        initEvent();
    }

    void initViews() {
        mBg = (ImageView) findViewById(R.id.iv_pic);
        mJump = (TextView) findViewById(R.id.tv_jump);
    }

    void initEvent() {

        mJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeMessages(ENTER_HOME);//及时移除延时消息,避免内存泄露;
                toHomeActivity();
            }
        });

        int i = new Random().nextInt(Constants.TRANSITION_URLS.length);
        Glide.with(this)
                .load(Constants.TRANSITION_URLS[i])   //网络加载字符串类型的url;
                .placeholder(R.drawable.img_transition_default) //网络加载时设置的等待图片;
                .error(R.drawable.img_transition_default)
                .into(mBg);


        mHandler.sendEmptyMessageDelayed(ENTER_HOME, 3000);  //延时3s进入主页面;

    }


    private void toHomeActivity() {
/*        //isIn初始值是false;如果已经true了,那么
        if (isEnter) {
            return;
        }*/
        //进行一次跳转后,表明是手动点击了跳过,那么将isIn设置成true,防止默认的计时再次进入MainActicity;
//        Intent intent = new Intent(this, PictureActivity.class);
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
        //跳转页面还是用了动画,进入的动画是缩小,退出的界面是out;
        //Activity切换效果,在startActivity(intent)之后调该方法即可;
        // 可能会产生不起作用的情况:在android2.0一下,这个情况不用担心,已经不再对4.0一下做兼容了;要么就是该方法的调用位置不对,不能在嵌套的子Activity中;
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
//        isEnter = true;
    }
}
