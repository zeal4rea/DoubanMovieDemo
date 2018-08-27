package com.zeal4rea.doubanmoviedemo.celebritydetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class CelebrityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebritydetail);
        Utils.setCustomDensity(this);
    }
}
