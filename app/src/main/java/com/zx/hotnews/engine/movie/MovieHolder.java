package com.zx.hotnews.engine.movie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.hotnews.R;

/**
 * Created by zhangxin on 2016/11/26.
 * <p>
 * Description :
 */

public class MovieHolder extends RecyclerView.ViewHolder {
    LinearLayout movieItem;
    ImageView moviePic;
    TextView movieName;
    TextView movieDirector;
    TextView movieActors;
    TextView movieType;
    TextView movieRating;

    public MovieHolder(View itemView) {
        super(itemView);
        movieItem = (LinearLayout) itemView.findViewById(R.id.ll_one_item);
        moviePic = (ImageView) itemView.findViewById(R.id.iv_one_photo);
        movieName = (TextView) itemView.findViewById(R.id.tv_one_title);
        movieDirector = (TextView) itemView.findViewById(R.id.tv_one_directors);
        movieActors = (TextView) itemView.findViewById(R.id.tv_one_casts);
        movieType = (TextView) itemView.findViewById(R.id.tv_one_genres);
        movieRating = (TextView) itemView.findViewById(R.id.tv_one_rating_rate);

    }
}
