package com.example.nghi.music.Activity.eventBus.event;

import com.example.nghi.music.Activity.object.Music;

import java.io.Serializable;

/**
 * Created by Nghi on 1/9/17.
 */

public class SendDataUpdateUIEvent implements Serializable{
    private Music music;
    private long durationPosition;
    private int position;
    private boolean isPlaying;

    public SendDataUpdateUIEvent(Music music, long durationPosition, int position, boolean status) {
        this.music = music;
        this.durationPosition = durationPosition;
        this.position = position;
        this.isPlaying = status;
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }
}
