package com.example.nghi.music.Activity.eventBus.event;

/**
 * Created by Tran Tuat on 1/11/2017.
 */

public class SendUpdateSeekBarEvent {
    private long durationPosition;

    public SendUpdateSeekBarEvent(long durationPosition) {
        this.durationPosition = durationPosition;
    }

    public long getDurationPosition() {
        return durationPosition;
    }

    public void setDurationPosition(long durationPosition) {
        this.durationPosition = durationPosition;
    }
}
