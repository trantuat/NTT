package com.example.nghi.music.Activity.eventBus.event;

import com.example.nghi.music.Activity.object.Music;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nghi on 1/10/17.
 */

public class SendListMusicEvent implements Serializable {
    private List<Music> musics;

    public SendListMusicEvent(List<Music> musics) {
        this.musics = musics;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }
}
