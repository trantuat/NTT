package com.example.nghi.music.Activity.eventBus.event;

import com.example.nghi.music.Activity.utils.Repeat;

/**
 * Created by Tran Tuat on 1/13/2017.
 */

public class SendRepeatEvent {
    private Repeat repeat;

    public SendRepeatEvent(Repeat repeat) {
        this.repeat = repeat;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }
}
