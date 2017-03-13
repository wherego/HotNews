package com.zx.hotnews.base;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by zhangxin on 2016/10/23.
 * <p>
 * Description :
 */

public abstract class BaseActivity extends AppCompatActivity {
    //布局文件ID
    protected abstract int getContentViewId();

    //获取第一个fragment,添加到Activity中,初始化Activity的布局,避免空白一片
    protected abstract BaseFragment getFirstFragment();


    //布局中盛放Fragment容器的ID,这个名气起的真是晦涩,更好的名字是 getFragmentContainerId();
    protected abstract int getFragmentContentId();

   /* //这个方法也不知道是干啥的,和注解有关?
    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }*/

    //获取到intent,处理其中的消息,可能附带着一些参数?
    protected void handleIntent(Intent intent) {

    }

    //首先为Activity设置content,此时如果有intent的话,就处理intent;接着添加一个Fragment,免得显示一片空白
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        //避免重复添加Fragment,另一种方法是判断saveInstanceState是否为null,目的都一样,就是避免Fragment重叠;
        if (null == getSupportFragmentManager().getFragments()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }
    }

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss(); //可能不是一个好的提交方式,但是可以避免一些不必要的 Exception;
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    //返回键返回事件,因为我们的Activity中其实只有一个Fragment作为界面展示的,如果点击了back键不做处理
    //其实只是把fragment移除了,Activity依然在,所以我们要额外处理一下,点击了back,fragment移除,Activity结束;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
