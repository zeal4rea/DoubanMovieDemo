package com.zeal4rea.doubanmoviedemo.query;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QueryPresenter implements QueryContract.Presenter {
    private final CompositeDisposable mCompositeDisposable;
    private QueryContract.View mView;
    private DataRepository mDataRepository;
    private final Map<String, String> queryMap;
    private int mStart = 0;
    private int mTotal = -1;

    QueryPresenter(QueryContract.View view, DataRepository dataRepository, String q, String tag) {
        mView = view;
        mDataRepository = dataRepository;
        mView.setPresenter(this);
        queryMap = new HashMap<>();
        if (!Utils.isTextEmpty(q)) {
            queryMap.put("q", q);
        }
        if (!Utils.isTextEmpty(tag)) {
            queryMap.put("tag", tag);
        }
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void query(final boolean more) {
        if (!more) {
            mStart = 0;
        } else if (mTotal != -1 && mStart >= mTotal){
            mView.load(null, true, false);
            return;
        }
        queryMap.put("start", mStart + "");
        queryMap.put("count", BaseContants.DEFAULT_COUNT + "");
        mView.loading(true);
        mDataRepository
                .apiQuerySubjects(queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResult<Void, Subject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CommonResult<Void, Subject> result) {
                        if (result != null) {
                            if (!more) {
                                mView.setTitle(result.title);
                            }
                            if (result.results != null && result.results.length > 0) {
                                mStart += result.results.length;
                                mTotal = result.total;
                                mView.load(new ArrayList<>(Arrays.asList(result.results)), more, mTotal > mStart);
                            } else {
                                if (more) {
                                    mView.load(null, true, false);
                                } else {
                                    mView.displayEmptyPage();
                                }
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
        query(false);
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }
}
