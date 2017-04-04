package com.zx.hotnews.engine.movie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zx.hotnews.R;
import com.zx.hotnews.bean.movie.MovieDetailBean;
import com.zx.hotnews.bean.movie.SubjectsBean;
import com.zx.hotnews.http.HttpUtils;
import com.zx.hotnews.utils.CommonUtils;
import com.zx.hotnews.utils.StatusBarUtil;
import com.zx.hotnews.utils.StatusBarUtils;
import com.zx.hotnews.utils.StringFormatUtil;
import com.zx.hotnews.widget.CustomChangeBounds;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.transitionName;

public class MovieDetailActivity extends AppCompatActivity {

    private SubjectsBean subjectsBean;
    private String mMoreUrl;
    private String mMovieName;
    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 滑动多少距离后标题不透明
    private int slidingDistance;

    private ImageView imgItemBg;
    private ImageView imgOnePhoto;
    private TextView tvOneRattingRate;
    private TextView tvOneRatingNumber;
    private TextView tvOneDirector;
    private TextView tvOneCasts;
    private TextView tvOneGenres;

    private TextView tvOneDay;
    private TextView tvOneCity;
    private RecyclerView rvCast;
    private Toolbar tbBaseTitle;
    private ImageView ivBaseTitlebarBg;

    private TextView tvOneTitle;
    private TextView tvOneSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initView();


        // 设置toolbar
        setToolBar();


        if (getIntent() != null) {
            subjectsBean = (SubjectsBean) getIntent().getSerializableExtra("bean");
            initHeader();
        }

        initSlideShapeTheme(setHeaderImgUrl(), setHeaderImageView());

        setTitles();

