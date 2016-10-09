package com.example.zchao.viewpagergroup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;

import base.DataProvider;
import javabean.PrettyGrilImage;
import util.ApiManager;
import util.SingCallBack;

/**
 * 实现原理，首先自定义一个竖直方向的Viewpager {@link #(com.example.zchao.viewpagergroup.VerticalViewPager)}
 * 此pager用来包裹多个Fragment{@link #(PagerFragment)};在PagerFragment中按照正常的Viewpager使用即可；
 */
public class MainActivity extends FragmentActivity implements DataProvider, View.OnClickListener{

    private VerticalViewPager mViewPager;
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;
    private int page = 1;
    private MyPagerAdapter myPagerAdapter;
    private static boolean isFullScreen = false;
    private static boolean isNotitle = false;
    private RelativeLayout mRoot;
    private FloatingActionButton mTypeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTypeBtn = (FloatingActionButton) findViewById(R.id.type);
        mViewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        mRoot = (RelativeLayout) findViewById(R.id.activity_main);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mTypeBtn.setOnClickListener(this);

        mViewPager.setAdapter(myPagerAdapter);

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

        getMoreDate("4002", String.valueOf(page), false);
    }

    /**
     * 切换全屏显示
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
    public void getMoreDate(String typeId, String page, boolean forceRefresh) {
        ApiManager.getInstance().getAllImageByType(new SingCallBack<PrettyGrilImage>() {
            @Override
            public void onDateReceive(PrettyGrilImage object, Throwable throwable, boolean isSuccess) {
                if (isSuccess && object != null && object.showapi_res_body != null) {
                    PrettyGrilImage.ShowapiResBodyBean body = object.showapi_res_body;
                    PrettyGrilImage.ShowapiResBodyBean.PagebeanBean pagebean = body.pagebean;
                    if (body.ret_code == 0 && pagebean != null) {
                        myPagerAdapter.addAlllist(pagebean);
                    }
                }
            }
        }, typeId, page, forceRefresh);
    }


    @Override
    public void refresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type:
                myPagerAdapter.clearAllDate();
                break;
        }
    }

    /**
     * 竖直方向Viewpager的Adapter
     */
    class MyPagerAdapter extends FragmentPagerAdapter {
        public static final String IMG_KEY = "img_key";
        ArrayList<PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean> list = new ArrayList<>();

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
        public int getCount() {
            return list.size();
        }

        public void addlist(PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean object) {
            if (object == null) {
                return;
            }
            list.add(object);
            notifyDataSetChanged();
        }

        /**
         * 批量加载数据
         * @param array
         */
        public void addAlllist(PrettyGrilImage.ShowapiResBodyBean.PagebeanBean array) {
            if (array == null || array.contentlist.size() == 0) {
                return;
            }
            list.addAll(array.contentlist);
            notifyDataSetChanged();
        }

        /**
         * 清除所有数据
         */
        public void clearAllDate() {
            if (list != null) {
                list.clear();
                notifyDataSetChanged();
            }
        }
    }

}
