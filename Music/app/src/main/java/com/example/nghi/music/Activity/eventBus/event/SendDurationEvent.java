package com.example.nghi.music.Activity.eventBus.event;

/**
 * Created by Nghi on 1/10/17.
 */

public class SendDurationEvent {
    private long duration;

    public SendDurationEvent(long duration) {
        this.duration = duration;
    }

    public long getDurationPosition() {
        return duration;
    }

    public void setDurationPosition(long duration) {
        this.duration = duration;
    }
}
