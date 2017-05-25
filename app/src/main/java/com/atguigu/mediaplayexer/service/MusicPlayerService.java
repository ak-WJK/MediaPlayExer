package com.atguigu.mediaplayexer.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;

import com.atguigu.mediaplayexer.IMusicPlayerService;
import com.atguigu.mediaplayexer.domain.LocalVideoBean;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    private int position;
    private MediaPlayer mediaPlayer;
    private LocalVideoBean bean;

    public MusicPlayerService() {
    }

    IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void openMusic(int position) throws RemoteException {

            service.openMusic(position);


        }

        @Override
        public void preMusic() throws RemoteException {
            service.preMusic();
        }

        @Override
        public void NextNusic() throws RemoteException {
            service.NextNusic();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public int getDuration() throws RemoteException {
            return 0;
        }

        @Override
        public int getCurrentDuration() throws RemoteException {
            return 0;
        }

        @Override
        public String getArtistName() throws RemoteException {
            return null;
        }

        @Override
        public String getMusicName() throws RemoteException {
            return null;
        }

        @Override
        public void seekTo(int position) throws RemoteException {

        }
    };


    private ArrayList<LocalVideoBean> mDatas;

    @Override
    public IBinder onBind(Intent intent) {

        return stub;
    }


    /**
     * 点击播放对应的音频
     *
     * @param position
     */
    public void openMusic(int position) {
        this.position = position;

        if (mDatas != null && mDatas.size() > 0) {

            if (position < mDatas.size()) {

                bean = mDatas.get(position);

                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer = null;
                }
                mediaPlayer = new MediaPlayer();

                try {
                    //设置播放地址
                    mediaPlayer.setDataSource(bean.getVideoAddress());


                    //设置播放的三个监听状态
                    mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                    mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                    mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                    //异步加载
                    mediaPlayer.prepareAsync();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();

        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            NextNusic();

        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            NextNusic();
            return true;
        }
    }

    /**
     * 播放音频
     */
    public void start() {
        mediaPlayer.start();

    }


    /**
     * 播放上一个
     */
    public void preMusic() {


    }

    /**
     * 暂停
     */
    public void pause() {
        mediaPlayer.pause();

    }


    /**
     * 播放下一个
     */
    public void NextNusic() {

    }


    /**
     * @return 得到时长
     */
    public int getDuration() {

        return 0;
    }

    /**
     * @return 得到当前时间
     */
    public int getCurrentDuration() {
        return 0;
    }

    /**
     * @return 得到艺术家
     */
    public String getArtistName() {

        return "";
    }

    /**
     * @return 得到音乐名称
     */
    public String getMusicName() {
        return "";
    }

    /**
     * @param position 得到进度
     */
    public void seekTo(int position) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDatas();
    }

    public void initDatas() {

        new Thread() {


            @Override
            public void run() {
                super.run();

                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] obj = {
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST

                };
                Cursor cursor = resolver.query(uri, obj, null, null, null);
                if (cursor != null) {
                    mDatas = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        String Address = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        Log.e("TAG", "name " + name);
                        if (duration > 10 * 1000) {
                            mDatas.add(new LocalVideoBean(name, size, duration, Address, artist));
                        }

                    }
                }

                cursor.close();


            }
        }.start();


    }


}

