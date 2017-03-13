package com.zx.hotnews.engine.picture;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zx.hotnews.R;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */

public class PictureHolder extends RecyclerView.ViewHolder{

    ImageView image;
    public PictureHolder(View itemView) {
        super(itemView);
        image = (ImageView)itemView.findViewById(R.id.girl_img);
    }
}
