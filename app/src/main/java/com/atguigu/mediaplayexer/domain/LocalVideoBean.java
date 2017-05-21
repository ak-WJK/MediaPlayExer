package com.atguigu.mediaplayexer.domain;

/**
 * Created by Administrator on 2017/5/19.
 */

public class LocalVideoBean {
    private String videoName;
    private long size;
    private long duration;
    private String videoAddress;

    public LocalVideoBean(String videoName, long size, long duration, String videoAddress) {
        this.videoName = videoName;
        this.size = size;
        this.duration = duration;
        this.videoAddress = videoAddress;
    }

    public LocalVideoBean() {
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    @Override
    public String toString() {
        return "LocalVideoBean{" +
                "videoName='" + videoName + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", videoAddress='" + videoAddress + '\'' +
                '}';
    }
}
