package com.zeal4rea.doubanmoviedemo.subjectdetail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject;
import com.zeal4rea.doubanmoviedemo.celebritydetail.CelebrityDetailActivity;
import com.zeal4rea.doubanmoviedemo.comments.CommentsActivity;
import com.zeal4rea.doubanmoviedemo.gallerytabs.GalleryTabsActivity;
import com.zeal4rea.doubanmoviedemo.image.ImageActivity;
import com.zeal4rea.doubanmoviedemo.reviews.ReviewsActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.HeaderAndFooterAdapterWrapper;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetailActivity extends AppCompatActivity implements SubjectDetailContract.View {

    private View mLayoutBasicInfo;
    private View mLayoutPhotos;
    private View mLayoutCelebrities;
    private View mLayoutComments;
    private View mLayoutReviews;
    private View mLayoutError;
    private RecyclerView mRecyclerViewPhotos;
    private RecyclerView mRecyclerViewComments;
    private RecyclerView mRecyclerViewCelebrities;
    private RecyclerView mRecyclerViewReviews;
    private NestedScrollView mContent;
    private LinearLayout mLayoutStars;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mCover;
    private TextView mTextViewCelebrityLabel;
    private TextView mTextViewPhotoLabel;
    private TextView mTextViewCommentLabel;
    private TextView mTextViewReviewLabel;
    private TextView mTextViewMeta;
    private TextView mTextViewSummary;
    private TextView mTextViewNoSummary;
    private TextView mTextViewNoCelebrities;
    private TextView mTextViewNoPhotos;
    private TextView mTextViewNoComments;
    private TextView mTextViewNoReviews;
    private TextView mTextViewNoRating;
    private TextView mTextViewSummaryLabel;
    private TextView mTextViewRatingCount;
    private TextView mTextViewRating;
    private SubjectDetailContract.Presenter mPresenter;
    private String subjectId;
    private Subject mSubject;
    private boolean loadFail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjectdetail);

        Window window = getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .build();
        Slidr.attach(this, config);
        Utils.setCustomDensity(this);

        subjectId = getIntent().getStringExtra("subject_id");
        new SubjectDetailPresenter(this, BaseApplication.getDataRepository(), subjectId);

        mToolbar = findViewById(R.id.subjectdetail$toolbar);
        mCover = findViewById(R.id.subjectdetail$image_view_cover);
        mProgressBar = findViewById(R.id.subjectdetail$progress_bar);
        mContent = findViewById(R.id.subjectdetail$content);
        mCollapsingToolbarLayout = findViewById(R.id.subjectdetail$collapsing_toolbar_layout);

        mLayoutStars = findViewById(R.id.subjectdetail_content$linear_layout_stars);
        mTextViewRating = findViewById(R.id.subjectdetail_content$text_view_rating);
        mTextViewRatingCount = findViewById(R.id.subjectdetail_content$text_view_rating_count);
        mTextViewMeta = findViewById(R.id.subjectdetail_content$text_view_meta);
        mTextViewSummary = findViewById(R.id.subjectdetail_content$text_view_summary);

        mLayoutBasicInfo = findViewById(R.id.subjectdetail_content$layout_basic_info);
        mLayoutCelebrities = findViewById(R.id.subjectdetail_content$layout_celebrities);
        mLayoutPhotos = findViewById(R.id.subjectdetail_content$layout_photos);
        mLayoutComments = findViewById(R.id.subjectdetail_content$layout_comments);
        mLayoutReviews = findViewById(R.id.subjectdetail_content$layout_reviews);
        mLayoutError = findViewById(R.id.subjectdetail_content$layout_error);

        mRecyclerViewCelebrities = mLayoutCelebrities.findViewById(R.id.common_recyclerview$recycler_view);
        mRecyclerViewPhotos = mLayoutPhotos.findViewById(R.id.common_recyclerview$recycler_view);
        mRecyclerViewComments = mLayoutComments.findViewById(R.id.common_recyclerview$recycler_view);
        mRecyclerViewReviews = mLayoutReviews.findViewById(R.id.common_recyclerview$recycler_view);

        mTextViewSummaryLabel = findViewById(R.id.subjectdetail_content$text_view_summary_label);
        mTextViewCelebrityLabel = mLayoutCelebrities.findViewById(R.id.common_recyclerview$text_view_label);
        mTextViewPhotoLabel = mLayoutPhotos.findViewById(R.id.common_recyclerview$text_view_label);
        mTextViewCommentLabel = mLayoutComments.findViewById(R.id.common_recyclerview$text_view_label);
        mTextViewReviewLabel = mLayoutReviews.findViewById(R.id.common_recyclerview$text_view_label);

        mTextViewNoSummary = findViewById(R.id.subjectdetail_content$text_view_no_summary);
        mTextViewNoRating = findViewById(R.id.subjectdetail_content$text_view_no_rating);
        mTextViewNoCelebrities = mLayoutCelebrities.findViewById(R.id.common_recyclerview$text_view_no_content);
        mTextViewNoPhotos = mLayoutPhotos.findViewById(R.id.common_recyclerview$text_view_no_content);
        mTextViewNoComments = mLayoutComments.findViewById(R.id.common_recyclerview$text_view_no_content);
        mTextViewNoReviews = mLayoutReviews.findViewById(R.id.common_recyclerview$text_view_no_content);

        mPresenter.subscribe();
    }

    @Override
    public void displayBasicInfo(Subject subject) {
        mSubject = subject;
        showPage(mSubject);
        Utils.logWithDebugingTag("subjectdetail: " + mSubject.toString());
        setUpActionBar();
        mProgressBar.setVisibility(View.GONE);
    }

    private void showPage(Subject subject) {
        if (!Utils.isTextEmpty(subject.header_bg_color)) {
            //mCover.setBackgroundColor(Color.parseColor("#" + subject.header_bg_color));
            mCollapsingToolbarLayout.setContentScrimColor(Color.parseColor("#" + subject.header_bg_color));
        }
        Glide.with(SubjectDetailActivity.this).load(subject.cover_img.url).into(mCover);
        setLabel(subject.title);
        if (subject.extra.rating_group.rating != null) {
            RatingAndStars.fillStars(this, mLayoutStars, RatingAndStars.correctRating(subject.extra.rating_group.rating.value, (int) subject.extra.rating_group.rating.max), 14);
            mTextViewRating.setText(String.valueOf(subject.extra.rating_group.rating.value));
            mTextViewRatingCount.setText(String.format(mTextViewRatingCount.getText().toString(), subject.extra.rating_group.rating.count));
        } else {
            mTextViewNoRating.setVisibility(View.VISIBLE);
            mTextViewRating.setVisibility(View.GONE);
            mLayoutStars.setVisibility(View.GONE);
            mTextViewRatingCount.setVisibility(View.GONE);
        }
        mTextViewMeta.setText(subject.extra.short_info);
        try {
            mTextViewSummary.setText(Jsoup.parse(subject.desc).select("div.content").first().text());
        } catch (Exception e) {
            e.printStackTrace();
            mTextViewSummary.setVisibility(View.GONE);
            mTextViewNoSummary.setVisibility(View.VISIBLE);
        }
        mLayoutBasicInfo.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.VISIBLE);
    }

    private void setLabel(String subjectName) {
        mTextViewCelebrityLabel.setText(Utils.getString(R.string.celebrity));
        mTextViewSummaryLabel.setText(String.format(Utils.getString(R.string.summary_placeholder).toString(), subjectName));
        mTextViewPhotoLabel.setText(String.format(Utils.getString(R.string.photo_placeholder).toString(), subjectName));
        mTextViewCommentLabel.setText(String.format(Utils.getString(R.string.comment_placeholder).toString(), subjectName));
        mTextViewReviewLabel.setText(String.format(Utils.getString(R.string.review_placeholder).toString(), subjectName));
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!loadFail) {
                actionBar.setTitle(mSubject.title);
            }
        }
    }

    @Override
    public void displayCelebrities(List<Celebrity> celebrities) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mRecyclerViewCelebrities.getLayoutParams();
        lp.height = Utils.dp2px(150);
        mRecyclerViewCelebrities.setLayoutParams(lp);
        mRecyclerViewCelebrities.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewCelebrities.setHorizontalScrollBarEnabled(true);
        mRecyclerViewCelebrities.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mRecyclerViewCelebrities.setNestedScrollingEnabled(false);
        CelebritiesAdapter celebritiesAdapter = new CelebritiesAdapter(SubjectDetailActivity.this, celebrities, new CelebritiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Celebrity celebrity) {
                Intent intent = new Intent(SubjectDetailActivity.this, CelebrityDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("celebrityId", celebrity.id);
                intent.putExtra("b", b);
                SubjectDetailActivity.this.startActivity(intent);
            }
        });
        mRecyclerViewCelebrities.setAdapter(celebritiesAdapter);
        mLayoutCelebrities.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayPhotos(List<Photo4J> photos) {
        Utils.logWithDebugingTag("photos:" + photos.toString());
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mRecyclerViewPhotos.getLayoutParams();
        lp.height = Utils.dp2px(100);
        mRecyclerViewPhotos.setLayoutParams(lp);
        mRecyclerViewPhotos.setHorizontalScrollBarEnabled(true);
        mRecyclerViewPhotos.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        mRecyclerViewPhotos.setHorizontalFadingEdgeEnabled(true);
        mRecyclerViewPhotos.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewPhotos.setNestedScrollingEnabled(false);
        PhotosAdapter innerAdapter = new PhotosAdapter(SubjectDetailActivity.this, photos, new PhotosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<Photo4J> photos, int position) {
                Intent intent = new Intent(SubjectDetailActivity.this, ImageActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("photos", new ArrayList<Parcelable>(photos));
                b.putInt("position", position);
                intent.putExtra("b", b);
                SubjectDetailActivity.this.startActivity(intent);
            }
        });
        HeaderAndFooterAdapterWrapper adapter = new HeaderAndFooterAdapterWrapper(innerAdapter);
        View morePhotosFooter = LayoutInflater.from(SubjectDetailActivity.this).inflate(R.layout.layout_subjectdetail_content_photos_more, mRecyclerViewPhotos, false);
        morePhotosFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubjectDetailActivity.this, GalleryTabsActivity.class);
                i.putExtra("subjectId", subjectId);
                i.putExtra("subjectTitle", mSubject.title);
                SubjectDetailActivity.this.startActivity(i);
            }
        });
        adapter.addFooterView(morePhotosFooter);
        mRecyclerViewPhotos.setAdapter(adapter);
        mLayoutPhotos.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayComments(List<Comment> comments) {
        Utils.logWithDebugingTag("comments:" + comments.toString());
        mRecyclerViewComments.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewComments.setNestedScrollingEnabled(false);
        CommentsAdapter innerAdapter = new CommentsAdapter(SubjectDetailActivity.this, comments);
        HeaderAndFooterAdapterWrapper adapter = new HeaderAndFooterAdapterWrapper(innerAdapter);
        View moreCommentsFooter = LayoutInflater.from(SubjectDetailActivity.this).inflate(R.layout.layout_subjectdetail_content_comments_more, mRecyclerViewComments, false);
        moreCommentsFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubjectDetailActivity.this, CommentsActivity.class);
                Bundle b = new Bundle();
                b.putString("subjectId", mSubject.id);
                b.putString("subjectTitle", mSubject.title);
                intent.putExtra("b", b);
                SubjectDetailActivity.this.startActivity(intent);
            }
        });
        adapter.addFooterView(moreCommentsFooter);
        mRecyclerViewComments.setAdapter(adapter);
        mLayoutComments.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayReviews(List<Review4J> reviews) {
        Utils.logWithDebugingTag("reviews: " + reviews.toString());
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewReviews.setNestedScrollingEnabled(false);
        ReviewsAdapter innerAdapter = new ReviewsAdapter(SubjectDetailActivity.this, reviews);
        HeaderAndFooterAdapterWrapper adapter = new HeaderAndFooterAdapterWrapper(innerAdapter);
        View moreReviewsFooter = LayoutInflater.from(SubjectDetailActivity.this).inflate(R.layout.layout_subjectdetail_content_reviews_more, mRecyclerViewReviews, false);
        moreReviewsFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubjectDetailActivity.this, ReviewsActivity.class);
                Bundle b = new Bundle();
                b.putString("subjectId", subjectId);
                b.putString("subjectTitle", mSubject.title);
                intent.putExtra("b", b);
                SubjectDetailActivity.this.startActivity(intent);
            }
        });
        adapter.addFooterView(moreReviewsFooter);
        mRecyclerViewReviews.setAdapter(adapter);
        mLayoutReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoCelebrities() {
        mRecyclerViewCelebrities.setVisibility(View.GONE);
        mTextViewNoCelebrities.setText(Utils.getString(R.string.no_celebrity));
        mTextViewNoCelebrities.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoPhotos() {
        mRecyclerViewPhotos.setVisibility(View.GONE);
        mTextViewNoPhotos.setText(Utils.getString(R.string.no_photo));
        mTextViewNoPhotos.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoComments() {
        mRecyclerViewComments.setVisibility(View.GONE);
        mTextViewNoComments.setText(Utils.getString(R.string.no_comment));
        mTextViewNoComments.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoReviews() {
        mRecyclerViewReviews.setVisibility(View.GONE);
        mTextViewNoReviews.setText(Utils.getString(R.string.no_review));
        mTextViewNoReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorPage() {
        mLayoutBasicInfo.setVisibility(View.GONE);
        mLayoutCelebrities.setVisibility(View.GONE);
        mLayoutPhotos.setVisibility(View.GONE);
        mLayoutComments.setVisibility(View.GONE);
        mLayoutReviews.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
        loadFail = true;
    }

    @Override
    public void loading(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPresenter(SubjectDetailContract.Presenter presenter) {
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
}
