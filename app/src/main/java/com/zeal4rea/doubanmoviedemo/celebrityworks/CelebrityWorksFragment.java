package com.zeal4rea.doubanmoviedemo.celebrityworks;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWork;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.subjectdetail.SubjectDetailActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.HeaderAndFooterAdapterWrapper;

import java.util.List;

public class CelebrityWorksFragment extends BaseFragment implements CelebrityWorksContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final int loadThreshold = 5;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mLayoutEmpty;
    private View mLayoutError;
    private CelebrityWorksContract.Presenter mPresenter;
    private View mLayoutErrorClick;
    private ImageView mLayoutErrorImageView;
    private HeaderAndFooterAdapterWrapper mWrapperAdapter;
    private CelebrityWorksAdapter mInnerAdapter;
    private CelebrityWorksAdapter.OnItemClickListener mListener = new CelebrityWorksAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(CelebrityWork work) {
            SubjectDetailActivity.newIntent(getActivity(), work.id);
        }
    };
    private boolean loadComplete = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        String id = args.getString("id");
        int index = args.getInt("index", 0);

        new CelebrityWorksPresenter(this, BaseApplication.getDataRepository(), id, index);

        mRefreshLayout.setColorSchemeResources(R.color.googleBlue, R.color.googleGreen, R.color.googleRed, R.color.googleYellow);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRefreshLayout.isRefreshing() && !loadComplete && canLoadMore()) {
                    mPresenter.load(true);
                }
            }
        });
        mPresenter.subscribe();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_common_fragment_with_recyclerview, container, false);
        mRefreshLayout = view.findViewById(R.id.common_fragment_with_recyclerview$refresh_layout);
        mRecyclerView = view.findViewById(R.id.common_fragment_with_recyclerview$recycler_view);
        mLayoutEmpty = view.findViewById(R.id.common_fragment_with_recyclerview$layout_empty);
        mLayoutError = view.findViewById(R.id.common_fragment_with_recyclerview$layout_error);
        mLayoutErrorClick = mLayoutError.findViewById(R.id.error$layout_click);
        mLayoutErrorImageView = mLayoutError.findViewById(R.id.error$image_view);
        return view;
    }

    @Override
    public void load(List<CelebrityWorkWrapper> works, boolean add, boolean hasMore) {
        if (add && loadComplete) {
            return;
        } else {
            loadComplete = false;
        }

        int firstVisibleItemPosition = 0, offset = 0;
        if (add) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            View firstVisibleItem = layoutManager.findViewByPosition(firstVisibleItemPosition);
            if (firstVisibleItem != null) {
                offset = firstVisibleItem.getTop();
            }
        }

        setUpRecyclerView(works, add);

        if (!hasMore) {
            setNoMoreFooter();
        }

        if (add) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(firstVisibleItemPosition, offset);
        }
    }

    private void setUpRecyclerView(List<CelebrityWorkWrapper> works, boolean add) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
            mInnerAdapter = new CelebrityWorksAdapter(getActivity(), works, mListener);
        } else {
            mInnerAdapter.setData(works, add);
        }

        if (mWrapperAdapter == null) {
            mWrapperAdapter = new HeaderAndFooterAdapterWrapper(mInnerAdapter);
        } else {
            mWrapperAdapter.setInnerAdapter(mInnerAdapter);
        }

        mRecyclerView.setAdapter(mWrapperAdapter);
    }

    private void setNoMoreFooter() {
        loadComplete = true;
        int footersCount = mWrapperAdapter.getFootersCount();
        mWrapperAdapter.removeFooters();
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.layout_common_vertical_recyclerview_item_no_more, mRecyclerView, false);
        mWrapperAdapter.addFooterView(footer);
        if (footersCount > 0) {
            mWrapperAdapter.notifyItemChanged(mWrapperAdapter.getItemCount() - 1);
        } else {
            mWrapperAdapter.notifyItemRangeInserted(mWrapperAdapter.getHeadersCount() + mWrapperAdapter.getRealItemCount(), 1);
        }
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

    @Override
    public void displayEmptyPage() {
        mRecyclerView.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorPage() {
        mRecyclerView.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
        mLayoutErrorClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.load(false);
            }
        });
    }

    @Override
    public void loading(boolean loading) {
        if (mRefreshLayout.isRefreshing() == loading) {
            return;
        }
        mRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void setPresenter(CelebrityWorksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unSubscribe();
    }

    @Override
    public void onRefresh() {
        mPresenter.load(false);
    }
}
