package com.zx.hotnews.engine.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zx.hotnews.R;
import com.zx.hotnews.bean.news.AndroidNewsBean;
import com.zx.hotnews.engine.WebViewActivity;

import java.util.List;

/**
 * Created by zhangxin on 2016/10/28 0028.
 * <p>
 * Description :
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {

    private final int[] icons = {
            R.drawable.img0,
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9,
    };


    private List<AndroidNewsBean.ResultBean> mItems;
    private Context mContext;

    public NewsAdapter(Context context, List<AndroidNewsBean.ResultBean> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        AndroidNewsBean.ResultBean bean = mItems.get(position);
        if (bean.getImages() != null
                && bean.getImages().size() > 0
                && !TextUtils.isEmpty(bean.getImages().get(0))) {
            Glide.with(mContext)
                    .load(bean.getImages().get(0))
                    .asBitmap()
                    .placeholder(R.drawable.img_one_bi_one)
                    .error(R.drawable.img_one_bi_one)
                    .into(holder.icon);
        } else {
            holder.icon.setVisibility(View.INVISIBLE);
        }
//        Glide.with(mContext).load(icons[position % 10]).into(holder.icon);
        holder.desc.setText(bean.getDesc());
        holder.time.setText(bean.getPublishedAt());
        holder.author.setText(bean.getWho());
        final String url = bean.getUrl();
        holder.news_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
