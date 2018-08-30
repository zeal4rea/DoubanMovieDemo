package com.zeal4rea.doubanmoviedemo.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.subjects.SubjectsFragment;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class MainTabsFragment extends BaseFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CommonTabsPagerAdapter mPagerAdapter = new CommonTabsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        SparseArray<String> list = Utils.getSparseArraySplitBy(getResources().getStringArray(R.array.movie_list_order), "=");
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i)))
                continue;
            SubjectsFragment fragment = new SubjectsFragment();
            Bundle args = new Bundle();
            args.putInt("index", i);
            fragment.setArguments(args);
            mPagerAdapter.addFragment(fragment, list.get(i));
        }

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("index", 0);
            mViewPager.setCurrentItem(index);
        }
        mPagerAdapter.notifyDataSetChanged();
        mTabLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.layout_main_tabs_fragment, container, false);
        mViewPager = v.findViewById(R.id.main_tabs_fragment$view_pager);
        mTabLayout = v.findViewById(R.id.main_tabs_fragment$tab_layout);
        mProgressBar = v.findViewById(R.id.main_tabs_fragment$progress_bar);
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", mViewPager.getCurrentItem());
    }
}
