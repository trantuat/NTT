package com.example.nghi.music.Activity.eventBus.event;

/**
 * Created by Tran Tuat on 1/15/2017.
 */

public class SendCheckActivityCreatedEvent {
    private boolean isCreated;

    public SendCheckActivityCreatedEvent(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }
}
