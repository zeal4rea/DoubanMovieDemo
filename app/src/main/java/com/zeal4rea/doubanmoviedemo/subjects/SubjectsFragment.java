package com.zeal4rea.doubanmoviedemo.subjects;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.subjectdetail.SubjectDetailActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.Arrays;

public class SubjectsFragment extends BaseFragment implements SubjectsContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SubjectsContract.Presenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SubjectsAdapter mAdapter;
    private SubjectItemListener mListener = new SubjectItemListener() {
        @Override
        public void onSubjectItemClick(Subject clickedSubject) {
            mPresenter.openSubjectDetail(clickedSubject);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_page_fragment, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.main_page_fragment$swipe_refresh_layout);
        mRecyclerView = v.findViewById(R.id.main_page_fragment$recycler_view);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.googleBlue, R.color.googleGreen, R.color.googleRed, R.color.googleYellow);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mPresenter.subscribe();
    }

    @Override
    public void displaySubjects(@NonNull CommonResult<Void, Subject> result, boolean refresh) {
        if (mAdapter == null) {
            mAdapter = new SubjectsAdapter(getActivity(), Arrays.asList(result.results), mListener);
        } else {
            mAdapter.setData(Arrays.asList(result.results), !refresh);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int px = Utils.dp2px(BaseApplication.getContext(), 5);
                    outRect.left = px;
                    outRect.right = px;
                    outRect.bottom = px;
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = px;
                    }
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void openSubjectDetail(Subject subject) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Snackbar.make(mRecyclerView, subject.title, BaseTransientBottomBar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), subject.title, Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(getActivity(), SubjectDetailActivity.class);
        intent.putExtra("subject_id", subject.id);
        getActivity().startActivity(intent);
    }

    @Override
    public void displayErrorPage() {
        //todo
    }

    @Override
    public void displayEmptyPage() {
        //todo
    }

    @Override
    public void loading(boolean loading) {
        mSwipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void setPresenter(SubjectsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRefresh() {
        mPresenter.loadMore();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unSubscribe();
    }

    public interface SubjectItemListener {
        void onSubjectItemClick(Subject clickedSubject);
    }
}
