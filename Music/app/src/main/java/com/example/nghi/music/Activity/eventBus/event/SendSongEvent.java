package com.example.nghi.music.Activity.eventBus.event;

import com.example.nghi.music.Activity.object.Music;

/**
 * Created by Nghi on 1/10/17.
 */

public class SendSongEvent {
    private Music music;

    public SendSongEvent(Music music) {
        this.music = music;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
