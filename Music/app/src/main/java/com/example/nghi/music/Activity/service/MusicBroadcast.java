package com.example.nghi.music.Activity.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.nghi.music.Activity.eventBus.BusProvider;
import com.example.nghi.music.Activity.eventBus.event.SendActionEvent;

/**
 * Created by Tran Tuat on 1/13/2017.
 */

public class MusicBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.ACTION_PAUSE)) {
            BusProvider.getInstance().post(new SendActionEvent(Constant.PAUSE));
        }
        if (intent.getAction().equals(Constant.ACTION_REPLAY)) {
            BusProvider.getInstance().post(new SendActionEvent(Constant.RESUME));
        }
        if (intent.getAction().equals(Constant.ACTION_NEXT)) {
            BusProvider.getInstance().post(new SendActionEvent(Constant.NEXT));
        }
        if (intent.getAction().equals(Constant.ACTION_PREV)) {
            BusProvider.getInstance().post(new SendActionEvent(Constant.PERIOUS));
        }
        if (intent.getAction().equals(Constant.ACTION_SHOW_ACTIVITY)) {
            BusProvider.getInstance().post(new SendActionEvent(Constant.SHOW_ACTIVITY));
        }
    }
}
