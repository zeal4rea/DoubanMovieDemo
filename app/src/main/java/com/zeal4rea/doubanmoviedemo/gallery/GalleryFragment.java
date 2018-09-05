package com.zeal4rea.doubanmoviedemo.gallery;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseFragment;
import com.zeal4rea.doubanmoviedemo.image.ImageActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.HeaderAndFooterAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends BaseFragment implements GalleryContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mInnerAdapter;
    private HeaderAndFooterAdapterWrapper mWrapperAdapter;
    private GalleryContract.Presenter mPresenter;
    private View mLayoutEmpty;
    private View mLayoutError;
    private View mLayoutErrorClick;
    private ImageView mLayoutErrorImageView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        String id = args.getString("id");
        int type = args.getInt("type");
        int index = args.getInt("index");

        new GalleryPresenter(BaseApplication.getDataRepository(), this, id, type, index);

        mRefreshLayout.setColorSchemeResources(R.color.googleBlue, R.color.googleGreen, R.color.googleRed, R.color.googleYellow);
        mRefreshLayout.setOnRefreshListener(this);
        mPresenter.subscribe();
    }

    @Override
    public void load(List<String> photos, boolean add, boolean hasMore) {
        int firstVisibleItemPosition = 0;
        int offset = 0;
        if (add) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            View firstVisibleItem = gridLayoutManager.findViewByPosition(firstVisibleItemPosition);
            if (firstVisibleItem != null) {
                offset = firstVisibleItem.getTop();
            }
        }

        setUpRecyclerView(photos, add);

        if (hasMore) {
            setLoadMoreFooter();
        } else {
            setNoMoreFooter();
        }

        if (add) {
            ((GridLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(firstVisibleItemPosition, offset);
        }
    }

    private void setUpRecyclerView(List<String> photos, boolean add) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int px = Utils.dp2px(5);
                    outRect.bottom = px;
                    outRect.right = px;
                    outRect.left = px;
                    outRect.top = px;
                }
            });
        }
        if (mInnerAdapter == null) {
            mInnerAdapter = new GalleryAdapter(getActivity(), photos, new GalleryAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(List<String> photos, int position) {
                    ImageActivity.newIntent(getActivity(), (ArrayList<String>) photos, position);
                }
            });
        } else {
            mInnerAdapter.setData(photos, add);
        }
        if (mWrapperAdapter == null) {
            mWrapperAdapter = new HeaderAndFooterAdapterWrapper(mInnerAdapter);
        } else {
            mWrapperAdapter.setInnerAdapter(mInnerAdapter);
        }
        mRecyclerView.setAdapter(mWrapperAdapter);
    }

    private void setLoadMoreFooter() {
        int footersCount = mWrapperAdapter.getFootersCount();
        mWrapperAdapter.removeFooters();
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.layout_gallery_item_more, mRecyclerView, false);
        TextView textViewMore = footer.findViewById(R.id.gallery$text_view_more);
        textViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "加载更多", Toast.LENGTH_SHORT).show();
                mPresenter.load(true);
            }
        });
        mWrapperAdapter.addFooterView(footer);
        if (footersCount > 0) {
            mWrapperAdapter.notifyItemChanged(mWrapperAdapter.getItemCount() - 1);
        } else {
            mWrapperAdapter.notifyItemRangeInserted(mWrapperAdapter.getHeadersCount() + mWrapperAdapter.getRealItemCount(), 1);
        }
    }

    private void setNoMoreFooter() {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
    public void displayEmptyPage() {
        Utils.logWithDebugingTag(getClass().getSimpleName() + ":displayEmptyPage");
        mLayoutEmpty.setVisibility(View.VISIBLE);
        mLayoutError.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void displayErrorPage() {
        Utils.logWithDebugingTag(getClass().getSimpleName() + ":displayErrorPage");
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
        mRefreshLayout.setVisibility(View.GONE);
        mLayoutErrorClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 动画
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
    public void onRefresh() {
        mPresenter.load(false);
    }

    @Override
    public void setPresenter(GalleryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.unSubscribe();
    }
}
