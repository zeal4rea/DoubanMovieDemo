package com.zeal4rea.doubanmoviedemo.gallerytabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
        String subjectId = getIntent().getStringExtra("subjectId");
        String subjectTitle = getIntent().getStringExtra("subjectTitle");

        setUpActionbar(subjectTitle);

        GalleryTabsFragment galleryTabsFragment = new GalleryTabsFragment();
        new GalleryTabsPresenter(galleryTabsFragment);
        Bundle b = new Bundle();
        b.putString("subjectId", subjectId);
        galleryTabsFragment.setArguments(b);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.gallerytabs$layout_content, galleryTabsFragment);
        fragmentTransaction.commit();
    }

    private void setUpActionbar(String title) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(String.format(Utils.getString(R.string.photo_placeholder).toString(), title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
