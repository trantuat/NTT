package com.example.nghi.music.Activity.eventBus.event;

/**
 * Created by Nghi on 1/10/17.
 */

public class SendActionEvent {
    private String action;

    public SendActionEvent(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
