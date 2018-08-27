package com.zeal4rea.doubanmoviedemo.subjectdetail;

import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.PhotoTemp;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Subject;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.SubjectCelebrities;
import com.zeal4rea.doubanmoviedemo.data.DataRepository;
import com.zeal4rea.doubanmoviedemo.data.remote.rexxar.RexxarApi;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SubjectDetailPresenter implements SubjectDetailContract.Presenter {
    private SubjectDetailContract.View mView;
    private DataRepository mDataRepository;
    private String mSubjectId;
    private final CompositeDisposable mCompositeDisposable;

    SubjectDetailPresenter(SubjectDetailContract.View view, DataRepository dataRepository, String subjectId) {
        mView = view;
        mDataRepository = dataRepository;
        mSubjectId = subjectId;
        mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void load() {
        mDataRepository
                .rexxarGetSubjectInfo(mSubjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Subject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Subject subject) {
                        if (subject != null) {
                            mView.displayBasicInfo(subject);
                        } else {
                            mView.displayErrorPage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.loading(false);
                    }

                    @Override
                    public void onComplete() {
                        mView.loading(false);
                    }
                });

        mDataRepository
                .rexxarGetSubjectCelebrities(mSubjectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SubjectCelebrities>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SubjectCelebrities subjectCelebrities) {
                        List<com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity> celebrities = new ArrayList<>();
                        if (subjectCelebrities != null) {
                            for (SubjectCelebrities.CelebritiesWrapper wrapper : subjectCelebrities.credits) {
                                String role = wrapper.title;
                                for (com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity celebrity : wrapper.celebrities) {
                                    celebrity.role = role;
                                    celebrities.add(celebrity);
                                }
                            }
                            mView.displayCelebrities(celebrities);
                        } else {
                            mView.displayNoCelebrities();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

        mDataRepository
                .htmlGetPhotos(mSubjectId, 0, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResult<Void, PhotoTemp>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(CommonResult<Void, PhotoTemp> result) {
                        Utils.logWithDebugingTag("CommonResult<Object, PhotoTemp> result:" + result.toString());
                        List<Photo4J> photos = new ArrayList<>();
                        Pattern pattern = Pattern.compile("/(\\d+)\\?");
                        for (PhotoTemp p : result.results) {
                            Matcher matcher = pattern.matcher(p.url);
                            if (matcher.find()) {
                                photos.add(Photo4J.create(matcher.group(1)));
                            }
                        }
                        if (photos.isEmpty()) {
                            mView.displayNoPhotos();
                        } else {
                            mView.displayPhotos(photos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

        mDataRepository
                .rexxarGetSubjectComments(mSubjectId, RexxarApi.SORT_COMMENT_HOT, 0, 5)
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
                            mView.displayComments(new ArrayList<>(Arrays.asList(result.results)));
                        } else {
                            mView.displayNoComments();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

        mDataRepository
                .htmlGetReviews(mSubjectId, 0, 5)
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
                            mView.displayReviews(reviews);
                        } else {
                            mView.displayNoReviews();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
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
