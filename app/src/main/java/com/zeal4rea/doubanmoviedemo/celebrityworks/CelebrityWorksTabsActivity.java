package com.zeal4rea.doubanmoviedemo.celebrityworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.main.CommonTabsPagerAdapter;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class CelebrityWorksTabsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_fragment_container);
        Utils.setCustomDensity(this);
        Slidr.attach(this, new SlidrConfig.Builder().edge(true).build());

        Bundle b = getIntent().getBundleExtra("b");
        String id = b.getString("id");
        String title = b.getString("title");

        Toolbar toolbar = findViewById(R.id.common_fragment_container$toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(String.format(Utils.getString(R.string.work_placeholder).toString(), title));
        }

        CelebrityWorksTabsFragment celebrityWorksTabsFragment = new CelebrityWorksTabsFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        celebrityWorksTabsFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.common_fragment_container$layout_content, celebrityWorksTabsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void newIntent(Activity fromActivity, String id, String title) {
        Intent intent = new Intent(fromActivity, CelebrityWorksTabsActivity.class);
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putString("title", title);
        intent.putExtra("b", b);
        fromActivity.startActivity(intent);
    }

    public static class CelebrityWorksTabsFragment extends BaseFragment {

        private ViewPager mViewPager;
        private TabLayout mTabLayout;
        private ProgressBar mProgressBar;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Bundle arguments = getArguments();
            String id = arguments.getString("id");
            CommonTabsPagerAdapter pagerAdapter = new CommonTabsPagerAdapter(getActivity().getSupportFragmentManager());
            mViewPager.setAdapter(pagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            SparseArray<String> titles = Utils.getSparseArraySplitBy(Utils.getStringArray(R.array.celebrity_work_list_order), "=");

            for (int i = 0; i < 2; i++) {
                CelebrityWorksFragment fragment = new CelebrityWorksFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                args.putInt("index", i);
                fragment.setArguments(args);
                pagerAdapter.addFragment(fragment, titles.get(i));
            }
            pagerAdapter.notifyDataSetChanged();

            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View view = inflater.inflate(R.layout.layout_common_tabs_fragment, container, false);
            mViewPager = view.findViewById(R.id.common_tabs_fragment$view_pager);
            mTabLayout = view.findViewById(R.id.common_tabs_fragment$tab_layout);
            mProgressBar = view.findViewById(R.id.common_tabs_fragment$progress_bar);
            return view;
        }
    }
}
