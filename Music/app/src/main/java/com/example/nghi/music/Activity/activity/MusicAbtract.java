package com.example.nghi.music.Activity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.example.nghi.music.Activity.eventBus.BusProvider;
import com.example.nghi.music.Activity.eventBus.event.SendActionEvent;
import com.example.nghi.music.Activity.eventBus.event.SendPositionEvent;
import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.R;
import com.squareup.otto.Bus;

/**
 * Created by Tran Tuat on 1/10/2017.
 */

public abstract class MusicAbtract extends BaseApp implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    protected ImageButton mImgPlay;
    protected ImageButton mImgNext;
    protected ImageButton mImgPrevious;
    protected Bus mBus;
    protected SeekBar mSeekBar;


    protected Music mMusic;
    protected long mDuration = 0;
    protected int mPosition = 0;
    protected boolean mIsPlaying = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUI() {
        mImgPlay = (ImageButton) findViewById(R.id.imgBtnPlay);
        mImgPrevious = (ImageButton) findViewById(R.id.imgBtnPrevious);
        mImgNext = (ImageButton) findViewById(R.id.imgBtnNext);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void addListener() {
        mImgPlay.setOnClickListener(this);
        mImgNext.setOnClickListener(this);
        mImgPrevious.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgBtnPlay: {
                if (mIsPlaying) {
                    mBus.post(new SendActionEvent(Constant.PAUSE));
                } else {
                    mBus.post(new SendActionEvent(Constant.RESUME));
                }
                Animation anim= AnimationUtils.loadAnimation(getBaseContext(),R.anim.anim_button_click);
                mImgPlay.startAnimation(anim);
                break;
            }

            case R.id.imgBtnNext: {
                mBus.post(new SendActionEvent(Constant.NEXT));
                break;
            }
            case R.id.imgBtnPrevious: {
                mBus.post(new SendActionEvent(Constant.PERIOUS));
                break;
            }

        }

    }

    protected void changePlayButton(boolean isplaying) {
        mImgPlay.setBackgroundResource(isplaying ? R.drawable.ic_pause_press : R.drawable.ic_play_play_activity);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }
}

