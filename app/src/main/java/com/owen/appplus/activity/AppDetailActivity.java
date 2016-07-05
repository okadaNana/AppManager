package com.owen.appplus.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owen.appplus.R;
import com.owen.appplus.base.BaseActivity;
import com.owen.appplus.bean.BeanApp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mike on 16/7/2.
 */
public class AppDetailActivity extends BaseActivity {

    private static final String EXTRA_BEAN_APP = "com.owen.appplus.AppDetailActivity.bean_app";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.iv_app_icon)
    ImageView mIvAppIcon;
    @Bind(R.id.tv_app_name)
    TextView mTvAppName;
    @Bind(R.id.tv_app_version_name)
    TextView mTvAppVersionName;
    @Bind(R.id.app_package_name)
    TextView mTvAppPackageName;
    @Bind(R.id.tv_app_version_code)
    TextView mTvAppVersionCode;

    private BeanApp mBeanApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        ButterKnife.bind(this);

        mToolbar.setTitle(R.string.empty);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBeanApp = getIntent().getParcelableExtra(EXTRA_BEAN_APP);

        mIvAppIcon.setImageBitmap(mBeanApp.getIcon());
        mTvAppName.setText(mBeanApp.getName());
        mTvAppPackageName.setText(mBeanApp.getPackageName());
        mTvAppVersionName.setText(mBeanApp.getVersionName());
        mTvAppVersionCode.setText(String.format(getString(R.string.version_code), mBeanApp.getVersionCode()));
    }

    @OnClick(R.id.tv_share)
    public void shareApk() {
        Toast.makeText(AppDetailActivity.this, "asdfasdf", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_export)
    public void exportToLocalDisk() {
        Toast.makeText(AppDetailActivity.this, "asdfasdf", Toast.LENGTH_SHORT).show();
    }

    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    @OnClick(R.id.tv_more_detail)
    public void moreDetailOfApp() {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Uri uri = Uri.fromParts(SCHEME, mBeanApp.getPackageName(), null);
            intent.setData(uri);
        } else {
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(appPkgName, mBeanApp.getPackageName());
        }
        startActivity(intent);
    }

    @OnClick(R.id.tv_more_operation)
    public void moreOperation() {
        new AlertDialog.Builder(this).
                setItems(R.array.more_action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentOpenApp = AppDetailActivity.this.getPackageManager().getLaunchIntentForPackage(mBeanApp.getPackageName());
                                startActivity(intentOpenApp);
                                break;
                            case 1:
                                Intent intentUninstall = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                                intentUninstall.setData(Uri.parse("package:" + mBeanApp.getPackageName()));
                                intentUninstall.putExtra(Intent.EXTRA_RETURN_RESULT,true);
                                startActivity(intentUninstall);
                                break;
                            case 2:
                                Uri uri = Uri.parse("market://details?id=" + mBeanApp.getPackageName());
                                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context, BeanApp beanApp) {
        Intent intent = new Intent(context, AppDetailActivity.class);
        intent.putExtra(EXTRA_BEAN_APP, beanApp);
        context.startActivity(intent);
    }
}