        loadMovieDetail();
    }

    private void initHeader() {
        Glide.with(imgOnePhoto.getContext())
                .load(subjectsBean.getImages().getLarge())
                .crossFade(500)
                .override((int) CommonUtils.getDimens(R.dimen.movie_detail_width), (int) CommonUtils.getDimens(R
                        .dimen.movie_detail_height))
                .placeholder(R.drawable.img_default_meizi)
                .error(R.drawable.img_default_meizi)
                .into(imgOnePhoto);

        tvOneRattingRate.setText("评分:" + subjectsBean.getRating().getAverage());
        tvOneRatingNumber.setText(subjectsBean.getCollect_count() + "人评");
        tvOneDirector.setText(StringFormatUtil.formatName(subjectsBean.getDirectors()));
        tvOneCasts.setText(StringFormatUtil.formatName(subjectsBean.getCasts()));
        tvOneGenres.setText("类型:" + StringFormatUtil.formatGenres(subjectsBean.getGenres()));

        // "23":模糊度；"4":图片缩放4倍后再进行模糊
        Glide.with(this)
                .load(subjectsBean.getImages().getMedium())
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 23, 4))
                .into(imgItemBg);
    }

    private void initView() {
        imgItemBg = (ImageView) findViewById(R.id.img_item_bg);
        imgOnePhoto = (ImageView) findViewById(R.id.iv_one_photo);
        tvOneRattingRate = (TextView) findViewById(R.id.tv_one_rating_rate);
        tvOneRatingNumber = (TextView) findViewById(R.id.tv_one_rating_number);
        tvOneDirector = (TextView) findViewById(R.id.tv_one_directors);
        tvOneCasts = (TextView) findViewById(R.id.tv_one_casts);
        tvOneGenres = (TextView) findViewById(R.id.tv_one_genres);

        tvOneTitle = (TextView) findViewById(R.id.tv_one_title);
        tvOneSummary = (TextView) findViewById(R.id.tv_one_summary);

        tvOneDay = (TextView) findViewById(R.id.tv_one_day);
        tvOneCity = (TextView) findViewById(R.id.tv_one_city);
        rvCast = (RecyclerView) findViewById(R.id.xrv_cast);
        tbBaseTitle = (Toolbar) findViewById(R.id.tb_base_title);
        ivBaseTitlebarBg = (ImageView) findViewById(R.id.iv_base_titlebar_bg);

    }

    protected void setTitles() {
        Log.e("###", "setTitles: " + subjectsBean.getTitle());
        tbBaseTitle.setTitle(subjectsBean.getTitle());
        tbBaseTitle.setSubtitle(String.format("主演：%s", StringFormatUtil.formatName(subjectsBean.getCasts())));
    }


    private void loadMovieDetail() {
        Subscription get = HttpUtils.getInstance().getMovieDetailClient().getMovieDetail(subjectsBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetailBean>() {
                    @Override
                    public void onCompleted() {
                        showContentView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(final MovieDetailBean movieDetailBean) {
                        // 上映日期
                        tvOneDay.setText(String.format("上映日期：%s", movieDetailBean.getYear()));
                        // 制片国家
                        tvOneCity.setText(String.format("制片国家/地区：%s", StringFormatUtil.formatGenres
                                (movieDetailBean.getCountries())));

                        tvOneTitle.setText(StringFormatUtil.formatGenres(movieDetailBean.getAka()));
                        tvOneSummary.setText(movieDetailBean.getSummary());

                        mMoreUrl = movieDetailBean.getAlt();
                        mMovieName = movieDetailBean.getTitle();

                        transformData(movieDetailBean);
                    }
                });


    }


    /**
     * 异步线程转换数据
     */
    private void transformData(final MovieDetailBean movieDetailBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < movieDetailBean.getDirectors().size(); i++) {
                    movieDetailBean.getDirectors().get(i).setType("导演");
                }
                for (int i = 0; i < movieDetailBean.getCasts().size(); i++) {
                    movieDetailBean.getCasts().get(i).setType("演员");
                }

                MovieDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(movieDetailBean);
                    }
                });
            }
        }).start();
    }

    /**
     * *** 初始化滑动渐变 一定要实现 ******
     *
     * @param imgUrl    header头部的高斯背景imageUrl
     * @param mHeaderBg header头部高斯背景ImageView控件
     */
    protected void initSlideShapeTheme(String imgUrl, ImageView mHeaderBg) {
        setImgHeaderBg(imgUrl);

        // toolbar 的高
        int toolbarHeight = tbBaseTitle.getLayoutParams().height;
        final int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最低端，保留（titlebar+statusbar）的高度
        ViewGroup.LayoutParams params = ivBaseTitlebarBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) ivBaseTitlebarBg
                .getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);

        ivBaseTitlebarBg.setImageAlpha(0);
        StatusBarUtils.setTranslucentImageHeader(this, 0, tbBaseTitle);

        // 上移背景图片，使空白状态栏消失(这样下方就空了状态栏的高度)
        if (mHeaderBg != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHeaderBg.getLayoutParams();
            layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0);

            ViewGroup.LayoutParams imgItemBgparams = mHeaderBg.getLayoutParams();
            // 获得高斯图背景的高度
            imageBgHeight = imgItemBgparams.height;
        }

        // 变色
        initScrollViewListener();
        initNewSlidingParams();
    }


    protected String setHeaderImgUrl() {
        if (subjectsBean == null) {
            return "";
        }
        return subjectsBean.getImages().getMedium();
    }


    protected ImageView setHeaderImageView() {
        return (ImageView) findViewById(R.id.img_item_bg);
    }

    /**
     * 加载titlebar背景
     */
    private void setImgHeaderBg(String imgUrl) {
        Log.e("###", "拿到的url" + imgUrl);
        if (!TextUtils.isEmpty(imgUrl)) {

            // 高斯模糊背景 原来 参数：12,5  23,4
            Glide.with(this).load(imgUrl)
                   // .placeholder(R.drawable.stackblur_default)
                    .error(R.drawable.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this, 23, 4))
                    .listener(new RequestListener<String,GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                        isFirstResource) {
                    e.printStackTrace();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                               boolean isFromMemoryCache, boolean isFirstResource) {
                    tbBaseTitle.setBackgroundColor(Color.TRANSPARENT);
                    ivBaseTitlebarBg.setImageAlpha(0);
                    ivBaseTitlebarBg.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(ivBaseTitlebarBg);
        }
    }


    private void initScrollViewListener() {
        // 为了兼容23以下
        ((NestedScrollView) findViewById(R.id.mns_base)).setOnScrollChangeListener(new NestedScrollView
                .OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });
    }

    private void initNewSlidingParams() {
        int titleBarAndStatusHeight = (int) (CommonUtils.getDimens(R.dimen.nav_bar_height) + StatusBarUtil
                .getStatusBarHeight(this));
        // 减掉后，没到顶部就不透明了
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (CommonUtils.getDimens(R.dimen
                .base_header_activity_slide_more));
    }


    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);

        Drawable drawable = ivBaseTitlebarBg.getDrawable();

        if (drawable == null) {
            return;
        }
        if (scrolledY <= slidingDistance) {
            // title部分的渐变
            drawable.mutate().setAlpha((int) (alpha * 255));
            ivBaseTitlebarBg.setImageDrawable(drawable);
        } else {
            drawable.mutate().setAlpha(255);
            ivBaseTitlebarBg.setImageDrawable(drawable);
        }
    }

    protected void showLoading() {
       /* if (llProgressBar.getVisibility() != View.VISIBLE) {
            llProgressBar.setVisibility(View.VISIBLE);
        }
        // 开始动画
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        if (bindingContentView.getRoot().getVisibility() != View.GONE) {
            bindingContentView.getRoot().setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }*/
        Log.e("###", "加载中ing...");
    }

    protected void showContentView() {
        /*if (llProgressBar.getVisibility() != View.GONE) {
            llProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        if (bindingContentView.getRoot().getVisibility() != View.VISIBLE) {
            bindingContentView.getRoot().setVisibility(View.VISIBLE);
        }*/

    }

    protected void showError() {
/*        if (llProgressBar.getVisibility() != View.GONE) {
            llProgressBar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);
        }
        if (bindingContentView.getRoot().getVisibility() != View.GONE) {
            bindingContentView.getRoot().setVisibility(View.GONE);
        }*/

        Log.e("###", "加载失败了");
    }

    /**
     * 失败后点击刷新
     */
    protected void onRefresh() {

    }


    /**
     * 设置导演&演员adapter
     */
    private void setAdapter(MovieDetailBean movieDetailBean) {
        rvCast.setVisibility(View.VISIBLE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MovieDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCast.setLayoutManager(mLayoutManager);

        // 需加，不然滑动不流畅
        rvCast.setNestedScrollingEnabled(false);
        rvCast.setHasFixedSize(false);

        MovieDetailAdapter mAdapter = new MovieDetailAdapter();
        mAdapter.addAll(movieDetailBean.getDirectors());
        mAdapter.addAll(movieDetailBean.getCasts());
        rvCast.setAdapter(mAdapter);
    }


    /**
     * 设置自定义 Shared Element切换动画
     * 默认不开启曲线路径切换动画，
     * 开启需要重写setHeaderPicView()，和调用此方法并将isShow值设为true
     *
     * @param imageView 共享的图片
     * @param isShow    是否显示曲线动画
     */
    protected void setMotion(ImageView imageView, boolean isShow) {
        if (!isShow) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();
            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(imageView);
            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }

    /**
     * 设置toolbar
     */
    protected void setToolBar() {
        setSupportActionBar(tbBaseTitle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        // 手动设置才有效果
        tbBaseTitle.setTitleTextAppearance(this, R.style.ToolBar_Title);
        tbBaseTitle.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
//        tbBaseTitle.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        tbBaseTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    /**
     * @param context      activity
     * @param positionData bean
     * @param imageView    imageView
     */
    public static void start(Activity context, SubjectsBean positionData, ImageView imageView) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("bean", positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, "transition_movie_img");//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
