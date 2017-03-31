package com.example.nghi.music.Activity.object;

import java.io.Serializable;

/**
 * Created by Nghi on 1/7/17.
 */

public class PlayList implements Serializable {
    private String namePlayList;
    private int playListId;
    private int numberOfSong=0;



    public PlayList(String namePlayList, int numberOfSong, int playListId) {
        this.namePlayList = namePlayList;
        this.numberOfSong = numberOfSong;
        this.playListId = playListId;
    }

    public PlayList(String namePlayList) {
        this.namePlayList = namePlayList;
    }

    public String getNamePlayList() {
        return namePlayList;
    }

    public void setNamePlayList(String namePlayList) {
        this.namePlayList = namePlayList;
    }

    public int getPlayListId() {
        return playListId;
    }

    public void setPlayListId(int playListId) {
        this.playListId = playListId;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public void setNumberOfSong(int numberOfSong) {
        this.numberOfSong = numberOfSong;
    }
}
