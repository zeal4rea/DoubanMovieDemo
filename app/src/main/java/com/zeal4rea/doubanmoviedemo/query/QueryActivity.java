package com.zeal4rea.doubanmoviedemo.query;

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
import android.view.LayoutInflater;
import android.view.View;

import com.r0adkll.slidr.Slidr;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.subjectdetail.SubjectDetailActivity;
import com.zeal4rea.doubanmoviedemo.subjects.SubjectsAdapter;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.HeaderAndFooterAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

public class QueryActivity extends AppCompatActivity implements QueryContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mEmptyPage;
    private View mErrorPage;
    private QueryContract.Presenter mPresenter;
    private SubjectsAdapter mInnerAdapter;
    private static final int loadThreshold = 3;
    private boolean loadComplete = false;
    private HeaderAndFooterAdapterWrapper mWrapperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        Utils.setCustomDensity(this);
        Slidr.attach(this);

        Bundle b = getIntent().getBundleExtra("b");
        String q = b.getString("q");
        String tag = b.getString("tag");

        new QueryPresenter(this, BaseApplication.getDataRepository(), q, tag);

        Toolbar toolbar = findViewById(R.id.query$toobar);
        mRefreshLayout = findViewById(R.id.query$swipe_refresh_layout);
        mRecyclerView = findViewById(R.id.query$recycler_view);
        mEmptyPage = findViewById(R.id.query$empty_page);
        mErrorPage = findViewById(R.id.query$error_page);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int dp = Utils.dp2px(5);
                    outRect.left = dp;
                    outRect.right = dp;
                    outRect.bottom = dp;
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = dp;
                    }
                }
            });
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRefreshLayout.isRefreshing() && canLoadMore() && !loadComplete) {
                    mPresenter.query(true);
                }
            }
        });
        mInnerAdapter = new SubjectsAdapter(this, new ArrayList<Subject>(), new SubjectsAdapter.SubjectItemListener() {
            @Override
            public void onSubjectItemClick(Subject clickedSubject) {
                SubjectDetailActivity.newIntent(QueryActivity.this, clickedSubject.id);
            }
        });
        mWrapperAdapter = new HeaderAndFooterAdapterWrapper(mInnerAdapter);
        mRecyclerView.setAdapter(mWrapperAdapter);

        mPresenter.subscribe();
    }

    @Override
    public void load(List<Subject> subjects, boolean add, boolean hasMore) {
        if (add && loadComplete) {
            return;
        } else {
            loadComplete = false;
        }
        mInnerAdapter.setData(subjects, add);
        if (add) {
            mWrapperAdapter.notifyDataSetChanged();
        } else {
            int positionStart = mWrapperAdapter.getHeadersCount() + mWrapperAdapter.getRealItemCount();
            mWrapperAdapter.notifyItemRangeInserted(positionStart, subjects.size());
        }
        if (!hasMore) {
            setNoMoreFooter();
        }
    }

    private void setNoMoreFooter() {
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
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_common_vertical_recyclerview_item_no_more, mRecyclerView, false);
        footer.setTag("nomore");
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
    public void setTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public void displayEmptyPage() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorPage.setVisibility(View.GONE);
        mEmptyPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorPage() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyPage.setVisibility(View.GONE);
        mErrorPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void loading(boolean loading) {
        mRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void setPresenter(QueryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRefresh() {
        mPresenter.query(false);
    }

    public static void newIntent(Activity fromActivity, String query, String tag) {
        Intent intent = new Intent(fromActivity, QueryActivity.class);
        Bundle b = new Bundle();
        b.putString("q", query);
        b.putString("tag", tag);
        intent.putExtra("b", b);
        fromActivity.startActivity(intent);
    }
}
