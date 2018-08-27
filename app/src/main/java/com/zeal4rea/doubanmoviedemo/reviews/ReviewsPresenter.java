package com.zeal4rea.doubanmoviedemo.reviews;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReviewsPresenter implements ReviewsContract.Presenter {
    private final ReviewsContract.View mReviewsView;
    private final DataRepository mDataRepository;
    private String mSubjectId;
    private final CompositeDisposable mCompositeDisposable;
    private int mStart = 0;
    private int mTotal = -1;

    ReviewsPresenter(ReviewsContract.View view, DataRepository dataRepository, String subjectId) {
        mReviewsView = view;
        mDataRepository = dataRepository;
        mSubjectId = subjectId;
        mReviewsView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void load(final boolean more) {
        if (!more) {
            mStart = 0;
            mReviewsView.emptyRecyclerView();
        }
        mReviewsView.loading(true);
        mCompositeDisposable.clear();
        mDataRepository
                .htmlGetReviews(mSubjectId, mStart, BaseContants.DEFAULT_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Review4J>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Review4J> reviews) {
                        if (reviews != null && !reviews.isEmpty()) {
                            mStart += reviews.size();
                            mReviewsView.fillData(reviews, more, true);
                        } else {
                            if (!more) {
                                mReviewsView.displayEmptyPage();
                            } else {
                                mReviewsView.fillData(new ArrayList<Review4J>(), true, false);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mReviewsView.loading(false);
                        mReviewsView.displayErrorPage();
                    }

                    @Override
                    public void onComplete() {
                        mReviewsView.loading(false);
                    }
                });
    }

    @Override
    public void subscribe() {
        load(false);
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }
}
