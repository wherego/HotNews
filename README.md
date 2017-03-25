[TOC]

**重要:**本应用作为MVP的练手项目,已经完成基本的逻辑,是一个可用的版本,但是最近想重构该项目,采用单Activity+多Fragment的结构,并添加一些新的功能,新项目地址=>[FreeTime](https://github.com/zachaxy/FreeTime),欢迎关注





# 简介

本款软件是一个平时练手的项目,整体采用的MVP架构,并结合`RxJava`+`Retrofit`+`Glide`开源框架的一款新闻客户端.整个应用的业务大致分为三个模块:

- 新闻模块(主要是Android开发相关的新闻,由Gank.io所提供的新闻资源)
- 美图模块(由Gank.io所提供的图片资源)
- 热门电影模块(由豆瓣所提供的热门电影的API接口)

# 代码结构

按照包名,做一个简单的介绍:

- adapter:由于当前应用中大量使用了RecyclerView,所以想为其做一个基类的adapter封装,但是目前并没有什么好的思路,暂时不用;

- base:

  - BaseActivity:整个Activity的基类,接下来所有的Activity都要继承该Activity,可以在这个类中做一些基础的封装,eg:UI界面的统一,懒加载等;
  - BaseFragment:整个Fragment的基类,接下来所有的Fragment都要继承该Fragment
  - BaseView:MVP中的V的基类
  - BasePresenter:MVP中P的基类

- bean

  - news:新闻bean对象
  - picture:美图bean对象
  - movie:电影bean对象

- engine:整个应用的业务逻辑,是最核心的部分.在下面拿出来单独细说;

- http:有关网络请求相关的工具;

- model:整个MVP中的M部分

  - AndroidNewsModel
  - MovieModel
  - PictureModel

- rx:整合了RxBus,作为练习使用;

- utils:辅助的工具类;

- widget:自定义的控件

  ​

这个最能体现MVP结构的部分在与engine包中,所以单独拿出来说一下:

整个应用分为新闻,美图和电影三块业务,在engine这个包中,这里单独拿出新闻的业务来举例子,其它两个都是一样的;

- engine

  - news
    - NewsActivity
    - NewsFragment
    - NewsPresenter
    - NewsContract




这里引用了一个契约接口:NewsContract

```java
public class NewsContract {
    interface View extends BaseView{
        void load(List<AndroidNewsBean.ResultBean> datas);

        void showError();

        void showNormal();

        //用来订阅;
        NewsFragment getFragment();
    }

    interface Presenter extends BasePresenter {
        void getNews(int page);
    }
}
```



在这里面管理了View和Presenter,所有的接口中的方法,在这里一目了然.



MVP的核心是让M和V分离,不让其直接产生交互,所以使用Presenter来作为二者之间交互的一个媒介.

这里,然后由Fragment来实现View中的接口,充当MVP中的V,Activity只是作为一个控制管理,用来控制Fragment(V)

同时在Presenter中要分别持有V和M的引用,这样就可以由P来统一调度管理V和M之间的通信.


# 效果图
- **部分效果图**

<img width="173" height=“274” src="https://github.com/zachaxy/HotNews/blob/master/screenshots/p1.jpg"></img>
<img width="173" height=“274” src="https://github.com/zachaxy/HotNews/blob/master/screenshots/p2.jpg"></img>
<img width="173" height=“274” src="https://github.com/zachaxy/HotNews/blob/master/screenshots/p3.jpg"></img>
<img width="173" height=“274” src="https://github.com/zachaxy/HotNews/blob/master/screenshots/p4.jpg"></img>
<img width="173" height=“274” src="https://github.com/zachaxy/HotNews/blob/master/screenshots/p5.jpg"></img>

 

# TODO

该项目只是一个初步的玩具应用,还有很多不足之处需要接下来的时间去完成.

- 引入Dragger2
- 代码的封装不是很好,还有待提高
- 为RecyclerView设计一个统一的Adapter
- ......