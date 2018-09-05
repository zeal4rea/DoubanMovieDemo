package com.zeal4rea.doubanmoviedemo.celebrityworks;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CelebrityWorksPresenter implements CelebrityWorksContract.Presenter {
    private final CompositeDisposable mCompositeDisposable;
    private CelebrityWorksContract.View mView;
    private DataRepository mDataRepository;
    private String mId;
    private int mIndex;
    private int mStart;
    private int mTotal;

    public CelebrityWorksPresenter(CelebrityWorksContract.View view, DataRepository dataRepository, String id, int index) {
        mView = view;
        mDataRepository = dataRepository;
        mId = id;
        mIndex = index;
        mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void load(final boolean more) {
        if (!more) {
            mStart = 0;
        } else if (mTotal != -1 && mStart >= mTotal) {
            mView.load(null, true, false);
            return;
        }
        mView.loading(true);
        mCompositeDisposable.clear();
        mDataRepository
                .rexxarGetCelebrityWorks(mId, BaseContants.SORT_CELEBRITY_WORK[mIndex], mStart, mTotal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResult<Void, CelebrityWorkWrapper>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CommonResult<Void, CelebrityWorkWrapper> result) {
                        if (result != null && result.results != null && result.results.length > 0) {
                            Utils.logWithDebugingTag("celebrityWorks" + new ArrayList<>(Arrays.asList(result.results)).toString());
                            mStart += result.results.length;
                            mTotal = result.total;
                            mView.load(new ArrayList<>(Arrays.asList(result.results)), more, mStart < mTotal);
                        } else {
                            if (more) {
                                mView.load(null, true, false);
                            } else {
                                mView.displayEmptyPage();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.displayErrorPage();
                        mView.loading(false);
                    }

                    @Override
                    public void onComplete() {
                        mView.loading(false);
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
