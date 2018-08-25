package com.zeal4rea.doubanmoviedemo.gallery;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GalleryPresenter implements GalleryContract.Presenter {
    private DataRepository mDataRepository;
    private GalleryContract.View mGalleryView;
    private String mSubjectId;
    private int mType;
    private int mStart = 0;
    private int mTotal = -1;
    private CompositeDisposable mCompositeDisposable;

    public GalleryPresenter(DataRepository dataRepository, GalleryContract.View view, String subjectId, int type) {
        mDataRepository = dataRepository;
        mGalleryView = view;
        mSubjectId = subjectId;
        mType = type;
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
        mDataRepository
                .htmlGetPhotos(mSubjectId, mStart, mType)
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
                            mGalleryView.load(photos, more, mStart < mTotal);
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
