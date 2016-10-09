package com.example.zchao.viewpagergroup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by zchao on 2016/9/30.
 */

public class SplashActivity extends FragmentActivity {

    private ImageView splash;
    private long startTime;
    private static final long DELAY_TIME = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        splash = (ImageView) findViewById(R.id.splash_img);
        splash.setImageResource(R.drawable.splash_img);

        startTime = System.currentTimeMillis();
        asyncTask.execute();

    }

    AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            long endTime = System.currentTimeMillis();
            long delay = endTime - startTime;
            if (delay < DELAY_TIME) {
                try {
                    Thread.sleep(DELAY_TIME - delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    };
}
