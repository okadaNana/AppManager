package com.owen.appplus.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.owen.appplus.R;
import com.owen.appplus.activity.AppDetailActivity;
import com.owen.appplus.adapter.AppListAdapter;
import com.owen.appplus.base.BaseFragment;
import com.owen.appplus.bean.BeanApp;
import com.owen.appplus.utils.FileUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by mike on 16/7/1.
 */
public abstract class AppListFragment extends BaseFragment implements AppListAdapter.onItemOptionMenuClickListener, AppListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    AppListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new AppListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        mRecyclerView.setHasFixedSize(true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemOptionMenuClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        createAppInfoList();
    }

    protected abstract void createAppInfoList();

    @Override
    public void onRefresh() {
        createAppInfoList();
    }

    @Override
    public void onItemClick(BeanApp beanApp) {
        AppDetailActivity.actionStart(getActivity(), beanApp);
    }

    @Override
    public void onItemOptionMenuClick(int itemId, BeanApp beanApp) {
        switch (itemId) {
            case R.id.pop_share:
                Toast.makeText(getContext(), "asdfasdf", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pop_export:
                export(beanApp);
                break;
            case R.id.pop_detail:
                Toast.makeText(getContext(), "asdfasdf", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void export(BeanApp beanApp) {
        if (!FileUtil.isSDCardMounted()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage("SD卡不存在")
                    .setPositiveButton("确定", null)
                    .show();

            return;
        }

        final File srcFile = new File(beanApp.getSrcPath());
        if (!srcFile.exists()) {
            Snackbar.make(getActivity().getWindow().getDecorView(), "导出失败，请检查安装包是否存在", Snackbar.LENGTH_SHORT).show();
            return;
        }

        File targetDir = new File(FileUtil.getSDCardPath(), getString(R.string.app_name));
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        String apkFileName = beanApp.getName().concat("_").concat(beanApp.getVersionName()).concat(".apk");
        final File apkFile = new File(targetDir, apkFileName);
        if (apkFile.exists()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage( String.format(getString(R.string.apk_file_exists), apkFileName, targetDir.getParentFile().getAbsolutePath()))
                    .setPositiveButton(R.string.not_override, null)
                    .setNegativeButton(R.string.override, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            writeToDisk();
                        }
                    })
                    .setNeutralButton(R.string.view_file, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            browseFile();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage(String.format(getString(R.string.are_sure_want_to_export), apkFileName, targetDir.getParentFile().getAbsolutePath()))
                    .setPositiveButton("确定导出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileChannel inputChannel = null;
                            FileChannel outputChannel = null;
                            try {
                                inputChannel = new FileInputStream(srcFile).getChannel();
                                outputChannel = new FileOutputStream(apkFile).getChannel();
                                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (inputChannel != null) {
                                    try {
                                        inputChannel.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (outputChannel != null) {
                                    try {
                                        outputChannel.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

}
