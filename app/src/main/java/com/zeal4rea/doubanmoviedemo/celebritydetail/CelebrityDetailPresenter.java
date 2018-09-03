package com.zeal4rea.doubanmoviedemo.celebritydetail;

import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CelebrityDetailPresenter implements CelebrityDetailContract.Presenter {
    private final CompositeDisposable mCompositeDisposable;
    private CelebrityDetailContract.View mView;
    private DataRepository mDataRepository;
    private String mCelebrityId;

    CelebrityDetailPresenter(CelebrityDetailContract.View view, DataRepository dataRepository, String celebrityId) {
        mView = view;
        mDataRepository = dataRepository;
        mCelebrityId = celebrityId;
        mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void load() {
        mView.loading(true);
        mDataRepository
                .rexxarGetCelebrityDetail(mCelebrityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Celebrity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Celebrity celebrity) {
                        if (celebrity != null) {
                            mView.displayBasicInfo(celebrity);

                            if (celebrity.album != null && celebrity.album.total > 0 && celebrity.album.photos.length > 0) {
                                mView.displayPhotos(new ArrayList<>(Arrays.asList(celebrity.album.photos)), celebrity.album.total > celebrity.album.photos.length);
                            } else {
                                mView.displayNoPhoto();
                            }

                            if (celebrity.works_count > 0) {
                                mView.displayWorks(new ArrayList<>(Arrays.asList(celebrity.latest_works)));
                            } else {
                                mView.displayNoWork();
                            }

                            if (celebrity.related_celebrities != null && celebrity.related_celebrities.length > 0) {
                                mView.displayRelatedCelebrities(new ArrayList<>(Arrays.asList(celebrity.related_celebrities)));
                            } else {
                                mView.displayNoRelatedCelebrity();
                            }
                        } else {
                            mView.displayErrorPage();
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
        load();
    }

    @Override
    public void unSubscribe() {
        mCompositeDisposable.clear();
    }
}
