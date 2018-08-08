package com.zeal4rea.doubanmoviedemo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeal4rea.doubanmoviedemo.MovieListAdapter;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.CommonResult;
import com.zeal4rea.doubanmoviedemo.bean.Subject;
import com.zeal4rea.doubanmoviedemo.network.Network;
import com.zeal4rea.doubanmoviedemo.network.api.OtherApi;

import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private CommonResult<Void, Subject> mResult;
    private Disposable disposable;
    private int type = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mSwipeRefreshLayout = v.findViewById(R.id.fragment_list$swipe_refresh_layout);
        mRecyclerView = v.findViewById(R.id.fragment_list$recycler_view);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        loadData(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private Observable<CommonResult<Void, Subject>> getObservable() {
        if (type == -1) {
            try {
                type = getArguments().getInt("type");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        OtherApi otherApi = Network.getOtherApi();
        Observable<CommonResult<Void, Subject>> observable = null;
        switch (type) {
            case 0:
                observable = otherApi.getInTheaters(null);
                break;
            case 1:
                observable = otherApi.getComingSoon(0, 20);
                break;
            case 2:
                observable = otherApi.getTop250();
                break;
            case 3:
                observable = otherApi.getWeekly();
                break;
            case 4:
                observable = otherApi.getUsBox();
                break;
            case 5:
                observable = otherApi.getNewMovies();
                break;
            default:
        }
        return observable;
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(boolean forceToReload) {
        mSwipeRefreshLayout.setRefreshing(true);
        if (mResult == null || forceToReload) {
            disposable = getObservable()
                    .doFinally(new Action() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CommonResult<Void, Subject>>() {
                        @Override
                        public void accept(CommonResult<Void, Subject> result) {
                            mResult = result;
                            setUpAdapterAndData(mResult);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            Log.d("lzl", "crash");
                            Toast.makeText(getContext(), "crash", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            setUpAdapterAndData(mResult);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setUpAdapterAndData(CommonResult<Void, Subject> result) {
        if (mAdapter == null) {
            mAdapter = new MovieListAdapter(getActivity(), Arrays.asList(result.results));
        } else {
            mAdapter.setData(Arrays.asList(result.results));
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }
}
