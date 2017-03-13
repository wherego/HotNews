package com.zx.hotnews.adapter;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by zhangxin on 2017/3/10 0010.
 * <p>
 * Description :
 */

public interface OnItemClickListener<T> {
    void onClick(T t,int position);
}
