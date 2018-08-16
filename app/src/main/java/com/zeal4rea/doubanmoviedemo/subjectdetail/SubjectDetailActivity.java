package com.zeal4rea.doubanmoviedemo.subjectdetail;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.data.remote.RemoteDataSource;
import com.zeal4rea.doubanmoviedemo.data.remote.jsoup.JsoupApi;
import com.zeal4rea.doubanmoviedemo.util.Utils;
import com.zeal4rea.doubanmoviedemo.util.view.HeaderAndFooterAdapterWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubjectDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private ImageView mCover;
    private NestedScrollView mContent;
    private View mLayoutPhotos;
    private View mLayoutCelebrities;
    private View mLayoutComments;
    private View mLayoutReviews;
    private RecyclerView mRecyclerViewPhotos;
    private RecyclerView mRecyclerViewComments;
    private RecyclerView mRecyclerViewCelebrities;
    private RecyclerView mRecyclerViewReviews;
    private TextView mTextViewPhotoLabel;
    private TextView mTextViewCommentLabel;
    private TextView mTextViewReviewLabel;
    private String subjectId;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Subject mSubject;
    //private Subject4J mSubject;
    private boolean loadFail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjectdetail);

        mToolbar = findViewById(R.id.subjectdetail$toolbar);
        mCover = findViewById(R.id.subjectdetail$image_view_cover);
        mProgressBar = findViewById(R.id.subjectdetail$progress_bar);
        mContent = findViewById(R.id.subjectdetail$content);

        mLayoutCelebrities = findViewById(R.id.subjectdetail_content$layout_celebrities);
        mLayoutPhotos = findViewById(R.id.subjectdetail_content$layout_photos);
        mLayoutComments = findViewById(R.id.subjectdetail_content$layout_comments);
        mLayoutReviews = findViewById(R.id.subjectdetail_content$layout_reviews);
        mRecyclerViewPhotos = findViewById(R.id.subjectdetail_content$recycler_view_photos);
        mRecyclerViewComments = findViewById(R.id.subjectdetail_content$recycler_view_comments);
        mRecyclerViewCelebrities = findViewById(R.id.subjectdetail_content$recycler_view_celebrities);
        mRecyclerViewReviews = findViewById(R.id.subjectdetail_content$recycler_view_reviews);
        mTextViewPhotoLabel = findViewById(R.id.subjectdetail_content$text_view_photo_label);
        mTextViewCommentLabel = findViewById(R.id.subjectdetail_content$text_view_comment_label);
        mTextViewReviewLabel = findViewById(R.id.subjectdetail_content$text_view_review_label);

        subjectId = getIntent().getStringExtra("subject_id");
        if (subjectId != null) {
            getSubjectData();
        }

        SlidrConfig config = new SlidrConfig.Builder()
                .edge(true)
                .build();
        Slidr.attach(this, config);
    }

    private void getSubjectData() {
        RemoteDataSource.getInstance()
                .apiGetSubjectDetail(subjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Subject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Subject subject) {
                        mSubject = subject;
                        showPage(mSubject);
                        List<Celebrity> celebrities = new ArrayList<>();
                        for (Celebrity c : mSubject.directors) {
                            c.role = "导演";
                            celebrities.add(c);
                        }
                        for (Celebrity c : mSubject.casts) {
                            c.role = "演员";
                            celebrities.add(c);
                        }
                        Utils.logWithDebugingTag("subjectdetail: " + mSubject.toString());
                        Utils.logWithDebugingTag("celebrities: " + new Gson().toJson(celebrities));
                        mRecyclerViewCelebrities.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        mRecyclerViewCelebrities.setAdapter(new CelebritiesAdapter(SubjectDetailActivity.this, celebrities));
                        mRecyclerViewCelebrities.setNestedScrollingEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadFail = true;
                        e.printStackTrace();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Snackbar.make(mContent, Utils.getString(R.string.load_fail), BaseTransientBottomBar.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SubjectDetailActivity.this, Utils.getString(R.string.load_fail), Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                        setUpActionBar();
                    }

                    @Override
                    public void onComplete() {
                        setUpActionBar();
                        mLayoutCelebrities.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

        RemoteDataSource.getInstance()
                .htmlGetPhotos(subjectId, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResult<Void, PhotoTemp>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CommonResult<Void, PhotoTemp> result) {
                        Utils.logWithDebugingTag("CommonResult<Object, PhotoTemp> result:" + result.toString());
                        List<Photo4J> photos = new ArrayList<>();
                        Pattern pattern = Pattern.compile("\\/(\\d+)\\?");
                        for (PhotoTemp p : result.results) {
                            Matcher matcher = pattern.matcher(p.url);
                            if (matcher.find()) {
                                photos.add(Photo4J.create(matcher.group(1)));
                            }
                        }
                        Utils.logWithDebugingTag("photos:" + photos.toString());
                        mRecyclerViewPhotos.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        PhotosAdapter innerAdapter = new PhotosAdapter(SubjectDetailActivity.this, photos);
                        HeaderAndFooterAdapterWrapper adapter = new HeaderAndFooterAdapterWrapper(innerAdapter);
                        View morePhotosFooter = LayoutInflater.from(SubjectDetailActivity.this).inflate(R.layout.layout_subjectdetail_content_photos_more, mRecyclerViewPhotos, false);
                        adapter.addFooterView(morePhotosFooter);
                        mRecyclerViewPhotos.setAdapter(adapter);
                        mRecyclerViewPhotos.setNestedScrollingEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLayoutPhotos.setVisibility(View.VISIBLE);
                    }
                });

        RemoteDataSource.getInstance()
                .htmlGetComments(subjectId, 0, JsoupApi.SORT_NEW_SCORE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Comment4J>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Comment4J> comments) {
                        Utils.logWithDebugingTag("comments:" + comments.toString());
                        mRecyclerViewComments.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        CommentsAdapter innerAdapter = new CommentsAdapter(SubjectDetailActivity.this, comments);
                        HeaderAndFooterAdapterWrapper adapter = new HeaderAndFooterAdapterWrapper(innerAdapter);
                        View moreCommentsFooter = LayoutInflater.from(SubjectDetailActivity.this).inflate(R.layout.layout_subjectdetail_content_comments_more, mRecyclerViewComments, false);
                        adapter.addFooterView(moreCommentsFooter);
                        mRecyclerViewComments.setAdapter(adapter);
                        mRecyclerViewComments.setNestedScrollingEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLayoutComments.setVisibility(View.VISIBLE);
                    }
                });

        RemoteDataSource.getInstance()
                .htmlGetReviews(subjectId, 0, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Review4J>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Review4J> reviews) {
                        Utils.logWithDebugingTag("reviews: " + reviews.toString());
                        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        ReviewsAdapter innerAdapter = new ReviewsAdapter(SubjectDetailActivity.this, reviews);
                        HeaderAndFooterAdapterWrapper adapter = new HeaderAndFooterAdapterWrapper(innerAdapter);
                        View moreReviewsFooter = LayoutInflater.from(SubjectDetailActivity.this).inflate(R.layout.layout_subjectdetail_content_reviews_more, mRecyclerViewReviews, false);
                        adapter.addFooterView(moreReviewsFooter);
                        mRecyclerViewReviews.setAdapter(adapter);
                        mRecyclerViewReviews.setNestedScrollingEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mLayoutReviews.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void showPage(Subject subject) {
        Glide.with(SubjectDetailActivity.this).load(subject.images.small).into(mCover);
        setLabel(subject.title);
        mContent.setVisibility(View.VISIBLE);
    }

    private void setLabel(String subjectName) {
        mTextViewPhotoLabel.setText(String.format(mTextViewPhotoLabel.getText().toString(), subjectName));
        mTextViewCommentLabel.setText(String.format(mTextViewCommentLabel.getText().toString(), subjectName));
        mTextViewReviewLabel.setText(String.format(mTextViewReviewLabel.getText().toString(), subjectName));
    }

    private void setUpActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (!loadFail) {
            getSupportActionBar().setTitle(mSubject.title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
