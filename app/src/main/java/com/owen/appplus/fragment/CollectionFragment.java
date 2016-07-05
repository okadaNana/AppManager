package com.owen.appplus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.owen.appplus.R;
import com.owen.appplus.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Owen on 2016/6/30.
 */
public class CollectionFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static Fragment newInstance() {
        return new CollectionFragment();
    }
}
