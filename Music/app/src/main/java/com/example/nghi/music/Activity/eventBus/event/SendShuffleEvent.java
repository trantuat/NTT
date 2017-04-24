package com.example.nghi.music.Activity.eventBus.event;

import com.example.nghi.music.Activity.utils.Shuffle;

/**
 * Created by Tran Tuat on 1/13/2017.
 */

public class SendShuffleEvent {
    private Shuffle shuffle;

    public SendShuffleEvent(Shuffle shuffle) {
        this.shuffle = shuffle;
    }

    public Shuffle getShuffle() {
        return shuffle;
    }

    public void setShuffle(Shuffle shuffle) {
        this.shuffle = shuffle;
    }
}
