package com.atguigu.mediaplayexer.videoPlayer;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.mediaplayexer.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_audio_player);
        findViews();
        //设置音频频谱
        setAudioFrequency();

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

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-05-25 15:53:53 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
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

}
