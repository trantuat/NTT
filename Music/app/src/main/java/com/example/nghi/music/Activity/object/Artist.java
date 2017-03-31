package com.example.nghi.music.Activity.object;

import java.io.Serializable;

/**
 * Created by Nghi on 1/6/17.
 */

public class Artist implements Serializable {
    private String nameSinger;
    private long singerId;
    private int numberOfSong;

    public Artist(String nameSinger, long singerId, int numberMusic) {
        this.nameSinger = nameSinger;
        this.numberOfSong = numberMusic;
        this.singerId = singerId;
    }

    public int getNumberOfSong() {
        return numberOfSong;
    }

    public void setNumberOfSong(int numberOfSong) {
        this.numberOfSong = numberOfSong;
    }
    //    public Artist() {
//    }
//
//    public Artist(String nameSinger, long singerId) {
//        this.nameSinger = nameSinger;
//        this.singerId = singerId;
//    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public long getSingerId() {
        return singerId;
    }

    public void setSingerId(long singerId) {
        this.singerId = singerId;
    }
}
