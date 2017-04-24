package com.example.nghi.music.Activity.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.Activity.service.MusicBroadcast;
import com.example.nghi.music.Activity.service.MusicService;
import com.example.nghi.music.R;

/**
 * Created by Tran Tuat on 1/13/2017.
 */

public class SongNotification {
    private static SongNotification mPlaySongNotification;
    private RemoteViews mRemoteViews;
    private Notification mNotification;

    public static SongNotification getInstance() {
        if (mPlaySongNotification == null) {
            mPlaySongNotification = new SongNotification();
        }
        return mPlaySongNotification;
    }

    boolean currentVersionSupportBigNotification = currentVersionSupportBigNotification();

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    public void showNotification(Context context, MusicService service, Music music, boolean isPlaying) {
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_big);
        mRemoteViews.setImageViewResource(R.id.imgSong, R.drawable.ic_launch);
        mRemoteViews.setImageViewResource(R.id.imgBtnPlay, R.drawable.ic_pause_press);
        Intent intent = new Intent(context, MusicBroadcast.class);
        intent.setAction(Constant.ACTION_SHOW_ACTIVITY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentPrevious = new Intent(context, MusicBroadcast.class);
        intentPrevious.setAction(Constant.ACTION_PREV);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(context, 0, intentPrevious, 0);

        Intent intentNext = new Intent(context, MusicBroadcast.class);
        intentNext.setAction(Constant.ACTION_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, 0);

        Intent intentPause = new Intent(context, MusicBroadcast.class);
        intentPause.setAction(Constant.ACTION_PAUSE);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context, 0, intentPause, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.imgBtnPrevious, pendingIntentPrev);
        mRemoteViews.setOnClickPendingIntent(R.id.imgBtnNext, pendingIntentNext);
        mRemoteViews.setOnClickPendingIntent(R.id.imgBtnPlay, pendingIntentPause);
        mRemoteViews.setOnClickPendingIntent(R.id.rl_notify, pendingIntent);
        mRemoteViews.setTextViewText(R.id.tvNameSong, music.getName());
        mRemoteViews.setTextViewText(R.id.tvNameSinger, music.getSinger());

        mNotification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(mRemoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();

        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        service.startForeground(Constant.FOREGROUND_SERVICE, mNotification);
    }

    public void updateNotification(Context context, NotificationManager manager, Music music, boolean isPlaying) {
        Intent intentPause = new Intent(context, MusicBroadcast.class);
        intentPause.setAction(Constant.ACTION_PAUSE);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context, 0, intentPause, 0);

        Intent intentPlay = new Intent(context, MusicBroadcast.class);
        intentPlay.setAction(Constant.ACTION_REPLAY);
        PendingIntent pendingIntentReplay = PendingIntent.getBroadcast(context, 0, intentPlay, 0);

        if (isPlaying){
            mRemoteViews.setImageViewResource(R.id.imgBtnPlay, R.drawable.ic_pause_press);
            mRemoteViews.setOnClickPendingIntent(R.id.imgBtnPlay,pendingIntentPause);
        }else {
            mRemoteViews.setImageViewResource(R.id.imgBtnPlay, R.drawable.ic_play_play_activity);
            mRemoteViews.setOnClickPendingIntent(R.id.imgBtnPlay,pendingIntentReplay);
        }
        mRemoteViews.setTextViewText(R.id.tvNameSong, music.getName());
        mRemoteViews.setTextViewText(R.id.tvNameSinger, music.getSinger());

        manager.notify(Constant.FOREGROUND_SERVICE, mNotification);

    }

}
