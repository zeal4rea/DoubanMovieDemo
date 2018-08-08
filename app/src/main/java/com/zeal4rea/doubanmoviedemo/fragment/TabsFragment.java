package com.zeal4rea.doubanmoviedemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.ViewPagerAdapter;
import com.zeal4rea.doubanmoviedemo.util.Utils;

public class TabsFragment extends BaseFragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;
    private ViewPagerAdapter mPagerAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        SparseArray<String> list = Utils.getSparseArraySplitBy(getResources().getStringArray(R.array.movie_list), "=");
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.isEmpty(list.get(i)))
                continue;
            ListFragment fragment = new ListFragment();
            Bundle args = new Bundle();
            args.putInt("type", i);
            fragment.setArguments(args);
            mPagerAdapter.addFragment(fragment, list.get(i));
        }

        /*for (int i = 0; i < 10; i++) {
            TestFragment fragment = new TestFragment();
            Bundle bundle = new Bundle();
            bundle.putString("text", "this is fragment" + i);
            fragment.setArguments(bundle);
            mPagerAdapter.addFragment(fragment, "fragment" + i);
        }*/
        mPagerAdapter.notifyDataSetChanged();
        mTabLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_tabs, container, false);
        mViewPager = v.findViewById(R.id.layout_tabs$view_pager);
        mTabLayout = v.findViewById(R.id.layout_tabs$tab_layout);
        mProgressBar = v.findViewById(R.id.layout_tabs$progress_bar);
        return v;
    }
}
