package com.example.nghi.music.Activity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.nghi.music.Activity.activity.BaseApp;
import com.example.nghi.music.Activity.fragments.AlbumFragment;
import com.example.nghi.music.Activity.fragments.ArtistFragment;
import com.example.nghi.music.Activity.fragments.PlaylistFragment;
import com.example.nghi.music.Activity.fragments.SongFragment;
import com.example.nghi.music.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseApp {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }

    @Override
    protected void initData() {
        setUpToolBar();
        setupViewPager(mViewPager);
        setUpTab();
        setUpDrawer();

    }

    @Override
    protected void addListener() {

    }
    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new SongFragment(), getString(R.string.song));
        mAdapter.addFragment(new AlbumFragment(), getString(R.string.album));
        mAdapter.addFragment(new ArtistFragment(), getString(R.string.artist));
        mAdapter.addFragment(new PlaylistFragment(), getString(R.string.playlist));
        viewPager.setAdapter(mAdapter);
    }

    private void setUpTab() {
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
