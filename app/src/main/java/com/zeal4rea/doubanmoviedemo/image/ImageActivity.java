package com.zeal4rea.doubanmoviedemo.image;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;

import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private ViewPager mViewPager;
    private List<Photo4J> photos;

    private Animation fadeIn = new AlphaAnimation(0, 1);
    private Animation fadeOut = new AlphaAnimation(1, 0);
    private Toolbar mToolbar;
    private boolean mVisible;
    private GestureDetector mDetector;

    {
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(150);

        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(150);
    }

    private Handler mHandler = new Handler();
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    };
    private Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.show();
            }

            mToolbar.startAnimation(fadeIn);
        }
    };
    private Runnable mAutoHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mToolbar = findViewById(R.id.image$toolbar);
        mViewPager = findViewById(R.id.image$view_pager);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                toggle();
                return true;
            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        mToolbar.startAnimation(fadeOut);
        mVisible = false;
        mHideRunnable.run();

        Bundle b = getIntent().getBundleExtra("b");
        photos = b.getParcelableArrayList("photos");

        ImageAdapter imageAdapter = new ImageAdapter(this, photos);
        mViewPager.setAdapter(imageAdapter);
        mViewPager.setCurrentItem(b.getInt("position", 0));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateTitle();
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        updateTitle();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
            if (AUTO_HIDE) {
                delayHide(AUTO_HIDE_DELAY);
            }
        }
    }

    private void delayHide(int hideDelay) {
        mHandler.removeCallbacks(mAutoHideRunnable);
        mHandler.postDelayed(mAutoHideRunnable, hideDelay);
    }

    private void show() {
        mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        mVisible = true;

        mHandler.removeCallbacks(mHideRunnable);
        mHandler.postDelayed(mShowRunnable, UI_ANIMATION_DELAY);
    }

    private void hide() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        mToolbar.startAnimation(fadeOut);

        mVisible = false;

        mHandler.removeCallbacks(mShowRunnable);
        mHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
    }

    private void updateTitle() {
        if (photos != null && !photos.isEmpty()) {
            getSupportActionBar().setTitle((mViewPager.getCurrentItem() + 1) + "/" + photos.size());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
