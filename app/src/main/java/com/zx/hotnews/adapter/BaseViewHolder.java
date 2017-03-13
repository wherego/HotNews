package com.zx.hotnews.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangxin on 2017/3/11 0011.
 * <p>
 * Description :
 */

public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    //下一个和上一个调用的super是一个方法
    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
    }

    protected <T extends View> T inflateItem(@IdRes int id) {
        return (T) itemView.findViewById(id);
    }

    public void setData(M data){};

    protected Context getContext() {
        return itemView.getContext();
    }
}
