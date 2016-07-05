package com.owen.appplus.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.owen.appplus.R;
import com.owen.appplus.base.BaseActivity;
import com.owen.appplus.fragment.CollectionFragment;
import com.owen.appplus.fragment.ExportedFragment;
import com.owen.appplus.fragment.InstalledFragment;
import com.owen.appplus.fragment.OpenRecentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav) NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpNavigationView();
        initFragment();
    }

    private void initFragment() {
        switchFragment(OpenRecentFragment.newInstance());
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void setUpNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switchContent(item);
                return true;
            }
        });
    }

    private void switchContent(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_open_recent:
                fragment = OpenRecentFragment.newInstance();
                break;
            case R.id.nav_collection:
                fragment = CollectionFragment.newInstance();
                break;
            case R.id.nav_installed:
                fragment = InstalledFragment.newInstance();
                break;
            case R.id.nav_export:
                fragment = ExportedFragment.newInstance();
                break;
            case R.id.nav_donate:
                break;
            case R.id.nav_setting:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_feedback:
                break;
        }

        if (fragment != null) {
            item.setChecked(true);
            mDrawerLayout.closeDrawers();
            switchFragment(fragment);
        }
    }
}
