package com.zeal4rea.doubanmoviedemo.gallerytabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.gallery.GalleryFragment;
import com.zeal4rea.doubanmoviedemo.main.CommonTabsPagerAdapter;

public class GalleryTabsFragment extends BaseFragment implements GalleryTabsContract.View {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;
    private String id;
    private CommonTabsPagerAdapter mPagerAdapter;
    private GalleryTabsContract.Presenter mPresenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        id = args.getString("id");
        int type = args.getInt("type");
        new GalleryTabsPresenter(this, type);
        mPagerAdapter = new CommonTabsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mPresenter.subscribe();
    }

    @Override
    public void addFragments(SparseArray<String> titles, int type) {
        for (int i = 0; i < titles.size(); i++) {
            GalleryFragment fragment = new GalleryFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            args.putInt("type", type);
            args.putInt("index", i);
            fragment.setArguments(args);
            mPagerAdapter.addFragment(fragment, titles.get(i));
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void initComplete() {
        mProgressBar.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorPage() {
        mProgressBar.setVisibility(View.GONE);
        //TODO
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_gallery_tabs_content, container, false);
        mViewPager = view.findViewById(R.id.gallerytabs$view_pager);
        mTabLayout = view.findViewById(R.id.gallerytabs$tab_layout);
        mProgressBar = view.findViewById(R.id.gallerytabs$progress_bar);
        return view;
    }

    @Override
    public void setPresenter(GalleryTabsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unSubscribe();
    }
}
