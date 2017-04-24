package com.example.nghi.music.Activity.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.nghi.music.Activity.object.Album;
import com.example.nghi.music.Activity.object.Artist;
import com.example.nghi.music.Activity.object.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tran Tuat on 1/10/2017.
 */

public class DatabaseUtil {
    public List<Artist> getArtist(Context context) {
        String colum[] = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };
        String sort = "LOWER(" + MediaStore.Audio.Artists.ARTIST + ") ASC";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, colum,null, null, sort);

        List<Artist> tmp = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int numberSong=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                tmp.add(new Artist(artist,id,numberSong));
                cursor.moveToNext();
            }
            cursor.close();
            return tmp;
        }
        return null;
    }

    public List<Album> getAlbum(Context context) {
        String colum[] = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums._ID,
        };
        String sort = "LOWER(" + MediaStore.Audio.Albums._ID + ") ASC";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, colum,null, null, sort);

        List<Album> tmp = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                tmp.add(new Album(id,artist,album));
                cursor.moveToNext();
            }
            cursor.close();
            return tmp;
        }
        return null;
    }


    public List<Music> getMusic(Context context) {
//        Cursor cursor1 = getActivity().managedQuery(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                new String[] { MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.ARTIST }, null, null,
//                "LOWER(" + MediaStore.Audio.Media.TITLE +") ASC");
        String colum[] = {
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String sort = "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, colum, null, null, sort);

        List<Music> tmp = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Music music = new Music();
                music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setSinger(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                music.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                tmp.add(music);
                cursor.moveToNext();
            }
            cursor.close();
            return tmp;
        }
        return null;

    }

}
