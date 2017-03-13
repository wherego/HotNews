package com.zx.hotnews.engine.picture;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zx.hotnews.R;
import com.zx.hotnews.base.BaseFragment;
import com.zx.hotnews.bean.picture.PictureBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxin on 2016/10/28.
 * <p>
 * Description :
 */
public class PictureFragment extends BaseFragment implements PictureContract.View {

    public List<String> mUrls = new ArrayList<>(); //刚一开始是空的;
    private RecyclerView mRecyclerView;
    private PictureAdapter mAdapter;
    private int mPage = 1;


    PictureContract.Presenter mPresenter;

    //UI部分,考虑要不要设置到Base中;
    // 加载中,一进来是有的,数据加载完设置为gone;
    private LinearLayout mLlProgressBar;

    // 加载失败
    private LinearLayout mRefresh;

    // 内容布局
    protected RelativeLayout mContainer;

    // 动画
    private AnimationDrawable mAnimationDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture; //这里只放了一个recyclerView;
    }

    public static PictureFragment getInstance() {
        PictureFragment pictureFragment = new PictureFragment();
        return pictureFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new PictureAdapter(getActivity(), mUrls);
        mRecyclerView.setAdapter(mAdapter);
        //此时,先不让RecyclerView显示呢...

        mLlProgressBar = (LinearLayout) view.findViewById(R.id.ll_progress_bar);
        ImageView img = (ImageView) view.findViewById(R.id.img_progress);
        // 加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        // 默认进入页面就开启动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        mRefresh = (LinearLayout) view.findViewById(R.id.ll_error_refresh);
        // 点击加载失败布局
        mRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPresenter.getPictures(mPage);
            }
        });

        //初始化数据
        mPresenter = new PicturePresenter(this);//让Presenter持有了view;
        mPresenter.start();
        //loadWelfareData();
        //loadApi(mPage);等同于上面的start();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScrollToEnd(mRecyclerView)) {
                    Log.e("tag", "============scroll to end");
                    mPage += 1;
                    //loadWelfareData();
//                    loadApi(mPage);
                    mPresenter.getPictures(mPage);
                }
            }
        });


    }


    private boolean isScrollToEnd(RecyclerView view) {
        if (view == null) return false;
        if (view.computeVerticalScrollExtent() + view.computeVerticalScrollOffset() >= view
                .computeVerticalScrollRange())
            return true;
        return false;
    }


    @Override
    public void load(List<PictureBean.ResultBean> datas) {
        for (int i = 0; i < datas.size(); i++) {
            mUrls.add(datas.get(i).getUrl());
        }
    }

    @Override
    public void showError() {
        //隐藏加载中...
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        //显示错误提示;
        if (mRefresh.getVisibility() != View.VISIBLE) {
            mRefresh.setVisibility(View.VISIBLE);
        }

    }

    //用于数据加载完之后,通知RecyclerView来改变数据;在这里修改一些UI吧;
    @Override
    public void showNormal() {
        //隐藏加载中...
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        //隐藏显示错误
        if (mRefresh.getVisibility() != View.GONE) {
            mRefresh.setVisibility(View.GONE);
        }
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public PictureFragment getFragment() {
        return this;
    }


/*    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private void loadApi(int page) {
        Request request = new Request.Builder().url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/" + page).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tag", "loading failure ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject json = new JSONObject(result);
                        JSONArray array = new JSONArray(json.getString("results"));
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ob = array.getJSONObject(i);
                            mUrls.add(ob.getString("url"));
                            Log.e("tag", "========== url: " + ob.getString("url"));
                        }

                        mHandler.sendEmptyMessage(2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }*/

        /*private void loadWelfareData() {
        //每次多加载一页,有mPage来决定加载哪一页;
        mModel.setData("福利", mPage, HttpUtils.per_page_more);
        mModel.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                PictureBean pictureBean = (PictureBean) object;
                if (mPage == 1) { //初始化的mPage就是1,表明这是第一次进来;一下加载了20张图片,服务器一页最多是50张;
                    if (pictureBean != null && pictureBean.getResults() != null && pictureBean.getResults().size() >
                            0) {
                        mUrls.clear();//之前的数据不要了
                        //重新初始化imgList;
                        for (int i = 0; i < pictureBean.getResults().size(); i++) {
                            String url = pictureBean.getResults().get(i).getUrl();
                            Log.e("###",url);
                            mUrls.add(url);
                        }

                    }
                } else {
                    //不是第一次加载的时候;
                    if (pictureBean != null && pictureBean.getResults() != null
                            && pictureBean.getResults().size() > 0) {
                        for (int i = 0; i < pictureBean.getResults().size(); i++) {
                            mUrls.add(pictureBean.getResults().get(i).getUrl());
                        }

                    }
                }
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void loadFailed() {
                Log.e("###","加载失败");

                if (mPage > 1) {
                    mPage--;
                }
            }

            @Override
            public void addSubscription(Subscription subscription) {
               addSubscription(subscription);
            }
        });
    }*/
}
