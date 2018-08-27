package com.zeal4rea.doubanmoviedemo.comments;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.data.remote.rexxar.RexxarApi;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentsPresenter implements CommentsContract.Presenter {
    private CompositeDisposable mCompositeDisposable;
    private CommentsContract.View mCommentsView;
    private DataRepository mDataRepository;
    private String mSubjectId;
    private int mStart = 0;
    private int mTotal = -1;

    public CommentsPresenter(CommentsContract.View view, DataRepository dataRepository, String subjectId) {
        mCommentsView = view;
        mDataRepository = dataRepository;
        mSubjectId = subjectId;
        mCommentsView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }
    @Override
    public void load(final boolean more) {
        if (!more) {
            mStart = 0;
            mCommentsView.emptyRecyclerView();
        }
        if (mTotal != -1 && mStart >= mTotal) {
            return;
        }
        mCommentsView.loading(true);
        mCompositeDisposable.clear();
        mDataRepository
                .rexxarGetSubjectComments(mSubjectId, RexxarApi.SORT_COMMENT_HOT, mStart, BaseContants.DEFAULT_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResult<Void, Comment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CommonResult<Void, Comment> result) {
                        if (result != null && result.results != null && result.results.length > 0) {
                            mStart += result.results.length;
                            mTotal = result.total;
                            mCommentsView.fillData(new ArrayList<>(Arrays.asList(result.results)), more, mStart < mTotal);
                        } else {
                            if (!more) {
                                mCommentsView.displayEmptyPage();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mCommentsView.loading(false);
                        mCommentsView.displayErrorPage();
                    }

                    @Override
                    public void onComplete() {
                        mCommentsView.loading(false);
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
