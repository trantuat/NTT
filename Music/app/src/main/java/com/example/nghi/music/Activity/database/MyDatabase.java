package com.example.nghi.music.Activity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.object.PlayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghi on 1/7/17.
 */

public class MyDatabase {
    public static final String DATABASE_NAME = "DB_PLAYLIST";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_MUSIC = "TB_MUSIC";
    public static final String TABLE_PLAYLIST = "TB_NAME_PLAYLIST";

    public static final String COLUMN_PLAYLIST_ID = "_id";
    public static final String COLUMN_PLAYLIST_NAME = "playlistname";
    public static final String COLUMN_NUMBER_SONG = "numbersong";

    public static final String COLUMN_MUSIC_NAME = "musicname";
    public static final String COLUMN_MUSIC_PLAYLIST_ID = "playlist";
    public static final String COLUMN_SINGER = "singer";
    public static final String COLUMN_MUSIC_ID = "_id";
    public static final String COLUMN_MUSIC_DATA = "path";
    public static final String COLUMN_MUSIC_DURATION = "duration";

    private static Context context;
    static SQLiteDatabase db;
    private OpenHelper openHelper;

    public MyDatabase(Context c) {
        this.context = c;
    }

    public MyDatabase open() throws SQLException {
        openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        openHelper.close();
    }

    public int addPlayList(PlayList playList) {
        long id = -1;
        open();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLAYLIST_NAME, playList.getNamePlayList());
        cv.put(COLUMN_NUMBER_SONG, playList.getNumberOfSong());
        id = db.insert(TABLE_PLAYLIST, null, cv);
        close();
        return (int)id;
    }

    public void addListMusic(List<Music> musics,int id) {
        open();
        for (Music music: musics){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_MUSIC_PLAYLIST_ID, id);
            cv.put(COLUMN_MUSIC_NAME, music.getName());
            cv.put(COLUMN_SINGER, music.getSinger());
            cv.put(COLUMN_MUSIC_DATA, music.getPath());
            cv.put(COLUMN_MUSIC_DURATION, music.getDuration());
            cv.put(COLUMN_MUSIC_ID, music.getId());
            db.insert(TABLE_MUSIC, null, cv);
        }
        close();
    }

    public void renamePlaylist(String oldPl, String newPl) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_NAME, newPl);
        db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_NAME + "=?", new String[]{oldPl});
        close();
    }
    public void insertNumberSong(int number, int id) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER_SONG, number);
        db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_ID + "=?", new String[]{String.valueOf(id)});
        close();
    }



    public List<PlayList> getData() {
        open();
        List<PlayList> list = new ArrayList<>();
        PlayList playList = null;
        String select = "SELECT * FROM " + TABLE_PLAYLIST + " ORDER BY " + COLUMN_PLAYLIST_ID;
        Cursor c = db.rawQuery(select, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int ID = c.getInt(c.getColumnIndex(COLUMN_PLAYLIST_ID));
            String name = c.getString(c.getColumnIndex(COLUMN_PLAYLIST_NAME));
            int numberSong = c.getInt(c.getColumnIndex(COLUMN_NUMBER_SONG));
            playList = new PlayList(name,numberSong,ID);
            list.add(playList);
        }
        c.close();
        close();
        return list;
    }


    public List<Music> searchMusicById(int id) {
        List<Music> musics = new ArrayList<>();
        Music music = null;
        String select = "SELECT * FROM " + TABLE_MUSIC + " WHERE " + COLUMN_MUSIC_PLAYLIST_ID + " = " + id;
        open();
        Cursor c = db.rawQuery(select, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            int ID = c.getInt(c.getColumnIndex(COLUMN_MUSIC_ID));
            String name = c.getString(c.getColumnIndex(COLUMN_MUSIC_NAME));
            String singer = c.getString(c.getColumnIndex(COLUMN_SINGER));
            String path = c.getString(c.getColumnIndex(COLUMN_MUSIC_DATA));
            String duration = c.getString(c.getColumnIndex(COLUMN_MUSIC_DURATION));
            music = new Music(name,singer,path,null,ID,duration);
            musics.add(music);

        }
        close();
        c.close();
        return musics;
    }

    public void deletePlayList(PlayList playList) {
        try {
            open();
            db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + "=" + playList.getPlayListId() , null);
            db.delete(TABLE_MUSIC, COLUMN_MUSIC_PLAYLIST_ID + "=" + playList.getPlayListId() , null);
            close();
        }catch (Exception e){

        }finally {
            close();
        }


    }


    public boolean isExistedInTable(String str, String table) {
        open();
        Cursor cursor;
        String select = "SELECT * FROM " + table + " WHERE " + COLUMN_PLAYLIST_NAME + " LIKE '" + str + "'";
        cursor = db.rawQuery(select, null);
        if (cursor != null && cursor.getCount() > 0) {
            close();
            return true;
        }
        close();
        return false;
    }


    static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_PLAYLIST + " ("
                    + COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NUMBER_SONG + " INTEGER NOT NULL, "
                    + COLUMN_PLAYLIST_NAME + " TEXT NOT NULL)"
                    + ";");
            db.execSQL("CREATE TABLE " + TABLE_MUSIC + " ("
                    + COLUMN_MUSIC_ID + " INTEGER NOT NULL, "
                    + COLUMN_MUSIC_NAME + " TEXT NOT NULL, "
                    + COLUMN_SINGER + " TEXT NOT NULL, "
                    + COLUMN_MUSIC_PLAYLIST_ID + " TEXT NOT NULL, "
                    + COLUMN_MUSIC_DATA + " TEXT NOT NULL, "
                    + COLUMN_MUSIC_DURATION + " TEXT NOT NULL)"
                    + ";");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
            onCreate(db);

        }
    }
}
