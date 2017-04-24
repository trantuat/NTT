package com.example.nghi.music.Activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.nghi.music.Activity.eventBus.BusProvider;
import com.example.nghi.music.Activity.eventBus.event.SendActionEvent;
import com.example.nghi.music.Activity.eventBus.event.SendCheckActivityCreatedEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDataUpdateUIEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDurationEvent;
import com.example.nghi.music.Activity.eventBus.event.SendRepeatEvent;
import com.example.nghi.music.Activity.eventBus.event.SendShuffleEvent;
import com.example.nghi.music.Activity.eventBus.event.SendTimerEvent;
import com.example.nghi.music.Activity.eventBus.event.SendUpdateSeekBarEvent;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.Activity.utils.Repeat;
import com.example.nghi.music.Activity.utils.Shuffle;
import com.example.nghi.music.Activity.utils.TimeUtil;
import com.example.nghi.music.R;
import com.squareup.otto.Subscribe;

public class PlaySongActivity extends MusicAbtract {
    private static final int TIME = 20000;
    private ImageView mImgSongPlay;
    private RotateAnimation mRotate;
    private TextView mTvTimeCurrent;
    private TextView mTvDuration;

    private ImageButton mImgExit;
    private ImageButton mImgShuffle;
    private ImageButton mImgRepeat;
    private ImageButton mImgBtnTimer;
    private TextView mTvNameOfSong;
    private long mDurationPosition = 0;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private String mShuffle;
    private String mRepeat;
    private TextView mTvTimer;
    private SeekBar mSeekBarTimer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_play_song);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    @Override
    protected void initUI() {
        super.initUI();
        mImgSongPlay = (ImageView) findViewById(R.id.imgMusic);
        mImgExit = (ImageButton) findViewById(R.id.imgBtnBack);
        mImgRepeat = (ImageButton) findViewById(R.id.imageRepeat);
        mImgShuffle = (ImageButton) findViewById(R.id.imageShuffle);
        mImgBtnTimer = (ImageButton) findViewById(R.id.imgBtnTimer);
        mTvNameOfSong = (TextView) findViewById(R.id.tvNameSongPlayActivity);
        mTvTimeCurrent = (TextView) findViewById(R.id.tvTimeSong);
        mTvDuration = (TextView) findViewById(R.id.tvDurationTime);
        mTvNameOfSong.setSelected(true);
    }

    @Override
    protected void initData() {
        super.initData();
        mBus = BusProvider.getInstance();
        mBus.register(this);
        mBus.post(new SendCheckActivityCreatedEvent(true));
        mPreferences = getSharedPreferences(Constant.PREFERENCES, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mShuffle = mPreferences.getString(Constant.KEY_SHUFFLE, String.valueOf(Shuffle.NOSHUFFLE));
        mRepeat = mPreferences.getString(Constant.KEY_REPEAT, String.valueOf(Repeat.REPEAT));
        init();
        init(mShuffle, mRepeat);
        Intent i = getIntent();
        if (i != null) {
            SendDataUpdateUIEvent data = (SendDataUpdateUIEvent) i.getSerializableExtra(Constant.KEY_SEND_TO_PLAY_SONG);
            mMusic = data.getMusic();
            mDurationPosition = data.getDurationPosition();
            mDuration = Long.parseLong(mMusic.getDuration());
            mIsPlaying = data.isPlaying();
            updateUI();
        }
    }

    private void init(){
        setImgTimer(mPreferences.getBoolean(Constant.KEY_TIMER,false));
    }
    private void setImgTimer(boolean timer){
        if (timer) {
            mImgBtnTimer.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bg_button_timer));
        } else {
            mImgBtnTimer.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_notimer));
        }
    }
    @Override
    protected void addListener() {
        super.addListener();
        mImgExit.setOnClickListener(this);
        mImgRepeat.setOnClickListener(this);
        mImgShuffle.setOnClickListener(this);
        mImgBtnTimer.setOnClickListener(this);
    }


    @Subscribe
    public void getUpdateUI(SendDataUpdateUIEvent event) {
        mIsPlaying = event.isPlaying();
        mDurationPosition = event.getDurationPosition();
        mMusic = event.getMusic();
        mDuration = Long.parseLong(event.getMusic().getDuration());
        updateUI();
    }

    private void updateUI() {
        mTvNameOfSong.setText(mMusic.getName());
        mTvDuration.setText(TimeUtil.convertDuration(mDuration));
        mTvTimeCurrent.setText(TimeUtil.convertDuration(mDurationPosition));
        mSeekBar.setMax(Integer.parseInt(mMusic.getDuration()));
        mSeekBar.setProgress((int) mDurationPosition);
        rotateImage();
        changePlayButton(mIsPlaying);
    }

    @Subscribe
    public void getDuartionCurrent(SendUpdateSeekBarEvent event) {
        mSeekBar.setProgress((int) event.getDurationPosition());
        mTvTimeCurrent.setText(TimeUtil.convertDuration(event.getDurationPosition()));
    }

    @Subscribe
    public void updateTimer(SendTimerEvent event) {
        mTvTimer.setText("Music stop after: "+event.getTimer()+" minutes");
        mSeekBarTimer.setProgress(event.getTimer());
    }

    @Subscribe
    public void updateImageTimer(SendActionEvent event) {
        if (event.getAction().equals(Constant.STOP_TIMER)){
            mImgBtnTimer.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_notimer));
            mEditor.putBoolean(Constant.KEY_TIMER,false);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        mShuffle = mPreferences.getString(Constant.KEY_SHUFFLE, String.valueOf(Shuffle.NOSHUFFLE));
        mRepeat = mPreferences.getString(Constant.KEY_REPEAT, String.valueOf(Repeat.REPEAT));
        int id = view.getId();
        switch (id) {
            case R.id.imgBtnBack: {
                mBus.post(new SendActionEvent(Constant.BACK));
                finish();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                break;
            }
            case R.id.imageRepeat: {

                if (mRepeat.equals(String.valueOf(Repeat.REPEAT))) {
                    mBus.post(new SendRepeatEvent(Repeat.UNREPEAT));
                    mEditor.putString(Constant.KEY_REPEAT, String.valueOf(Repeat.UNREPEAT));
                    mEditor.commit();
                    mImgRepeat.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_unrepeat));
                    return;
                }
                if (mRepeat.equals(String.valueOf(Repeat.UNREPEAT))) {
                    mBus.post(new SendRepeatEvent(Repeat.ONE));
                    mEditor.putString(Constant.KEY_REPEAT, String.valueOf(Repeat.ONE));
                    mEditor.commit();
                    mImgRepeat.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_repeat_one));
                    return;
                }
                if (mRepeat.equals(String.valueOf(Repeat.ONE))) {
                    mBus.post(new SendRepeatEvent(Repeat.REPEAT));
                    mEditor.putString(Constant.KEY_REPEAT, String.valueOf(Repeat.REPEAT));
                    mEditor.commit();
                    mImgRepeat.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_repeat));
                    return;
                }
                break;
            }
            case R.id.imageShuffle: {
                if (mShuffle.equals(String.valueOf(Shuffle.SHUFFLE))) {
                    mBus.post(new SendShuffleEvent(Shuffle.NOSHUFFLE));
                    mEditor.putString(Constant.KEY_SHUFFLE, String.valueOf(Shuffle.NOSHUFFLE));
                    mEditor.commit();
                    mImgShuffle.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_noshuffle));
                    return;
                }
                if (mShuffle.equals(String.valueOf(Shuffle.NOSHUFFLE))) {
                    mBus.post(new SendShuffleEvent(Shuffle.SHUFFLE));
                    mEditor.putString(Constant.KEY_SHUFFLE, String.valueOf(Shuffle.SHUFFLE));
                    mEditor.commit();
                    mImgShuffle.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_shuffle));
                    return;
                }
                break;
            }
            case R.id.imgBtnTimer: {
                dialogTimer();
                mBus.post(new SendActionEvent(Constant.REQUEST_UPDATE_TIMER));
                break;
            }

        }
    }

    private void init(String shuffle, String repeat) {
        if (shuffle.equals(String.valueOf(Shuffle.SHUFFLE))) {
            mImgShuffle.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_shuffle));
        } else {
            mImgShuffle.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_noshuffle));
        }
        if (repeat.equals(String.valueOf(Repeat.ONE))) {
            mImgRepeat.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_repeat_one));
        } else if (repeat.equals(String.valueOf(Repeat.REPEAT))) {
            mImgRepeat.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_repeat));
        } else {
            mImgRepeat.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.bg_button_unrepeat));
        }

    }

    private void rotateImage() {
        if (mIsPlaying) {
            mRotate = new RotateAnimation(0, 359,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            mRotate.setDuration(TIME);
            mRotate.setRepeatCount(Animation.INFINITE);
            mImgSongPlay.startAnimation(mRotate);
        } else {
            mImgSongPlay.clearAnimation();
        }
    }
    private void dialogTimer() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.item_timer, null);
        view.setAlpha(0.8f);
        mSeekBarTimer = (SeekBar) view.findViewById(R.id.seekBar);
        mTvTimer = (TextView) view.findViewById(R.id.tvtimer);
        mSeekBarTimer.setMax(150);
        mSeekBarTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvTimer.setText("Music stop after: "+progress+" minutes");
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mBus.post(new SendTimerEvent(seekBar.getProgress()));
                if (seekBar.getProgress()>0){
                    setImgTimer(true);
                    mEditor.putBoolean(Constant.KEY_TIMER,true);
                }else{
                    setImgTimer(false);
                    mEditor.putBoolean(Constant.KEY_TIMER,false);
                }
                mEditor.commit();
            }
        });
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        dialog.setCancelable(true);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mSeekBar.setProgress(i);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mDurationPosition = seekBar.getProgress();
        mTvTimeCurrent.setText(TimeUtil.convertDuration((int) mDurationPosition));
        mBus.post(new SendDurationEvent(mDurationPosition));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBus.post(new SendActionEvent(Constant.BACK));
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.post(new SendCheckActivityCreatedEvent(false));
    }
}
