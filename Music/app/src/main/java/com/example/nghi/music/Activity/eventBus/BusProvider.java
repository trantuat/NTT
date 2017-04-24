package com.example.nghi.music.Activity.eventBus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


public class BusProvider {
    private static Bus mBus;
    public static synchronized Bus getInstance() {
        if (mBus == null) {
            mBus = new Bus(ThreadEnforcer.ANY);
        }
        return mBus;
    }
}
