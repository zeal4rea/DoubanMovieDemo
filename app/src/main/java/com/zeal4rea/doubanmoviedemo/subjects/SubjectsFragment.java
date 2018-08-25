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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.subjectdetail.SubjectDetailActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.HeaderAndFooterAdapterWrapper;

import java.util.List;

public class SubjectsFragment extends BaseFragment implements SubjectsContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SubjectsContract.Presenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mLayoutEmpty;
    private View mLayoutError;
    private SubjectsAdapter mInnerAdapter;
    private HeaderAndFooterAdapterWrapper mWrapperAdapter;
    private final int loadThreshold = 5;
    private boolean loadComplete = false;
    private SubjectItemListener mListener = new SubjectItemListener() {
        @Override
        public void onSubjectItemClick(Subject clickedSubject) {
            openSubjectDetail(clickedSubject);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.googleBlue, R.color.googleGreen, R.color.googleRed, R.color.googleYellow);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading() && canLoadMore() && !loadComplete) {
                    mPresenter.load(true);
                }
            }
        });
        mPresenter.subscribe();
    }

    private boolean canLoadMore() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int visibleChildCount = mRecyclerView.getChildCount();
        int itemCount = mWrapperAdapter.getRealItemCount();
        int firstVisibleItem = 0;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            firstVisibleItem = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
        } else if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return (firstVisibleItem + loadThreshold) >= (itemCount - visibleChildCount);
    }

    private boolean isLoading() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void load(List<Subject> subjects, boolean add, boolean hasMore) {
        if (add && loadComplete) {
            return;
        } else {
            loadComplete = false;
        }

        int firstVisibleItemPosition = 0;
        int offset = 0;
        if (add) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            View firstVisibleItem = layoutManager.findViewByPosition(firstVisibleItemPosition);
            if (firstVisibleItem != null) {
                offset = firstVisibleItem.getTop();
            }
        }

        setUpRecyclerView(subjects, add);
        if (!hasMore) {
            setNoMoreFooter();
        }
        if (add) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(firstVisibleItemPosition, offset);
        }
    }

    private void setUpRecyclerView(List<Subject> subjects, boolean add) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int px = Utils.dp2px(5);
                    outRect.left = px;
                    outRect.right = px;
                    outRect.bottom = px;
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = px;
                    }
                }
            });
        }
        if (mInnerAdapter == null) {
            mInnerAdapter = new SubjectsAdapter(getActivity(), subjects, mListener);
        } else {
            mInnerAdapter.setData(subjects, add);
        }
        if (mWrapperAdapter == null) {
            mWrapperAdapter = new HeaderAndFooterAdapterWrapper(mInnerAdapter);
        } else {
            mWrapperAdapter.setInnerAdapter(mInnerAdapter);
        }
        mRecyclerView.setAdapter(mWrapperAdapter);
    }

    @Override
    public void emptyRecyclerView() {
        if (mInnerAdapter != null && mWrapperAdapter != null && mInnerAdapter.getItemCount() != 0) {
            int itemCount = mWrapperAdapter.getItemCount();
            mInnerAdapter.clear();
            mWrapperAdapter.removeHeaders();
            mWrapperAdapter.removeFooters();
            mWrapperAdapter.notifyItemRangeRemoved(0, itemCount);
        }
    }

    public void setNoMoreFooter() {
        loadComplete = true;
        int footersCount = mWrapperAdapter.getFootersCount();
        if (footersCount > 0) {
            View footerAtPosition = mWrapperAdapter.getFooterAtPosition(0);
            Object tag = footerAtPosition.getTag();
            if (tag != null && tag.toString().equalsIgnoreCase("nomore")) {
                return;
            }
        }
        mWrapperAdapter.removeFooters();
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.layout_common_item_no_more, mRecyclerView, false);
        footer.setTag("nomore");
        mWrapperAdapter.addFooterView(footer);
        if (footersCount > 0) {
            mWrapperAdapter.notifyItemChanged(mWrapperAdapter.getItemCount() - 1);
        } else {
            mWrapperAdapter.notifyItemRangeInserted(mWrapperAdapter.getHeadersCount() + mWrapperAdapter.getRealItemCount(), 1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_subjects_fragment, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.subjects$swipe_refresh_layout);
        mRecyclerView = v.findViewById(R.id.subjects$recycler_view);
        mLayoutEmpty = v.findViewById(R.id.subjects$layout_empty);
        mLayoutError = v.findViewById(R.id.subjects$layout_error);
        return v;
    }

    @Override
    public void displayErrorPage() {
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayEmptyPage() {
        mLayoutError.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
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
        emptyRecyclerView();
        mPresenter.load(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unSubscribe();
    }

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

    public interface SubjectItemListener {
        void onSubjectItemClick(Subject clickedSubject);
    }
}
