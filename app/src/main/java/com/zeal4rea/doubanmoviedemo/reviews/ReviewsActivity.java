package com.zeal4rea.doubanmoviedemo.reviews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.r0adkll.slidr.Slidr;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.subjectdetail.ReviewsAdapter;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements ReviewsContract.View {

    private ReviewsContract.Presenter mPresenter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerViewReviews;
    private View mLayoutEmpty;
    private View mLayoutError;
    private ReviewsAdapter mAdapter;
    private boolean loadComplete = false;
    private final static int loadThreshold = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Utils.setCustomDensity(this);
        Slidr.attach(this);

        Bundle b = getIntent().getBundleExtra("b");
        String subjectId = b.getString("subjectId");
        String subjectTitle = b.getString("subjectTitle");

        new ReviewsPresenter(this, BaseApplication.getDataRepository(), subjectId);

        Toolbar toolbar = findViewById(R.id.reviews$toolbar);
        mRefreshLayout = findViewById(R.id.reviews$swipe_refresh_layout);
        mRecyclerViewReviews = findViewById(R.id.reviews$recycler_view);
        mLayoutEmpty = findViewById(R.id.reviews$empty);
        mLayoutError = findViewById(R.id.reviews$error);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpTitle(subjectTitle);

        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (mRecyclerViewReviews.getItemDecorationCount() == 0) {
            mRecyclerViewReviews.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = Utils.dp2px(15);
                    }
                }
            });
        }
        mRecyclerViewReviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!mRefreshLayout.isRefreshing() && canLoadMore() && !loadComplete) {
                    mPresenter.load(true);
                }
            }
        });
        mAdapter = new ReviewsAdapter(this, new ArrayList<Review4J>());
        mRecyclerViewReviews.setAdapter(mAdapter);

        mPresenter.subscribe();
    }

    @Override
    public void fillData(List<Review4J> reviews, boolean add, boolean hasMore) {
        if (add && loadComplete) {
            return;
        } else {
            loadComplete = false;
        }

        mAdapter.setData(reviews, add);

        mLayoutError.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
        mRecyclerViewReviews.setVisibility(View.VISIBLE);
    }

    private boolean canLoadMore() {
        RecyclerView.LayoutManager layoutManager = mRecyclerViewReviews.getLayoutManager();
        int visibleChildCount = mRecyclerViewReviews.getChildCount();
        int itemCount = mAdapter.getItemCount();
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

    private void setUpTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(Utils.getString(R.string.review_placeholder).toString(), title));
        }
    }

    @Override
    public void emptyRecyclerView() {
        mAdapter.clear();
    }

    @Override
    public void displayEmptyPage() {
        loadComplete = true;
        mRecyclerViewReviews.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorPage() {
        loadComplete = true;
        mRecyclerViewReviews.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
    }

    @Override
    public void loading(boolean loading) {
        mRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void setPresenter(ReviewsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    public static void newIntent(Activity fromActivity, String subjectId, String subjectTitle) {
        Intent intent = new Intent(fromActivity, ReviewsActivity.class);
        Bundle b = new Bundle();
        b.putString("subjectId", subjectId);
        b.putString("subjectTitle", subjectTitle);
        intent.putExtra("b", b);
        fromActivity.startActivity(intent);
    }
}
