package com.zeal4rea.doubanmoviedemo.subjects;

import android.support.annotation.NonNull;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.data.SubjectsType;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubjectsPresenter implements SubjectsContract.Presenter {
    private final CompositeDisposable mCompositeDisposable;
    private DataRepository mDataRepository;
    private SubjectsContract.View mSubjectsView;
    private SubjectsType mType;
    private int mStart = 0;
    private int mTotal = -1;

    SubjectsPresenter(@NonNull DataRepository dataRepository, @NonNull SubjectsContract.View subjectsView, SubjectsType type) {
        mDataRepository = dataRepository;
        mSubjectsView = subjectsView;
        mType = type;
        mSubjectsView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void load(final boolean more) {
        if (!more) {
            mStart = 0;
        }
        if (mTotal != -1 && mStart >= mTotal) {
            mSubjectsView.load(null, more, false);
            return;
        }
        mSubjectsView.loading(true);
        mCompositeDisposable.clear();
        mDataRepository
                .apiGetSubjects(mType, mStart, BaseContants.DEFAULT_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResult<Void, Subject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CommonResult<Void, Subject> result) {
                        if (result != null && result.results != null && result.results.length > 0) {
                            mStart += result.results.length;
                            mTotal = result.total;
                            mSubjectsView.load(new ArrayList<>(Arrays.asList(result.results)), more, mStart < mTotal);
                        } else {
                            if (more) {
                                mSubjectsView.load(null, true, false);
                            } else {
                                mSubjectsView.displayEmptyPage();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mSubjectsView.displayErrorPage();
                        mSubjectsView.loading(false);
                    }

                    @Override
                    public void onComplete() {
                        mSubjectsView.loading(false);
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
