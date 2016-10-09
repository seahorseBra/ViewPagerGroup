package com.example.zchao.viewpagergroup;


import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import javabean.PrettyGrilImage;

/**
 * Created by zchao on 2016/9/28.
 */

public class PagerFragment extends Fragment {

    private static final String TAG = "PagerFragment";
    private ViewPager mViewPager;
    private PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean data;
    private OnPagerItemClick onPagerItemClick;
    private OnPagerItemLongClick onPagerItemLongClick;
    private DisplayImageOptions options;

    public PagerFragment() {

    }

    public void setOnPagerItemClick(OnPagerItemClick onPagerItemClick) {
        this.onPagerItemClick = onPagerItemClick;
    }

    public void setOnPagerItemLongClick(OnPagerItemLongClick onPagerItemLongClick) {
        this.onPagerItemLongClick = onPagerItemLongClick;
    }

    interface OnPagerItemClick {
        void onItemClick(int position);
    }
    interface OnPagerItemLongClick {
        void onItemLongClick(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(MainActivity.MyPagerAdapter.IMG_KEY);
            if (!TextUtils.isEmpty(string)) {
                Gson gson = new Gson();
                PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean jsonObject = gson.fromJson(string, PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean.class);
                if (jsonObject != null) {
                    data = jsonObject;
                }
            }
        }

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        MottoAdapter adapter = new MottoAdapter();
        mViewPager.setAdapter(adapter);
        if (data != null) {
            adapter.addAllList(data);
        }
    }

    class MottoAdapter extends PagerAdapter {
        ArrayList<PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ListBean> list = new ArrayList<>();
        AlphaAnimation showAnim;

        public MottoAdapter() {
            showAnim = new AlphaAnimation(0f, 1f);
            showAnim.setDuration(500);
            showAnim.setInterpolator(new AccelerateInterpolator());
        }

        public void addAllList(PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean array) {
            if (array == null) {
                return;
            }
            list.addAll(array.list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View root = LayoutInflater.from(getActivity()).inflate(R.layout.pager_item, container, false);
            final ImageView imgView = (ImageView) root.findViewById(R.id.img);
            final ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.progressbar);
            PrettyGrilImage.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ListBean listBean = list.get(position);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPagerItemClick != null) {
                        onPagerItemClick.onItemClick(position);
                    }
                }
            });
            imgView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onPagerItemLongClick != null) {
                        onPagerItemLongClick.onItemLongClick(position);
                    }
                    return false;
                }
            });

            //加载图片
            ImageLoader.getInstance().displayImage(listBean.big, imgView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    imgView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Toast.makeText(getContext(), "图片下载失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.INVISIBLE);
                    imgView.startAnimation(showAnim);
                    imgView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            container.addView(root);

            return root;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);

        }

        public int getResourceid(int position) {
            return getResources().getIdentifier("a" + (position + 1), "drawable", getActivity().getPackageName());
        }
    }
}
