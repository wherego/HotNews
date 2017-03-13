package com.zx.hotnews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by zhangxin on 2017/3/10 0010.
 * <p>
 * Description :
 * 试图来封装一个BaseAdapter,但是感觉自己封装的不是很好,暂时不用...
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {


    protected OnItemClickListener<T> listener;

    public  List<T> data = new ArrayList<>();

    public void addAll(List<T> data) {
        this.data.addAll(data);
    }

    public void clear() {
        data.clear();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

    }
}

