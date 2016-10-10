package com.example.zchao.viewpagergroup;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.PopupAdapter;
import base.DataProvider;
import javabean.PrettyGrilImage;
import util.ApiManager;
import util.ContextUtils;
import util.SingCallBack;

/**
 * 实现原理，首先自定义一个竖直方向的Viewpager {@link #(com.example.zchao.viewpagergroup.VerticalViewPager)}
 * 此pager用来包裹多个Fragment{@link #(PagerFragment)};在PagerFragment中按照正常的Viewpager使用即可；
 */
public class MainActivity extends FragmentActivity implements DataProvider, View.OnClickListener {

    public static final String TAG = "MainActivity";
    private static final String SELECT_KEY = "select_type_key";//用于存储用户选择的关注图片类别
    private VerticalViewPager mViewPager;
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;
    private int page = 1;
    private MyPagerAdapter myPagerAdapter;
    private static boolean isFullScreen = false;
    private static boolean isNotitle = false;
    private RelativeLayout mContent;
    private FloatingActionButton mTypeBtn;
    private PopupWindow mTypeSelectWindow;
    private FrameLayout mRoot;
    private int selectID = 4001;
    private int lastPos = 0;
    private boolean inRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTypeBtn = (FloatingActionButton) findViewById(R.id.type);
        mViewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        mContent = (RelativeLayout) findViewById(R.id.activity_main);
        mRoot = (FrameLayout) findViewById(R.id.root);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mTypeBtn.setOnClickListener(this);

        mViewPager.setAdapter(myPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mViewPager.getAdapter().getCount() - 2) {
                    if (positionOffsetPixels < 10 && positionOffsetPixels - lastPos > 0) {//表示像下滑动
                        page++;
                        lastPos = positionOffsetPixels;
                        getMoreDate(String.valueOf(selectID), String.valueOf(page), false);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) {
                    view.setAlpha(0);

                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationY(vertMargin - horzMargin / 2);
                    } else {
                        view.setTranslationY(-vertMargin + horzMargin / 2);
                    }

                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else {
                    view.setAlpha(0);
                }
            }
        });
        refresh();
    }

    /**
     * 切换全屏显示
     *
     * @param isFull 当前是否是全屏显示
     */
    private void changeFullScreen(boolean isFull) {
        if (isFull) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void changeTitleBarVisible(boolean isVisible) {
        if (isVisible) {

        } else {

        }

    }

    @Override
    public void getMoreDate(String typeId, String page, final boolean forceRefresh) {
        if (inRequest) {
            return;
        }
        inRequest  = true;
        ApiManager.getInstance().getAllImageByType(new SingCallBack<PrettyGrilImage>() {
            @Override
            public void onDateReceive(PrettyGrilImage object, Throwable throwable, boolean isSuccess) {
                if (isSuccess && object != null && object.showapi_res_body != null) {
                    PrettyGrilImage.ShowapiResBodyBean body = object.showapi_res_body;
                    PrettyGrilImage.ShowapiResBodyBean.PagebeanBean pagebean = body.pagebean;
                    if (body.ret_code == 0 && pagebean != null) {
                        myPagerAdapter.addAlllist(pagebean, forceRefresh);
                        inRequest = false;
                    }
                }
            }
        }, typeId, page, forceRefresh);
    }


    @Override
    public void refresh() {
        int defaultID = AppSetting.getInstance().getInt(SELECT_KEY);
        if (defaultID != -1) {
            selectID = defaultID;
        }
        getMoreDate(String.valueOf(selectID), String.valueOf(page), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type:
                changePopupWindow();
                break;
        }
    }

    /**
     * 显示或隐藏类别列表
     */
    private void changePopupWindow() {
        if (mTypeSelectWindow != null && mTypeSelectWindow.isShowing()) {
            hidePopupWindow();
        } else {
            showPopupWindow();
        }
    }

    private void showPopupWindow() {
        DisplayMetrics metrix = ContextUtils.getMetrix(this);
        if (mTypeSelectWindow == null) {
            View popView = getLayoutInflater().inflate(R.layout.main_activity_type_select, null);
            mTypeSelectWindow = new PopupWindow(popView, metrix.widthPixels, (int) ContextUtils.dp2pix(this, 200), true);
            mTypeSelectWindow.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            bindDate(popView);
            mTypeSelectWindow.setOutsideTouchable(true);
            mTypeSelectWindow.setFocusable(true);
            mTypeSelectWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
            mTypeSelectWindow.setTouchable(true);

        }
        mTypeSelectWindow.showAtLocation(mRoot, Gravity.BOTTOM, 0, (int) (mTypeBtn.getHeight() + ContextUtils.dp2pix(this, 30)));
    }

    private void bindDate(View view) {
        if (mTypeSelectWindow != null) {
            RecyclerView mPopupList = (RecyclerView) view.findViewById(R.id.popup_list);
            mPopupList.setLayoutManager(new GridLayoutManager(this, 4));
            final PopupAdapter adapter = new PopupAdapter(this);
            mPopupList.setAdapter(adapter);
            adapter.setSelectId(selectID);
            adapter.setListener(new PopupAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int typeId) {
                    AppSetting.getInstance().setInt(SELECT_KEY, typeId);
                    adapter.setSelectId(typeId);
                    refresh();
                }
            });
        }
    }

    private void hidePopupWindow() {
        mTypeSelectWindow.dismiss();
    }


    /**
     * 竖直方向Viewpager的Adapter
     */
    class MyPagerAdapter extends FragmentPagerAdapter {
        public static final String IMG_KEY = "img_key";
        ArrayList<PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list = new ArrayList<>();
//        private HashMap<Integer, PagerFragment> cacheFragment = new HashMap<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PagerFragment pagerFragment = new PagerFragment();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String s = gson.toJson(list.get(position));
            bundle.putString(IMG_KEY, s);
            pagerFragment.setArguments(bundle);
            pagerFragment.setOnPagerItemClick(new PagerFragment.OnPagerItemClick() {
                @Override
                public void onItemClick(int position) {
                    changeTitleBarVisible(isNotitle);
                    isNotitle = !isNotitle;
                }

            });
            pagerFragment.setOnPagerItemLongClick(new PagerFragment.OnPagerItemLongClick() {
                @Override
                public void onItemLongClick(int position) {
                    changeFullScreen(isFullScreen);
                    isFullScreen = !isFullScreen;
                }
            });
            return pagerFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PagerFragment fragment = (PagerFragment) super.instantiateItem(container, position);
            fragment.refreshData(list.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return list.size();
        }


        /**
         * 批量加载数据
         *
         * @param array
         */
        public void addAlllist(PrettyGrilImage.ShowapiResBodyBean.PagebeanBean array, boolean needClear) {
            if (array == null || array.contentlist.size() == 0) {
                return;
            }
            if (needClear) {
                list.clear();
            }
            list.addAll(array.contentlist);
            notifyDataSetChanged();
        }

    }

}
