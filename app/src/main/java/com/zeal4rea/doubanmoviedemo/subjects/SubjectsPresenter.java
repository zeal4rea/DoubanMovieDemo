package com.zeal4rea.doubanmoviedemo.subjects;

import android.support.annotation.NonNull;
import android.util.Log;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.data.SubjectsType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SubjectsPresenter implements SubjectsContract.Presenter {
    private final CompositeDisposable mCompositeDisposable;
    private DataRepository mDataRepository;
    private SubjectsContract.View mSubjectsView;
    private SubjectsType mType;

    public SubjectsPresenter(@NonNull DataRepository dataRepository, @NonNull SubjectsContract.View subjectsView, SubjectsType type) {
        mDataRepository = dataRepository;
        mSubjectsView = subjectsView;
        mType = type;
        mSubjectsView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void openSubjectDetail(Subject subject) {
        mSubjectsView.openSubjectDetail(subject);
    }

    @Override
    public void refreshSubjects() {
        mSubjectsView.loading(true);
        mCompositeDisposable.clear();
        Disposable disposable = mDataRepository
                .apiGetSubjects(mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonResult<Void, Subject>>() {
                    @Override
                    public void accept(CommonResult<Void, Subject> result) throws Exception {
                        if (result.results != null && result.results.length > 0) {
                            mSubjectsView.displaySubjects(result, true);
                        } else {
                            mSubjectsView.displayEmptyPage();
                        }
                        mSubjectsView.loading(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Log.d(BaseContants.APPLICATION_TAG, "crash!!!!!!!!!" + throwable.getMessage());
                        mSubjectsView.displayErrorPage();
                        mSubjectsView.loading(false);
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadMore() {
        //todo
    }

    @Override
    public void subscribe() {
        refreshSubjects();
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }
}
