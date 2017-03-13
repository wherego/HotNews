package com.zx.hotnews.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxin on 2016/10/23.
 * <p>
 * Description :
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    //rx部分,用来收集所有的subscriber(订阅者),接下来在Activity onPause或者onDestroy时候统一取消订阅，避免造成内存泄漏
    //移除订阅之后,就不能再订阅了,除非是重新创建一个该对象;
    private CompositeSubscription mCompositeSubscription;

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取fragment布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    //持有宿主Activity,这样不会出现getActivity()==null的,情况,以后需要使用Activity,就直接用 mActivity;但是会有内存泄露的隐患;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            getHoldingActivity().addFragment(fragment);
        }
    }

    //移除fragment
    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    //销毁时解除绑定,避免内存泄露;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
