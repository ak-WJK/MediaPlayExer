// IMusicPlayerService.aidl
package com.atguigu.mediaplayexer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


/**
     * 播放音频
     */
     void start();

    /**
     * 点击播放对应的音频
     *
     * @param position
     */
   void openMusic(int position) ;

    /**
     * 播放上一个
     */
     void preMusic() ;

    /**
     * 播放下一个
     */
       void NextNusic();



       /**
     * 暂停
     */
     void pause();


    /**
     * @return 得到时长
     */
     int getDuration() ;

    /**
     * @return 得到当前时间
     */
    int getCurrentDuration();

    /**
     * @return 得到艺术家
     */
     String getArtistName();

    /**
     * @return 得到音乐名称
     */
     String getMusicName();

    /**
     * @param position 得到进度
     */
    void seekTo(int position);

/**
     * @return
     * 是否播放
     */
    boolean isPlayer() ;



}
