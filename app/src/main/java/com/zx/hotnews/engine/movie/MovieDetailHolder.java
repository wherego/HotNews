package com.zx.hotnews.engine.movie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.hotnews.R;

/**
 * Created by zhangxin on 2017/4/3 0003.
 * <p>
 * Description :
 */

public class MovieDetailHolder extends RecyclerView.ViewHolder {

    ImageView avatar;
    TextView name;
    TextView type;

    public MovieDetailHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        name = (TextView) itemView.findViewById(R.id.tv_name);
        type = (TextView) itemView.findViewById(R.id.tv_type);
    }
}
