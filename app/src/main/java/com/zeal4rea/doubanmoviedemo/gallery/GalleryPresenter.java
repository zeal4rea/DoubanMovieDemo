package com.zeal4rea.doubanmoviedemo.gallery;

import com.zeal4rea.doubanmoviedemo.base.BaseContants;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GalleryPresenter implements GalleryContract.Presenter {

    private DataRepository mDataRepository;
    private GalleryContract.View mGalleryView;
    private String mId;
    private int mType;
    private int mIndex;
    private int mStart = 0;
    private int mTotal = -1;
    private CompositeDisposable mCompositeDisposable;

    public GalleryPresenter(DataRepository dataRepository, GalleryContract.View view, String id, int type, int index) {
        mDataRepository = dataRepository;
        mGalleryView = view;
        mId = id;
        mType = type;
        mIndex = index;
        mGalleryView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void load(final boolean more) {
        if (!more) {
            mStart = 0;
        }
        if (mTotal != -1 && mStart >= mTotal) {
            mGalleryView.load(null, more, false);
            return;
        }
        mGalleryView.loading(true);
        mCompositeDisposable.clear();
        switch (mType) {
            case BaseContants.TYPE_GALLERY_SUBJECT:
                mDataRepository
                        .htmlGetPhotos(mId, mStart, mIndex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommonResult<Void, PhotoTemp>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mCompositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(CommonResult<Void, PhotoTemp> result) {
                                if (result != null && result.results != null && result.results.length > 0) {
                                    List<Photo4J> photos = new ArrayList<>();
                                    for (PhotoTemp p : result.results) {
                                        String[] arr = Utils.regexMatch("\\/(\\d+)\\?", p.url);
                                        photos.add(Photo4J.create(arr[1]));
                                    }
                                    Utils.logWithDebugingTag("photos:" + photos.toString());
                                    mStart += photos.size();
                                    mTotal = result.total;
                                    mGalleryView.load(Utils.extractPhotoUrl(photos), more, mStart < mTotal);
                                } else {
                                    if (more) {
                                        mGalleryView.load(null, true, false);
                                    } else {
                                        mGalleryView.displayEmptyPage();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                mGalleryView.displayErrorPage();
                                mGalleryView.loading(false);
                            }

                            @Override
                            public void onComplete() {
                                mGalleryView.loading(false);
                            }
                        });
                break;
            case BaseContants.TYPE_GALLERY_CELEBRITY:
                mDataRepository
                        .rexxarGetCelebrityPhotos(mId, mStart, BaseContants.DEFAULT_COUNT_GALLERY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CommonResult<Void, Photo>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mCompositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(CommonResult<Void, Photo> result) {
                                if (result != null && result.results != null && result.results.length > 0) {
                                    List<Photo> photos = new ArrayList<>(Arrays.asList(result.results));
                                    Utils.logWithDebugingTag("photos:" + photos.toString());
                                    mStart += photos.size();
                                    mTotal = result.total;
                                    mGalleryView.load(Utils.extractPhotoUrl(photos), more, mStart < mTotal);
                                } else {
                                    if (more) {
                                        mGalleryView.load(null, true, false);
                                    } else {
                                        mGalleryView.displayEmptyPage();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                mGalleryView.displayErrorPage();
                                mGalleryView.loading(false);
                            }

                            @Override
                            public void onComplete() {
                                mGalleryView.loading(false);
                            }
                        });
                break;
        }
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
