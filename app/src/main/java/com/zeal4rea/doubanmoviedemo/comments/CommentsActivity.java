package com.zeal4rea.doubanmoviedemo.comments;

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
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.subjectdetail.CommentsAdapter;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements CommentsContract.View, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerViewComments;
    private SwipeRefreshLayout mRefreshLayout;
    private CommentsAdapter mAdapter;
    private CommentsContract.Presenter mPresenter;
    private View mLayoutEmpty;
    private View mLayoutError;
    private boolean loadComplete = false;
    private static final int loadThreshold = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Utils.setCustomDensity(this);
        Slidr.attach(this);

        Bundle b = getIntent().getBundleExtra("b");
        String subjectId = b.getString("subjectId");
        String subjectTitle = b.getString("subjectTitle");
        new CommentsPresenter(this, BaseApplication.getDataRepository(), subjectId);

        Toolbar toolbar = findViewById(R.id.comments$toolbar);
        mRefreshLayout = findViewById(R.id.comments$swipe_refresh_layout);
        mRecyclerViewComments = findViewById(R.id.comments$recycler_view);
        mLayoutEmpty = findViewById(R.id.comments$empty);
        mLayoutError = findViewById(R.id.comments$error);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpTitle(subjectTitle);

        mRefreshLayout.setColorSchemeResources(R.color.googleBlue, R.color.googleGreen, R.color.googleRed, R.color.googleYellow);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new CommentsAdapter(this, new ArrayList<Comment>());
        mRecyclerViewComments.setAdapter(mAdapter);
        if (mRecyclerViewComments.getItemDecorationCount() == 0) {
            mRecyclerViewComments.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = Utils.dp2px(15);
                    }
                }
            });
        }
        mRecyclerViewComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!mRefreshLayout.isRefreshing() && canLoadMore() && !loadComplete) {
                    mPresenter.load(true);
                }
            }
        });

        mPresenter.subscribe();
    }

    @Override
    public void setUpTitle(String subjectTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(Utils.getString(R.string.comment_placeholder).toString(), subjectTitle));
        }
    }

    @Override
    public void loading(boolean loading) {
        mRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void fillData(List<Comment> comments, boolean add, boolean hasMore) {
        if (add && loadComplete) {
            return;
        } else {
            loadComplete = false;
        }

        mAdapter.setData(comments, add);

        mRecyclerViewComments.setVisibility(View.VISIBLE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
    }

    private boolean canLoadMore() {
        RecyclerView.LayoutManager layoutManager = mRecyclerViewComments.getLayoutManager();
        int visibleChildCount = mRecyclerViewComments.getChildCount();
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

    @Override
    public void emptyRecyclerView() {
        mAdapter.clear();
    }

    @Override
    public void displayErrorPage() {
        loadComplete = true;
        mRecyclerViewComments.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayEmptyPage() {
        loadComplete = true;
        mRecyclerViewComments.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(CommentsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    @Override
    public void onRefresh() {
        mPresenter.load(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static void newIntent(Activity fromActivity, String subjectId, String subjectTitle) {
        Intent intent = new Intent(fromActivity, CommentsActivity.class);
        Bundle b = new Bundle();
        b.putString("subjectId", subjectId);
        b.putString("subjectTitle", subjectTitle);
        intent.putExtra("b", b);
        fromActivity.startActivity(intent);
    }
}
