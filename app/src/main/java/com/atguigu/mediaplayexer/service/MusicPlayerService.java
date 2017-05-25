package com.atguigu.mediaplayexer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MusicPlayerService extends Service {
    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * 点击播放对应的音频
     *
     * @param position
     */
    public void openMusic(int position) {

    }


    /**
     * 播放上一个
     */
    public void preMusic() {

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

}
