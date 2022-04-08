package com.sm.music.Bean;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
//获取音频文件，并且对歌曲名、歌手和时间等进行格式规范
public class Utils {
    public static List<LocMus> list;

    public static LocMus song;


    public static List<LocMus> getmusic(Context context) {

        list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                song = new LocMus();
                song.setMusicname(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                list.add(song);
                }

            }


        return list;

    }



    }


