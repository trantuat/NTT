package com.example.nghi.music.Activity.activity;

import android.app.ActivityManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nghi.music.Activity.eventBus.BusProvider;
import com.example.nghi.music.Activity.eventBus.event.SendActionEvent;
import com.example.nghi.music.Activity.eventBus.event.SendCheckActivityCreatedEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDataUpdateUIEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDurationEvent;
import com.example.nghi.music.Activity.eventBus.event.SendListMusicEvent;
import com.example.nghi.music.Activity.eventBus.event.SendUpdateSeekBarEvent;
import com.example.nghi.music.Activity.fragments.AlbumFragment;
import com.example.nghi.music.Activity.fragments.ArtistFragment;
import com.example.nghi.music.Activity.fragments.PlaylistFragment;
import com.example.nghi.music.Activity.fragments.SongFragment;
import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.Activity.service.MusicService;
import com.example.nghi.music.Activity.utils.TimeUtil;
import com.example.nghi.music.Activity.utils.Utils;
import com.example.nghi.music.R;
import com.squareup.otto.Subscribe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MainActivity extends MusicAbtract implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTvSong;
    private TextView mTvSinger;
    private ImageView mImgSong;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SendDataUpdateUIEvent mDataSend;
    private long mDurationPosition = 0;

    private static final int TIME = 20000;
    private RotateAnimation mRotate;
    private RelativeLayout mRlPlay;
    private static final long TIME_INTERVAL = 2000;
    private long mBackPressed;
    private SendListMusicEvent mMusics;
    private NavigationView mNavView;
    private boolean mIsPlaySongActivityCreated;

    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void addListener() {
        super.addListener();
        mRlPlay.setOnClickListener(this);
        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent i =getIntent();
        if (i!=null){
            mMusics = (SendListMusicEvent)i.getSerializableExtra(Constant.KEY_SEND_TO_MAIN);
        }
        mIsPlaySongActivityCreated = false;
        mBus = BusProvider.getInstance();
        mBus.register(this);
        setUpToolBar();
        setupViewPager(mViewPager);
        setUpTab();
        setUpDrawer();
    }

    @Override
    protected void initUI() {
        super.initUI();
        mRlPlay = (RelativeLayout) findViewById(R.id.rlplay);
        mImgSong = (ImageView) findViewById(R.id.imgSong);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTvSong = (TextView) findViewById(R.id.tvNameSong);
        mTvSinger = (TextView) findViewById(R.id.tvNameSinger);
        mNavView = (NavigationView) findViewById(R.id.nvView);
        mTvSong.setSelected(true);
        mTvSinger.setSelected(true);
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        SongFragment f=new SongFragment();
        Bundle b=new Bundle();
        b.putSerializable(Constant.KEY_SEND_TO_MAIN,mMusics);
        f.setArguments(b);
        mAdapter.addFragment(f, getString(R.string.song));
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
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        switch (id) {
            case R.id.rlplay:
                Intent i = new Intent(this, PlaySongActivity.class);
                mDataSend.setDurationPosition(mDurationPosition);
                i.putExtra(Constant.KEY_SEND_TO_PLAY_SONG, mDataSend);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mSeekBar.setProgress(i);
        mDurationPosition = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mDurationPosition = seekBar.getProgress();
        mBus.post(new SendDurationEvent(mDurationPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();

        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.setQuery("",false);
                mSearchView.clearFocus();
                mSearchView.setIconified(true);
                Utils.hideKeyboard(mSearchView,getBaseContext());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (mViewPager.getCurrentItem()){
                    case 0:
                        SongFragment songFragment = (SongFragment) mAdapter.getItem(0);
                        songFragment.search(newText);
                        break;
                    case 1:
                        AlbumFragment albumsFragment = (AlbumFragment) mAdapter.getItem(1);
                        albumsFragment.search(newText);
                        break;
                    case 2:
                        ArtistFragment artistFragment = (ArtistFragment) mAdapter.getItem(2);
                        artistFragment.search(newText);
                        break;
                    case 3:
                        PlaylistFragment playistFragment = (PlaylistFragment) mAdapter.getItem(3);
                        playistFragment.search(newText);
                        break;
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Subscribe
    public void getUpdateUI(SendDataUpdateUIEvent event) {
        mDataSend = event;
        mDurationPosition = event.getDurationPosition();
        mMusic = event.getMusic();
        mIsPlaying = event.isPlaying();
        mDuration = Long.parseLong(event.getMusic().getDuration());
        updateUI();
        rotateimage();
    }
    @Subscribe
    public void getCheckPlaySongIsCreated(SendCheckActivityCreatedEvent event) {
       mIsPlaySongActivityCreated = event.isCreated();
    }

    @Subscribe
    public void startActivity(SendActionEvent event) {
        if (event.getAction().equals(Constant.START_ACTIVITY)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mIsPlaySongActivityCreated){
                        Intent i = new Intent(getBaseContext(), PlaySongActivity.class);
                        i.putExtra(Constant.KEY_SEND_TO_PLAY_SONG, mDataSend);
                        startActivity(i);
                    }
                }
            }, 500);
        }
    }

    @Subscribe
    public void getDuartionCurrent(SendUpdateSeekBarEvent event) {
        mSeekBar.setProgress((int) event.getDurationPosition());
    }

    private void rotateimage() {
        if (mIsPlaying) {
            mRotate = new RotateAnimation(0, 359,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            mRotate.setDuration(TIME);
            mRotate.setRepeatCount(Animation.INFINITE);
            mImgSong.startAnimation(mRotate);
        } else {
            mImgSong.clearAnimation();
        }
    }

    private void updateUI() {
        displayRlBottom();
        changePlayButton(mIsPlaying);
        mSeekBar.setMax(Integer.parseInt(mMusic.getDuration()));
        mSeekBar.setProgress((int) mDurationPosition);
    }

    private void displayRlBottom() {
        if (mRlPlay.getVisibility() == View.GONE) {
            mRlPlay.setVisibility(View.VISIBLE);
            mRlPlay.setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.show_view_play);
            mRlPlay.startAnimation(anim);
        }
        mTvSong.setText(mMusic.getName());
        mTvSinger.setText(mMusic.getSinger());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.songs:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.albums:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.artist:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.playlist:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.settings:
                Intent i =new Intent(this,EqualizerActivity.class);
                startActivity(i);
                break;
            case R.id.about_us:
                Toast.makeText(getBaseContext(),"About us",Toast.LENGTH_SHORT).show();
                break;
            case R.id.policy:
                Toast.makeText(getBaseContext(),"Policy",Toast.LENGTH_SHORT).show();
                break;
        }
        item.setCheckable(false);
        mDrawerLayout.closeDrawers();
        return true;
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

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MusicService.class));
        super.onDestroy();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawers();
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            stopService(new Intent(this, MusicService.class));
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        if (mSearchView!=null){
            mSearchView.setQuery("",false);
            mSearchView.clearFocus();
            mSearchView.setIconified(true);
        }
        super.onResume();
    }
}
