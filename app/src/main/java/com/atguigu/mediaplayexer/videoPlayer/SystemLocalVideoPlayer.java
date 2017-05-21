package com.atguigu.mediaplayexer.videoPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.atguigu.mediaplayexer.R;
import com.atguigu.mediaplayexer.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemLocalVideoPlayer extends AppCompatActivity implements View.OnClickListener {

    private static final int PROGRESS = 1;
    private Utils utils;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    //的到前的进度
                    int currentPosition = vv_player.getCurrentPosition();
                    //设置到seekBar
                    sbVideoPragressControl.setProgress(currentPosition);
                    //设置到跟进时间
                    tvVideoTime.setText(utils.stringForTime(currentPosition));
                    //不断更新进度
                    handler.sendEmptyMessageDelayed(PROGRESS, 0);
                    //得到系统时间
                    tvSystemTime.setText(getSystemTime());


                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils();
        //初始化所有控件
        findViews();
        //设置监听
        setListener();
        //得到电量
        getBattery();


    }

    private void setListener() {
        //设置播放的三个监听事件
        vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_player.start();
                //的到播放视频的时长
                int duration = vv_player.getDuration();
                //设置视频的时长
                tvVideoTotaltime.setText(utils.stringForTime(duration));
                //把视频的总时长设置为seekBar的总长度
                sbVideoPragressControl.setMax(duration);
                //发送消息
                handler.sendEmptyMessage(PROGRESS);


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

//        vv_player.setMediaController(new MediaController(this));

        //设置视频播放seekBar的监听
        sbVideoPragressControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    vv_player.seekTo(progress);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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
            finish();
        } else if (v == ibPre) {
            // Handle clicks for ibPre
        } else if (v == ibSwitchcontrol) {
            setPlayerAndPause();


        } else if (v == ibNext) {
            // Handle clicks for ibNext
        } else if (v == ibFullscreen) {
            // Handle clicks for ibFullscreen
        }
    }

//设置播放和暂停
    private void setPlayerAndPause() {
        if (vv_player.isPlaying()) {
            vv_player.pause();
            ibSwitchcontrol.setBackgroundResource(R.drawable.media_switchcontrol2_select);
        } else {
            vv_player.start();
            ibSwitchcontrol.setBackgroundResource(R.drawable.media_switchcontrol1_select);
        }
    }


    public String getSystemTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());

    }


    //得到电量改变
    public void getBattery() {

        BatteryReceiver receiver = new BatteryReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

    }

    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            setBatteryStatus(level);
        }

        private void setBatteryStatus(int level) {
            if (level <= 0) {
                ivBattery.setBackgroundResource(R.drawable.ic_battery_0);
            } else if (level > 0 && level <= 10) {
                ivBattery.setImageResource(R.drawable.ic_battery_10);
            } else if (level > 10 && level <= 20) {
                ivBattery.setImageResource(R.drawable.ic_battery_20);
            } else if (level > 20 && level <= 40) {
                ivBattery.setImageResource(R.drawable.ic_battery_40);
            } else if (level > 40 && level <= 60) {
                ivBattery.setImageResource(R.drawable.ic_battery_60);
            } else if (level > 60 && level <= 80) {
                ivBattery.setImageResource(R.drawable.ic_battery_80);
            } else if (level > 80 && level <= 100) {
                ivBattery.setImageResource(R.drawable.ic_battery_100);
            } else {
                ivBattery.setImageResource(R.drawable.ic_battery_100);
            }

        }

    }


}
