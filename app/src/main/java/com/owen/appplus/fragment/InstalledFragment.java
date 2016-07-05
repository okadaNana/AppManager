package com.owen.appplus.fragment;

import android.support.v4.app.Fragment;

import com.owen.appplus.bean.AppDataEngine;
import com.owen.appplus.bean.BeanApp;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Owen on 2016/6/30.
 */
public class InstalledFragment extends AppListFragment {

    @Override
    protected void createAppInfoList() {
        AppDataEngine.getInstalledApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BeanApp>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(true);
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {
                        mRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onNext(List<BeanApp> beanApps) {
                        mAdapter.setAppInfoList(beanApps);
                    }
                });
    }

    public static Fragment newInstance() {
        return new InstalledFragment();
    }
}
