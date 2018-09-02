package com.zeal4rea.doubanmoviedemo.celebritydetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWork;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;
import com.zeal4rea.doubanmoviedemo.subjectdetail.SubjectDetailActivity;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.List;

public class CelebrityDetailActivity extends AppCompatActivity implements CelebrityDetailContract.View {

    private CelebrityDetailContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private View mLayoutBasicInfo;
    private View mLayoutPhotos;
    private View mLayoutWorks;
    private View mLayoutRelatedCelebrities;
    private View mLayoutErrorPage;
    private TextView mTextViewInfo;
    private TextView mTextViewIntro;
    private TextView mTextViewPhotoLabel;
    private TextView mTextViewCelebritiesLabel;
    private TextView mTextViewWorkLabel;
    private ImageView mImageViewCover;
    private RecyclerView mRecyclerViewPhotos;
    private RecyclerView mRecyclerViewCelebrities;
    private RecyclerView mRecyclerViewWorks;
    private TextView mTextViewNoPhoto;
    private TextView mTextViewNoCelebrity;
    private TextView mTextViewNoWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebritydetail);
        Utils.setCustomDensity(this);
        Slidr.attach(this, new SlidrConfig.Builder().edge(true).build());

        Bundle b = getIntent().getBundleExtra("b");
        String celebrityId = b.getString("celebrityId");

        new CelebrityDetailPresenter(this, BaseApplication.getDataRepository(), celebrityId);

        Toolbar mToolbar = findViewById(R.id.celebrity_detail$toolbar);
        mProgressBar = findViewById(R.id.celebrity_detail$progress_bar);

        mLayoutBasicInfo = findViewById(R.id.celebrity_detail$layout_basic_info);
        mLayoutPhotos = findViewById(R.id.celebrity_detail$layout_photos);
        mLayoutWorks = findViewById(R.id.celebrity_detail$layout_works);
        mLayoutRelatedCelebrities = findViewById(R.id.celebrity_detail$layout_related_celebrities);
        mLayoutErrorPage = findViewById(R.id.celebrity_detail$layout_error);

        mTextViewInfo = mLayoutBasicInfo.findViewById(R.id.celebrity_detail$text_view_info);
        mTextViewIntro = mLayoutBasicInfo.findViewById(R.id.celebrity_detail$text_view_intro);
        mImageViewCover = mLayoutBasicInfo.findViewById(R.id.celebrity_detail$image_view_cover);
        mTextViewPhotoLabel = mLayoutPhotos.findViewById(R.id.common_recyclerview$text_view_label);
        mTextViewCelebritiesLabel = mLayoutRelatedCelebrities.findViewById(R.id.common_recyclerview$text_view_label);
        mTextViewWorkLabel = mLayoutWorks.findViewById(R.id.common_recyclerview$text_view_label);

        mRecyclerViewPhotos = mLayoutPhotos.findViewById(R.id.common_recyclerview$recycler_view);
        mRecyclerViewCelebrities = mLayoutRelatedCelebrities.findViewById(R.id.common_recyclerview$recycler_view);
        mRecyclerViewWorks = mLayoutWorks.findViewById(R.id.common_recyclerview$recycler_view);

        mTextViewNoPhoto = mLayoutPhotos.findViewById(R.id.common_recyclerview$text_view_no_content);
        mTextViewNoCelebrity = mLayoutRelatedCelebrities.findViewById(R.id.common_recyclerview$text_view_no_content);
        mTextViewNoWork = mLayoutWorks.findViewById(R.id.common_recyclerview$text_view_no_content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextViewPhotoLabel.setText(Utils.getString(R.string.image));
        mTextViewCelebritiesLabel.setText(Utils.getString(R.string.related_celebrities));
        mTextViewWorkLabel.setText(Utils.getString(R.string.works));

        mPresenter.subscribe();
    }

    @Override
    public void setUpTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void displayBasicInfo(Celebrity celebrity) {
        setUpTitle(celebrity.name);
        mTextViewInfo.setText(celebrity.info);
        mTextViewIntro.setText(celebrity.intro);
        Glide.with(this).load(celebrity.avatar.normal).into(mImageViewCover);
        mLayoutBasicInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayPhotos(List<Photo> photos) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mRecyclerViewPhotos.getLayoutParams();
        lp.height = Utils.dp2px(100);
        mRecyclerViewPhotos.setLayoutParams(lp);
        mRecyclerViewPhotos.setHorizontalScrollBarEnabled(true);
        mRecyclerViewPhotos.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        mRecyclerViewPhotos.setHorizontalFadingEdgeEnabled(true);
        mRecyclerViewPhotos.setNestedScrollingEnabled(false);
        mRecyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PhotosAdapter photosAdapter = new PhotosAdapter(this, photos, new PhotosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<Photo> photos, int position) {
            }
        });
        mRecyclerViewPhotos.setAdapter(photosAdapter);

        mLayoutPhotos.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayWorks(List<CelebrityWork> works) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mRecyclerViewWorks.getLayoutParams();
        lp.height = Utils.dp2px(200);
        mRecyclerViewWorks.setLayoutParams(lp);
        mRecyclerViewWorks.setHorizontalScrollBarEnabled(true);
        mRecyclerViewWorks.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mRecyclerViewWorks.setNestedScrollingEnabled(false);
        mRecyclerViewWorks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        WorksAdapter worksAdapter = new WorksAdapter(this, works, new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CelebrityWork work) {
                Intent intent = new Intent(CelebrityDetailActivity.this, SubjectDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("subjectId", work.id);
                b.putString("subjectTitle", work.title);
                intent.putExtra("b", b);
                CelebrityDetailActivity.this.startActivity(intent);
            }
        });
        mRecyclerViewWorks.setAdapter(worksAdapter);

        mLayoutWorks.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayRelatedCelebrities(List<Celebrity.RelatedCelebrity> relatedCelebrities) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mRecyclerViewCelebrities.getLayoutParams();
        lp.height = Utils.dp2px(150);
        mRecyclerViewCelebrities.setLayoutParams(lp);
        mRecyclerViewCelebrities.setHorizontalScrollBarEnabled(true);
        mRecyclerViewCelebrities.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mRecyclerViewCelebrities.setNestedScrollingEnabled(false);
        mRecyclerViewCelebrities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RelatedCelebritiesAdapter celebritiesAdapter = new RelatedCelebritiesAdapter(this, relatedCelebrities, new RelatedCelebritiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Celebrity.RelatedCelebrity celebrity) {
                Intent intent = new Intent(CelebrityDetailActivity.this, CelebrityDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("celebrityId", celebrity.celebrity.id);
                intent.putExtra("b", b);
                CelebrityDetailActivity.this.startActivity(intent);
            }
        });
        mRecyclerViewCelebrities.setAdapter(celebritiesAdapter);

        mLayoutRelatedCelebrities.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoPhoto() {
        mRecyclerViewPhotos.setVisibility(View.GONE);
        mTextViewNoPhoto.setText(Utils.getString(R.string.no_image));
        mTextViewNoPhoto.setVisibility(View.VISIBLE);
        mLayoutPhotos.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoWork() {
        mRecyclerViewWorks.setVisibility(View.GONE);
        mTextViewNoWork.setText(Utils.getString(R.string.no_work));
        mTextViewNoWork.setVisibility(View.VISIBLE);
        mLayoutWorks.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayNoRelatedCelebrity() {
        mRecyclerViewCelebrities.setVisibility(View.GONE);
        mTextViewNoCelebrity.setText(Utils.getString(R.string.no_celebrity));
        mTextViewNoCelebrity.setVisibility(View.VISIBLE);
        mLayoutRelatedCelebrities.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayErrorPage() {
        mLayoutErrorPage.setVisibility(View.VISIBLE);
        mLayoutBasicInfo.setVisibility(View.GONE);
        mLayoutPhotos.setVisibility(View.GONE);
        mLayoutWorks.setVisibility(View.GONE);
        mLayoutRelatedCelebrities.setVisibility(View.GONE);
    }

    @Override
    public void loading(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPresenter(CelebrityDetailContract.Presenter presenter) {
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
}
