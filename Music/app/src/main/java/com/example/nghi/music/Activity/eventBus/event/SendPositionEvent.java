package com.example.nghi.music.Activity.eventBus.event;

/**
 * Created by Nghi on 1/10/17.
 */

public class SendPositionEvent {
    private int position;

    public SendPositionEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
