package com.example.nghi.music.Activity.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.nghi.music.Activity.eventBus.event.SendListMusicEvent;
import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.Activity.service.MusicService;
import com.example.nghi.music.Activity.utils.DatabaseUtil;
import com.example.nghi.music.R;

import java.util.List;

public class FlashActivity extends BaseApp {
    private SeekBar mSeekbar;
    private ImageView mImgFlash;
    private int mProcess;
    private Handler mHandler;
    private List<Music> mMusics;
    private DatabaseUtil mDbUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_flash);
    }

    @Override
    protected void initUI() {
        mSeekbar=(SeekBar) findViewById(R.id.seekBar);
        mImgFlash = (ImageView) findViewById(R.id.imvFlash);
    }

    @Override
    protected void initData() {
        startService();
        mDbUtil = new DatabaseUtil();
        mMusics =mDbUtil.getMusic(this);
        mProcess=0;
        mSeekbar.setMax(100);
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mSeekbar.setProgress(mProcess);
                mProcess++;
                if (mProcess==20)  showImg();
                if (mProcess>100){
                    mHandler.removeCallbacks(this);
                    Intent i =new Intent(getBaseContext(),MainActivity.class);
                    i.putExtra(Constant.KEY_SEND_TO_MAIN,new SendListMusicEvent(mMusics));
                    startActivity(i);
                }else {
                    mHandler.postDelayed(this,25);
                }
            }
        },25);
    }

    private void showImg() {
        mImgFlash.setBackground(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_imv_flash));
        mImgFlash.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.anim_show_image));
    }

    @Override
    protected void addListener() {

    }

    private void startService() {
        if (!isMyServiceRunning(MusicService.class)) {
            startService(new Intent(this, MusicService.class));
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
