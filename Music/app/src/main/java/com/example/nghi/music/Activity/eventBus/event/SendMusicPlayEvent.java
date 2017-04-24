package com.example.nghi.music.Activity.eventBus.event;

import com.example.nghi.music.Activity.object.Music;

/**
 * Created by Tran Tuat on 1/10/2017.
 */

public class SendMusicPlayEvent {
    private Music music;
    private int position;
    private long durationPosition;

    public SendMusicPlayEvent(Music music, long durationPosition, int position) {
        this.music = music;
        this.durationPosition = durationPosition;
        this.position = position;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public long getDurationPosition() {
        return durationPosition;
    }

    public void setDurationPosition(long durationPosition) {
        this.durationPosition = durationPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
