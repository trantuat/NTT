package com.example.nghi.music.Activity.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.nghi.music.Activity.activity.PlaySongActivity;
import com.example.nghi.music.Activity.eventBus.BusProvider;
import com.example.nghi.music.Activity.eventBus.event.SendActionEvent;
import com.example.nghi.music.Activity.eventBus.event.SendCheckActivityCreatedEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDataUpdateUIEvent;
import com.example.nghi.music.Activity.eventBus.event.SendDurationEvent;
import com.example.nghi.music.Activity.eventBus.event.SendListMusicEvent;
import com.example.nghi.music.Activity.eventBus.event.SendPositionEvent;
import com.example.nghi.music.Activity.eventBus.event.SendRepeatEvent;
import com.example.nghi.music.Activity.eventBus.event.SendShuffleEvent;
import com.example.nghi.music.Activity.eventBus.event.SendSongEvent;
import com.example.nghi.music.Activity.eventBus.event.SendStatusEvent;
import com.example.nghi.music.Activity.eventBus.event.SendTimerEvent;
import com.example.nghi.music.Activity.eventBus.event.SendUpdateSeekBarEvent;
import com.example.nghi.music.Activity.notification.SongNotification;
import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.utils.Repeat;
import com.example.nghi.music.Activity.utils.Shuffle;
import com.example.nghi.music.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Nghi on 1/9/17.
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private List<Music> mMusics;
    private MediaPlayer mMedia;
    private int mPosition = -1;
    private Bus mBus;
    private Runnable mRunnable;
    private Handler mHandlerDelay;
    private Handler mHandlerTimer;
    private Runnable mRunTimer;
    private boolean mIsPlaying = false;
    private Repeat mType;
    private Shuffle mShuffle;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private List<Integer> mListRepeat;
    private Random rand;
    private int mTime;
    private SongNotification mNotification;
    private boolean mIsNotificationShowing;
    private NotificationManager mNotificationManager;
    private boolean mIsCreatedActivity;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIsCreatedActivity = false;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = SongNotification.getInstance();
        mIsNotificationShowing = false;
        mHandlerDelay = new Handler();
        mTime = 0;
        mListRepeat = new ArrayList<>();
        mPreferences = getSharedPreferences(Constant.PREFERENCES, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        String repeat = mPreferences.getString(Constant.KEY_REPEAT, String.valueOf(Repeat.REPEAT));
        String shuffle = mPreferences.getString(Constant.KEY_SHUFFLE, String.valueOf(Shuffle.NOSHUFFLE));
        if (repeat.equals(String.valueOf(Repeat.REPEAT))) {
            mType = Repeat.REPEAT;
        } else if (repeat.equals(String.valueOf(Repeat.UNREPEAT))) {
            mType = Repeat.UNREPEAT;
        } else {
            mType = Repeat.ONE;
        }

        if (shuffle.equals(Shuffle.SHUFFLE)) {
            mShuffle = Shuffle.SHUFFLE;
        } else {
            mShuffle = Shuffle.NOSHUFFLE;
        }
        mShuffle = Shuffle.NOSHUFFLE;
        mMusics = new ArrayList<>();
        mMedia = new MediaPlayer();
        mBus = BusProvider.getInstance();
        mBus.register(this);
        mMedia.setOnCompletionListener(this);
        mMedia.setOnErrorListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    private void playSong() {
        mIsPlaying = true;
        mMedia.start();
        updateUI();
    }

    private void prepareSong() {
        if (mMedia != null) {
            mMedia.reset();
        }
        try {
            mMedia.setDataSource(mMusics.get(mPosition).getPath());
            mMedia.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void pauseSong() {
        mIsPlaying = false;
        if (mTime > 0) {
            mTime = 0;
            if (mHandlerTimer != null) mHandlerTimer.removeCallbacks(mRunTimer);
            mEditor.putBoolean(Constant.KEY_TIMER, false);
            mEditor.commit();
            mBus.post(new SendActionEvent(Constant.STOP_TIMER));
        }
        if (mMedia.isPlaying()) {
            mMedia.pause();
        }
        updateUI();
    }

    private void resumeSong() {
        if (!mMedia.isPlaying()) {
            mMedia.start();
            mIsPlaying = true;
        }
        updateUI();
    }

    private void updateUI() {
        if (mIsNotificationShowing)
            mNotification.updateNotification(getBaseContext(), mNotificationManager, mMusics.get(mPosition), mIsPlaying);
        mBus.post(new SendDataUpdateUIEvent(mMusics.get(mPosition), mMedia.getCurrentPosition(), mPosition, mIsPlaying));
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    mBus.post(new SendUpdateSeekBarEvent(mMedia.getCurrentPosition()));
                    mHandlerDelay.postDelayed(this, 1000);
                }
            };
        }
        if (mIsPlaying) {
            mHandlerDelay.postDelayed(mRunnable, 1000);
        } else {
            mHandlerDelay.removeCallbacks(mRunnable);
        }
    }

    @Subscribe
    public void getTime(SendTimerEvent event) {
        mTime = event.getTimer();
        scheduleTimer();
    }

    private void scheduleTimer() {
        if (mHandlerTimer != null) mHandlerTimer.removeCallbacks(mRunTimer);
        if (mTime > 0) {
            mHandlerTimer = new Handler();
            mRunTimer = new Runnable() {
                @Override
                public void run() {
                    mTime -= 1;
                    mBus.post(new SendTimerEvent(mTime));
                    if (mTime <= 0) {
                        mHandlerTimer.removeCallbacks(this);
                        pauseSong();
                        mBus.post(new SendActionEvent(Constant.STOP_TIMER));
                    } else {
                        mHandlerTimer.postDelayed(this, 60000);
                    }
                }
            };
            mHandlerTimer.postDelayed(mRunTimer, 60000);
        }

    }

    @Subscribe
    public void getListMusic(SendListMusicEvent event) {
        mMusics = event.getMusics();
    }

    @Subscribe
    public void getPositionPlay(SendPositionEvent event) {
        mPosition = event.getPosition();
    }

    @Subscribe
    public void getDuration(SendDurationEvent event) {
        mMedia.seekTo((int) event.getDurationPosition());
    }

    @Subscribe
    public void getRepeat(SendRepeatEvent event) {
        mType = event.getRepeat();
    }

    @Subscribe
    public void getShuffle(SendShuffleEvent event) {
        mShuffle = event.getShuffle();
    }
    @Subscribe
    public void getCheckCreatedActivity(SendCheckActivityCreatedEvent event) {
        mIsCreatedActivity=event.isCreated();
    }

    @Subscribe
    public void getAction(SendActionEvent event) {
        switch (event.getAction()) {
            case Constant.PLAY:
                prepareSong();
                playSong();
                mNotification.showNotification(getBaseContext(), this, mMusics.get(mPosition), mIsPlaying);
                mIsNotificationShowing = true;
                break;
            case Constant.PAUSE:
                pauseSong();
                break;
            case Constant.RESUME:
                resumeSong();
                break;
            case Constant.NEXT:
                nextSong();
                break;
            case Constant.PERIOUS:
                periousSong();
                break;
            case Constant.BACK:
                back();
                break;
            case Constant.SHOW_ACTIVITY:
                showActivity();
                break;
            case Constant.REQUEST_UPDATE_TIMER:
                mBus.post(new SendTimerEvent(mTime));
                break;
        }
    }

    private void showActivity() {
        getApplication().sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        if (!mIsCreatedActivity){
            Intent i=new Intent(this, PlaySongActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SendDataUpdateUIEvent data = new SendDataUpdateUIEvent(mMusics.get(mPosition),mMedia.getCurrentPosition(),mPosition,mIsPlaying);
            i.putExtra(Constant.KEY_SEND_TO_PLAY_SONG,data);
            startActivity(i);
        }
    }

    private void back() {
        mBus.post(new SendDataUpdateUIEvent(mMusics.get(mPosition), mMedia.getCurrentPosition(), mPosition, mIsPlaying));
    }

    private void nextSong() {
        if (mShuffle == Shuffle.NOSHUFFLE) {
            if (mPosition == mMusics.size() - 1) {
                mPosition = 0;
            } else {
                mPosition++;
            }
            mListRepeat.add(mPosition);
        } else {
            int newPosition = mPosition;
            rand = new Random();
            while ((newPosition == mPosition || mListRepeat.contains(newPosition)) && mListRepeat.size() < mMusics.size()) {
                newPosition = rand.nextInt(mMusics.size() - 1);
            }
            if (mListRepeat.size() > mMusics.size()) {
                mListRepeat.clear();
                newPosition = rand.nextInt(mMusics.size() - 1);
            }
            mPosition = newPosition;
            mListRepeat.add(newPosition);
        }
        prepareSong();
        playSong();
        updateUI();
    }

    private void periousSong() {
        if (mShuffle == Shuffle.NOSHUFFLE) {
            if (mPosition == 0) {
                mPosition = mMusics.size() - 1;
            } else {
                mPosition--;
            }
            mListRepeat.add(mPosition);
        } else {
            int newPosition = mPosition;
            rand = new Random();
            while (newPosition == mPosition || mListRepeat.contains(newPosition)) {
                newPosition = rand.nextInt(mMusics.size() - 1);
            }
            mPosition = newPosition;
            mListRepeat.add(newPosition);
        }
        prepareSong();
        playSong();
        updateUI();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNext();
    }

    private void playNext() {
        switch (mType) {
            case UNREPEAT:
                pauseSong();
                break;
            case REPEAT:
                if (mShuffle == Shuffle.NOSHUFFLE) {
                    if (mPosition == mMusics.size() - 1) {
                        mPosition = 0;
                    } else {
                        mPosition++;
                    }
                    mListRepeat.add(mPosition);
                } else {
                    int newPosition = mPosition;
                    rand = new Random();
                    while (newPosition == mPosition || mListRepeat.contains(newPosition)) {
                        newPosition = rand.nextInt(mMusics.size() - 1);
                    }
                    mPosition = newPosition;
                    mListRepeat.add(newPosition);
                }
                prepareSong();
                playSong();
                break;
            case ONE:
                prepareSong();
                playSong();
                break;
        }
        updateUI();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        playNext();
        return false;
    }

    @Override
    public void onDestroy() {
        mBus.unregister(this);
        super.onDestroy();
        mEditor.putBoolean(Constant.KEY_TIMER, false);
        mEditor.commit();
        mMedia.reset();
        mMedia.release();
        mMedia = null;
        mIsNotificationShowing = false;
        if (mHandlerTimer != null) mHandlerTimer.removeCallbacks(mRunTimer);
        if (mHandlerDelay != null) {
            mHandlerDelay.removeCallbacks(mRunnable);
        }
    }

}
