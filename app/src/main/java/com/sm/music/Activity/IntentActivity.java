package com.sm.music.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.sm.music.Bean.LocMus;
import com.sm.music.Bean.RecMusic;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class IntentActivity extends AppCompatActivity {
    //定义一个集合，存放从本地读取到的内容
    public static List<LocMus> list;

    public static LocMus song;
    private static String name;
    private static String singer;
    private static String path;
    private static int duration;
    private static long size;
    private static long albumId;
    private static long id;
    //获取专辑封面的Uri
   // private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    public static List<LocMus> getmusic(Context context) {

        list = new ArrayList<>();


        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                song = new LocMus();
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //list.add(song);
                //把歌曲名字和歌手切割开
                //song.setName(name);
                song.setMusicname(name);
                song.setLocation(path);
                //  song.setDuration(duration);
                //  song.setSize(size);
               //  song.setMusicid(id);
                // song.setAlbumId(albumId);
                if (size > 1000 * 800) {
                    if (name.contains("-")) {
                        String[] str = name.split("-");
                        singer = str[0];
                        //song.setSinger(singer);
                        name = str[1];
                        song.setMusicname(name);
                    } else {
                        song.setMusicname(name);
                    }
                    list.add(song);
                }
            }
        }
        cursor.close();
        return list;
    }




    public static String getAlbumArt(Context context,long album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Long.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        String path = null;
        if (album_art != null) {
            path = album_art;
        } else {
            //path = "drawable/music_no_icon.png";
            //bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_cover);
        }
        return path;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

