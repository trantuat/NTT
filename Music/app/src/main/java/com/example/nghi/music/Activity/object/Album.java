package com.example.nghi.music.Activity.object;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nghi on 1/6/17.
 */

public class Album implements Serializable{
    private String albumName;
    private String singer;
    private List<Music> musicList;
    private long albumId;

    public Album() {
    }

    public Album(long albumId, String singer, String albumName) {
        this.albumId = albumId;
        this.singer = singer;
        this.albumName = albumName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
