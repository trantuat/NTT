package com.example.nghi.music.Activity.activity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nghi.music.Activity.listener.RecycleTouchListener;
import com.example.nghi.music.Activity.adapter.SongAdapter;
import com.example.nghi.music.Activity.database.MyDatabase;
import com.example.nghi.music.Activity.eventBus.BusProvider;
import com.example.nghi.music.Activity.eventBus.event.SendActionEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDataUpdateUIEvent;
import com.example.nghi.music.Activity.eventBus.event.SendListMusicEvent;
import com.example.nghi.music.Activity.eventBus.event.SendPositionEvent;
import com.example.nghi.music.Activity.object.Album;
import com.example.nghi.music.Activity.object.Artist;
import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.object.PlayList;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends BaseApp {
    private List<Music> mMusicList ;
    private RecyclerView mRecyclerView;
    private SongAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private Toolbar mToolbar;
    private MyDatabase db;
    private Bus mBus;
    private SendDataUpdateUIEvent mDataSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_song);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    @Override
    protected void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycle_view_song);
        setUpToolBar();
        getData();
        setUpRecycler();
    }

    @Override
    protected void initData() {
        mBus= BusProvider.getInstance();
        mBus.register(this);
    }

    @Override
    protected void addListener() {
        mRecyclerView.addOnItemTouchListener(new RecycleTouchListener(this, mRecyclerView, new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                mBus.post(new SendListMusicEvent(mMusicList));
                mBus.post(new SendPositionEvent(position));
                mBus.post(new SendActionEvent(Constant.PLAY));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mDataSend != null) {
                            Intent i =new Intent(view.getContext(),PlaySongActivity.class);
                            i.putExtra(Constant.KEY_SEND_TO_PLAY_SONG,mDataSend);
                            startActivity(i);
                            handler.removeCallbacks(this);
                        }
                    }
                },400);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Subscribe
    public void getDataSend(SendDataUpdateUIEvent event) {
        mDataSend = event;
    }


    public void setUpToolBar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }
    private void getData(){
        Intent i=getIntent();
        if(i.getIntExtra(Constant.TAG,Constant.DEFAULS)==Constant.TAG_ALBUMS) {
            Album album = (Album) i.getSerializableExtra(Constant.KEY);
            mMusicList=new ArrayList<>();
            mMusicList = getMusicFromAlbum(album);
            mToolbar.setTitle(album.getAlbumName());
        }
        if(i.getIntExtra(Constant.TAG,Constant.DEFAULS)==Constant.TAG_ARTIST) {
            Artist artist = (Artist)i.getSerializableExtra(Constant.KEY);
            mMusicList=new ArrayList<>();
            mMusicList = getMusicFromArtist(artist);
            mToolbar.setTitle(artist.getNameSinger());
        }
        if(i.getIntExtra(Constant.TAG,Constant.DEFAULS)==Constant.TAG_PLAYLIST){
            PlayList playList = (PlayList) i.getSerializableExtra(Constant.KEY);
            mMusicList=new ArrayList<>();
            db=new MyDatabase(this);
            mMusicList=db.searchMusicById(playList.getPlayListId());
            mToolbar.setTitle(playList.getNamePlayList());
        }
    }
    public void setUpRecycler(){
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SongAdapter(getBaseContext(),mMusicList);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    private List<Music> getMusicFromArtist(Artist artist) {
        String colum[]={
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String sort= "LOWER(" + MediaStore.Audio.Media.TITLE +") ASC";
        String where=MediaStore.Audio.Media.ARTIST_ID+" = "+artist.getSingerId();
        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,colum,where,null,sort);

        List<Music> tmp=new ArrayList<>();
        if (cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Music music=new Music();
                music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setSinger(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                tmp.add(music);
                cursor.moveToNext();
            }
            cursor.close();
            return tmp;
        }
        return null;

    }

    private List<Music> getMusicFromAlbum(Album album) {
        String colum[]={
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String sort= "LOWER(" + MediaStore.Audio.Media.TITLE +") ASC";
        String where=MediaStore.Audio.Albums.ALBUM_ID+" = "+album.getAlbumId();
        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,colum,where,null,sort);

        List<Music> tmp=new ArrayList<>();
        if (cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Music music=new Music();
                music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setSinger(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                tmp.add(music);
                cursor.moveToNext();
            }
            cursor.close();
            return tmp;
        }
        return null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

}
