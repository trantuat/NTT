package com.example.nghi.music.Activity.eventBus.event;

/**
 * Created by Nghi on 1/10/17.
 */

public class SendStatusEvent {
    private boolean isPlay;

    public SendStatusEvent(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
