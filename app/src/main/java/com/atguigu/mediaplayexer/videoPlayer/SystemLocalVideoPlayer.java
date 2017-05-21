package com.atguigu.mediaplayexer.videoPlayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.atguigu.mediaplayexer.R;

public class SystemLocalVideoPlayer extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化所有控件
        findViews();
        //设置监听
        setListener();


    }

    private void setListener() {
        //设置播放的三个监听事件
        vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_player.start();
                //设置

            }
        });

        vv_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        vv_player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        //设置播放地址
        Intent intent = getIntent();
        Uri uri = intent.getData();
        vv_player.setVideoURI(uri);

        vv_player.setMediaController(new MediaController(this));
    }

    private VideoView vv_player;
    private RelativeLayout llVideoInfo;
    private TextView tvVideoName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private LinearLayout llTopControls;
    private ImageButton ibVolune;
    private SeekBar sbVolumeControl;
    private ImageButton ivShera;
    private LinearLayout llBottemControls;
    private TextView tvVideoTime;
    private SeekBar sbVideoPragressControl;
    private TextView tvVideoTotaltime;
    private LinearLayout llControlButton;
    private ImageButton ibBack;
    private ImageButton ibPre;
    private ImageButton ibSwitchcontrol;
    private ImageButton ibNext;
    private ImageButton ibFullscreen;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-21 20:18:24 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_local_video_player);
        vv_player = (VideoView) findViewById(R.id.vv_player);
        llVideoInfo = (RelativeLayout) findViewById(R.id.ll_video_info);
        tvVideoName = (TextView) findViewById(R.id.tv_video_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        llTopControls = (LinearLayout) findViewById(R.id.ll_top_controls);
        ibVolune = (ImageButton) findViewById(R.id.ib_volune);
        sbVolumeControl = (SeekBar) findViewById(R.id.sb_volume_control);
        ivShera = (ImageButton) findViewById(R.id.iv_shera);
        llBottemControls = (LinearLayout) findViewById(R.id.ll_bottem_controls);
        tvVideoTime = (TextView) findViewById(R.id.tv_video_time);
        sbVideoPragressControl = (SeekBar) findViewById(R.id.sb_video_pragress_control);
        tvVideoTotaltime = (TextView) findViewById(R.id.tv_video_totaltime);
        llControlButton = (LinearLayout) findViewById(R.id.ll_control_button);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibPre = (ImageButton) findViewById(R.id.ib_pre);
        ibSwitchcontrol = (ImageButton) findViewById(R.id.ib_switchcontrol);
        ibNext = (ImageButton) findViewById(R.id.ib_next);
        ibFullscreen = (ImageButton) findViewById(R.id.ib_fullscreen);

        ibVolune.setOnClickListener(this);
        ivShera.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        ibPre.setOnClickListener(this);
        ibSwitchcontrol.setOnClickListener(this);
        ibNext.setOnClickListener(this);
        ibFullscreen.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-21 20:18:24 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == ibVolune) {
            // Handle clicks for ibVolune
        } else if (v == ivShera) {
            // Handle clicks for ivShera
        } else if (v == ibBack) {
            // Handle clicks for ibBack
        } else if (v == ibPre) {
            // Handle clicks for ibPre
        } else if (v == ibSwitchcontrol) {
            // Handle clicks for ibSwitchcontrol
        } else if (v == ibNext) {
            // Handle clicks for ibNext
        } else if (v == ibFullscreen) {
            // Handle clicks for ibFullscreen
        }
    }


}
