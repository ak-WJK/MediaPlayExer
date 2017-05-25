package com.atguigu.mediaplayexer.videoPlayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.mediaplayexer.IMusicPlayerService;
import com.atguigu.mediaplayexer.R;
import com.atguigu.mediaplayexer.service.MusicPlayerService;

public class SystemAudioPlayer extends AppCompatActivity implements View.OnClickListener {
    private ImageView audio_frequency;

    private RelativeLayout llBottemControls;
    private TextView tvAudioTime;
    private SeekBar sbAudioPragressControl;
    private LinearLayout llControlButton;
    private ImageButton ibFor;
    private ImageButton ibPre;
    private ImageButton ibSwitchcontrol;
    private ImageButton ibNext;
    private ImageButton ibGeci;
    private Intent intent;
    private IMusicPlayerService service;
    private int position;
    private ServiceConnection conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_audio_player);
        findViews();
        //设置音频频谱
        setAudioFrequency();

        //得到点击传过来的位置
        getPosition();

        //启动播放音乐的服务
        startPlayerMusicService();


    }

    private void getPosition() {

        position = getIntent().getIntExtra("position", 0);
//        Log.e("TAG", "position " + position);
    }

    private void startPlayerMusicService() {

        intent = new Intent(this, MusicPlayerService.class);
        //连接服务的时候回调
        conn = new ServiceConnection() {
            //连接服务的时候回调
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                service = IMusicPlayerService.Stub.asInterface(iBinder);
                if (service != null) {
                    try {
                        service.openMusic(position);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }

            }

            //断开连接的时候回调
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        //屏蔽多次创建服务
        startService(intent);

    }

    private void setAudioFrequency() {
        AnimationDrawable background = (AnimationDrawable) audio_frequency.getBackground();
        background.start();
    }


    private void findViews() {
        audio_frequency = (ImageView) findViewById(R.id.audio_frequency);
        llBottemControls = (RelativeLayout) findViewById(R.id.ll_bottem_controls);
        tvAudioTime = (TextView) findViewById(R.id.tv_audio_time);
        sbAudioPragressControl = (SeekBar) findViewById(R.id.sb_audio_pragress_control);
        llControlButton = (LinearLayout) findViewById(R.id.ll_control_button);
        ibFor = (ImageButton) findViewById(R.id.ib_for);
        ibPre = (ImageButton) findViewById(R.id.ib_pre);
        ibSwitchcontrol = (ImageButton) findViewById(R.id.ib_switchcontrol);
        ibNext = (ImageButton) findViewById(R.id.ib_next);
        ibGeci = (ImageButton) findViewById(R.id.ib_geci);

        ibFor.setOnClickListener(this);
        ibPre.setOnClickListener(this);
        ibSwitchcontrol.setOnClickListener(this);
        ibNext.setOnClickListener(this);
        ibGeci.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == ibFor) {
            // Handle clicks for ibFor
        } else if (v == ibPre) {
            // Handle clicks for ibPre
        } else if (v == ibSwitchcontrol) {


            // Handle clicks for ibSwitchcontrol
        } else if (v == ibNext) {
            // Handle clicks for ibNext
        } else if (v == ibGeci) {
            // Handle clicks for ibGeci
        }
    }

    @Override
    protected void onDestroy() {
        if (conn != null) {

            unbindService(conn);

            conn = null;

        }


        super.onDestroy();

    }
}
