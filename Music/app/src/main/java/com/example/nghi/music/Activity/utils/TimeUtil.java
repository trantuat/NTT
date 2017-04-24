package com.example.nghi.music.Activity.utils;

/**
 * Created by Nghi on 1/9/17.
 */

public class TimeUtil {
    public static String convertDuration(long s) {
        long time = s;
        time /= 1000;
        int hours = (int) time / 3600;
        time = time % 3600;
        int minutes = (int) time / 60;
        time = time % 60;
        int seconds = (int) time;
        String results = "";
        String s1 = "";
        s1 += seconds >= 10 ? seconds : "0" + seconds;
        String m1 = "";
        m1 += minutes >= 10 ? minutes + ":" : "0" + minutes + ":";
        String h1 = "";
        if (hours > 0) {
            h1 += hours >= 10 ? hours + ":" : "0" + hours + ":";
        }
        results = h1 + m1 + s1;
        return results;

    }

}
