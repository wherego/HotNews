package com.zx.hotnews.engine.news;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zx.hotnews.R;

/**
 * Created by zhangxin on 2016/10/28 0028.
 * <p>
 * Description :
 */
public class NewsHolder extends RecyclerView.ViewHolder {


    LinearLayout news_item;
    ImageView icon;
    TextView desc;
    TextView time;
    TextView author;

    public NewsHolder(View itemView) {
        super(itemView);
        news_item = (LinearLayout) itemView.findViewById(R.id.news_item);
        icon = (ImageView) itemView.findViewById(R.id.news_icon);
        desc = (TextView) itemView.findViewById(R.id.news_desc);
        time = (TextView) itemView.findViewById(R.id.news_time);
        author = (TextView) itemView.findViewById(R.id.news_author);
    }
}
