package com.zeal4rea.doubanmoviedemo.gallerytabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class GalleryTabsActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallerytabs);
        Utils.setCustomDensity(this);
        Slidr.attach(this, new SlidrConfig.Builder().edge(true).build());

        mToolbar = findViewById(R.id.gallerytabs$toolbar);

        Bundle b = getIntent().getBundleExtra("b");
        String id = b.getString("id");
        String title = b.getString("title");
        int type = b.getInt("type");

        setUpActionbar(title);

        GalleryTabsFragment galleryTabsFragment = new GalleryTabsFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putInt("type", type);
        galleryTabsFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.gallerytabs$layout_content, galleryTabsFragment);
        fragmentTransaction.commit();
    }

    private void setUpActionbar(String title) {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(String.format(Utils.getString(R.string.photo_placeholder).toString(), title));
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void newIntent(Activity fromActivity, String id, String title, int type) {
        Intent intent = new Intent(fromActivity, GalleryTabsActivity.class);
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putString("title", title);
        b.putInt("type", type);
        intent.putExtra("b", b);
        fromActivity.startActivity(intent);
    }
}
