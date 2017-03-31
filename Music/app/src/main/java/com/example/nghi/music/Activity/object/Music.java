package com.example.nghi.music.Activity.object;

import java.io.Serializable;

/**
 * Created by Nghi on 1/5/17.
 */

public class Music implements Serializable {
    private String name;
    private String singer;
    private String path;
    private String album;
    private long id;
    private String duration;

    public Music(String name, String singer, String path, String album, String duration) {
        this.name = name;
        this.singer = singer;
        this.path = path;
        this.album = album;
        this.duration = duration;
    }

    public Music(String name, String singer, String path, String album, long id, String duration) {
        this.name = name;
        this.singer = singer;
        this.path = path;
        this.album = album;
        this.id = id;
        this.duration = duration;
    }

//    public Music(String name, String singer, String path, String album) {
//        this.name = name;
//        this.singer = singer;
//        this.path = path;
//        this.album = album;
//    }
//


    public Music() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Music(String name, String singer) {
        this.name = name;
        this.singer = singer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
