package com.atguigu.mediaplayexer.videoPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.atguigu.mediaplayexer.R;
import com.atguigu.mediaplayexer.domain.LocalVideoBean;
import com.atguigu.mediaplayexer.utils.Utils;
import com.atguigu.mediaplayexer.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemLocalVideoPlayer extends AppCompatActivity implements View.OnClickListener {

    private static final int SHOW_HIDE_CONTROL = 2;
    private static final int DEFUALT_SCREEN = 3;
    private static final int FULL_SCREEN = 4;
    private static final int SHOW_NET_SPEED = 5;
    private RelativeLayout rl_layout;
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
    private LinearLayout ll_loading;
    private LinearLayout ll_buffering;

    private TextView tv_loading_net_speed;
    private TextView tv_net_speed;

    private static final int PROGRESS = 1;
    private Utils utils;


    private GestureDetector detector;

    private int preCurrrentPosition;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_NET_SPEED:

                    if (isNetUri) {
                        String netSpeed = utils.getNetSpeed(SystemLocalVideoPlayer.this);
                        tv_loading_net_speed.setText("正在加载中...." + netSpeed);

                        tv_net_speed.setText("正在缓冲...." + netSpeed);

                        Log.e("TAG", "netSpeed" + netSpeed);

                        //一定要隔一段时间在发送消息
                        sendEmptyMessageDelayed(SHOW_NET_SPEED, 1000);
                    }


                    break;
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

                    //设置视频缓存
                    if (isNetUri) {

                        int bufferPercentage = vv_player.getBufferPercentage();
                        int totalBuffer = bufferPercentage * sbVideoPragressControl.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        sbVideoPragressControl.setSecondaryProgress(secondaryProgress);
                    } else {
                        sbVideoPragressControl.setSecondaryProgress(0);
                    }

                    //监听视频卡顿
                    if (isNetUri && vv_player.isPlaying()) {
                        int duration = currentPosition - preCurrrentPosition;
                        if (duration < 500) {
                            ll_buffering.setVisibility(View.GONE);
                        } else {
                            ll_buffering.setVisibility(View.VISIBLE);
                        }
                        preCurrrentPosition = currentPosition;
                    }


                    break;
                case SHOW_HIDE_CONTROL:

                    hideControl();

                    break;
            }

        }
    };
    private ArrayList<LocalVideoBean> mDatas;
    private int position;
    private Uri uri;
    private BatteryReceiver receiver;
    private int screenHeight;
    private int screenWidth;
    private int videoHeight;
    private int videoWidth;
    private AudioManager am;
    private int currentVolume;
    private int maxVolume;
    private int volume;
    private int min;
    private float downY;
    private float moveY;

    private boolean isNetUri = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils();
        //初始化所有控件
        findViews();

        //实现手势识别器
        setGestureDetector();
        //得到音量
        getVolume();

        //关联最大音量
        sbVolumeControl.setMax(maxVolume);
        //设置当前进度
        sbVolumeControl.setProgress(currentVolume);


        //得到播放列表
        getmDatas();


        //设置监听
        setListener();

        //设置播放列表
        setmDatas();


        //得到电量
        getBattery();


    }

    private boolean isFullScreen = false;

    private void setGestureDetector() {
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {

                setPlayerAndPause();

                super.onLongPress(e);


            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //得到屏幕的宽和高
                getScreenWidthAndHeight();

                if (isFullScreen) {
                    setVideoType(DEFUALT_SCREEN);
                } else {
                    setVideoType(FULL_SCREEN);
                }

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isShow) {
                    hideControl();
                    handler.removeMessages(SHOW_HIDE_CONTROL);
                } else {
                    showControl();
                    handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);
                }


                return super.onSingleTapConfirmed(e);

            }


        });


        //得到屏幕的宽和高
        getScreenWidthAndHeight();


    }

    private void getVolume() {
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private void getScreenWidthAndHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    private boolean isShow;

    public void showControl() {
        rl_layout.setVisibility(View.VISIBLE);
        isShow = true;

    }

    public void hideControl() {
        rl_layout.setVisibility(View.GONE);
        isShow = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将事件传入手势识别器
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                min = Math.min(screenWidth, screenHeight);

                handler.removeMessages(SHOW_HIDE_CONTROL);

                break;
            case MotionEvent.ACTION_MOVE:

                moveY = event.getY();
                float distanceY = downY - moveY;

                float delta = (distanceY / min) * maxVolume;
                if (delta != 0) {
                    float mVolume = Math.min(Math.max(delta + volume, 0), maxVolume);

                    updateVolume((int) mVolume);
                }


                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);
                break;
        }


        return super.onTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVolume--;
            updateVolume(currentVolume);

            handler.removeMessages(SHOW_HIDE_CONTROL);
            handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            currentVolume++;
            updateVolume(currentVolume);


            handler.removeMessages(SHOW_HIDE_CONTROL);
            handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void setListener() {
        //设置播放的三个监听事件
        vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //得到视频的宽和高
                videoHeight = mp.getVideoHeight();
                videoWidth = mp.getVideoWidth();


                vv_player.start();
                //的到播放视频的时长
                int duration = vv_player.getDuration();
                //设置视频的时长
                tvVideoTotaltime.setText(utils.stringForTime(duration));
                //把视频的总时长设置为seekBar的总长度
                sbVideoPragressControl.setMax(duration);
                //发送消息
                handler.sendEmptyMessage(PROGRESS);

                handler.removeMessages(SHOW_HIDE_CONTROL);
                handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);

                //设置为隐藏
                ll_loading.setVisibility(View.GONE);

                //设置默认为视频本身大小
                setVideoType(DEFUALT_SCREEN);
            }
        });

        vv_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //设置自动播放下一个视频
                setplayerNext();
            }

        });

        vv_player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        //设置播放地址
//        Intent intent = getIntent();
//        Uri uri = intent.getData();
//        vv_player.setVideoURI(uri);

//        vv_player.setMediaController(new MediaController(this));

        //设置视频播放seekBar的监听
        sbVideoPragressControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    vv_player.seekTo(progress);

                    handler.removeMessages(SHOW_HIDE_CONTROL);
                    handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置音量的监听
        sbVolumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateVolume(progress);
                    handler.removeMessages(SHOW_HIDE_CONTROL);
                    handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);
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

    private boolean isMute = false;

    private void updateVolume(int progress) {

        currentVolume = progress;

        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        sbVolumeControl.setProgress(currentVolume);

        if (currentVolume <= 0) {
            isMute = true;
        } else {
            isMute = false;
        }


    }

    //设置自动播放下一个视频
    private void setplayerNext() {

        position++;
        if (position < mDatas.size()) {
            LocalVideoBean videoBean = mDatas.get(position);
            //得到并设置视频名称
            tvVideoName.setText(videoBean.getVideoName());
            //得到并设置播放地址
            vv_player.setVideoPath(videoBean.getVideoAddress());

            isNetUri = utils.isNetUri(videoBean.getVideoAddress());


            ll_loading.setVisibility(View.VISIBLE);

            //设置点击按钮状态
            setButtonStatus();

        } else {
            finish();
        }


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
            isMute = !isMute;
            volumeStart(isMute);

            handler.removeMessages(SHOW_HIDE_CONTROL);
            handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);


        } else if (v == ivShera) {
            // Handle clicks for ivShera
        } else if (v == ibBack) {
            finish();
        } else if (v == ibPre) {
            setPlayerPre();

            handler.removeMessages(SHOW_HIDE_CONTROL);
            handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);

        } else if (v == ibSwitchcontrol) {
            setPlayerAndPause();

            handler.removeMessages(SHOW_HIDE_CONTROL);
            handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);

        } else if (v == ibNext) {
            setplayerNext();

            handler.removeMessages(SHOW_HIDE_CONTROL);
            handler.sendEmptyMessageDelayed(SHOW_HIDE_CONTROL, 3000);

        } else if (v == ibFullscreen) {
            //得到屏幕的宽和高
            getScreenWidthAndHeight();
            if (isFullScreen) {
                setVideoType(DEFUALT_SCREEN);
            } else {
                setVideoType(FULL_SCREEN);
            }
        }
    }

    //设置是否静音
    private void volumeStart(boolean isMute) {
        if (isMute) {
            //静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            sbVolumeControl.setProgress(0);
        } else {
            //非静音
            am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            sbVolumeControl.setProgress(currentVolume);
        }
    }

    private void setPlayerPre() {
        position--;
        if (position > 0) {

            LocalVideoBean videoBean = mDatas.get(position);
            //得到并设置视频名称
            tvVideoName.setText(videoBean.getVideoName());
            //得到并设置播放地址
            vv_player.setVideoPath(videoBean.getVideoAddress());

            isNetUri = utils.isNetUri(videoBean.getVideoAddress());

            ll_loading.setVisibility(View.VISIBLE);

            //设置按钮的点击状态
            setButtonStatus();

        } else {
            finish();
        }


    }

    private void setButtonStatus() {

        if (mDatas != null && mDatas.size() > 0) {

            setEnable(true);

            if (position == 0) {
                ibPre.setBackgroundResource(R.drawable.btn_pre_gray);
                ibPre.setEnabled(false);
            }
            if (position == mDatas.size() - 1) {
                ibPre.setBackgroundResource(R.drawable.btn_next_gray);
                ibNext.setEnabled(false);
            }

        } else if (uri != null) {
            setEnable(false);
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

        receiver = new BatteryReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

    }

    //设置播放列表
    private void setmDatas() {
        if (mDatas != null && mDatas.size() > 0) {

            LocalVideoBean videoBean = mDatas.get(position);

            //得到播放地址
            vv_player.setVideoPath(videoBean.getVideoAddress());
            tvVideoName.setText(videoBean.getVideoName());

            //判断是否是网络资源
            isNetUri = utils.isNetUri(videoBean.getVideoAddress());

        } else if (uri != null) {
            vv_player.setVideoURI(uri);
            tvVideoName.setText(uri.toString());
            //判断是否是网络资源
            isNetUri = utils.isNetUri(uri.toString());

        }

        //设置点击按钮状态
        setButtonStatus();

    }


    //得到数据列表
    public void getmDatas() {

        uri = getIntent().getData();

        mDatas = (ArrayList<LocalVideoBean>) getIntent().getSerializableExtra("mDatas");
        position = getIntent().getIntExtra("position", 0);

//        Log.e("TAG", "mData " + mDatas.size());
//        Log.e("TAG", "position " + position);


    }

    //通用设置按钮状态
    public void setEnable(boolean b) {
        if (b) {
            ibPre.setBackgroundResource(R.drawable.media_pre_control_select);
            ibNext.setBackgroundResource(R.drawable.media_next_control_select);

        } else {
            ibPre.setBackgroundResource(R.drawable.btn_pre_gray);
            ibNext.setBackgroundResource(R.drawable.btn_next_gray);

        }

        ibPre.setEnabled(b);
        ibNext.setEnabled(b);

    }

    //设置是否全屏显示
    public void setVideoType(int videoType) {
        switch (videoType) {
            case FULL_SCREEN:
                isFullScreen = true;
                ibFullscreen.setBackgroundResource(R.drawable.media_fullscreen2_control_select);

                //设置视频为全屏
                vv_player.setVideoSize(screenWidth, screenHeight);

                break;
            case DEFUALT_SCREEN:
                isFullScreen = false;
                ibFullscreen.setBackgroundResource(R.drawable.media_fullscreen_control_select);

                //视频原生的宽和高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;

                //计算好的要显示的视频的宽和高
                int width = screenWidth;
                int height = screenHeight;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                vv_player.setVideoSize(width, height);


                break;
        }


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


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-21 20:18:24 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {

        setContentView(R.layout.activity_system_local_video_player);

        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
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
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_buffering = (LinearLayout) findViewById(R.id.ll_buffering);
        tv_loading_net_speed = (TextView) findViewById(R.id.tv_loading_net_speed);
        tv_net_speed = (TextView) findViewById(R.id.tv_net_speed);

        ibVolune.setOnClickListener(this);
        ivShera.setOnClickListener(this);
        ibBack.setOnClickListener(this);
        ibPre.setOnClickListener(this);
        ibSwitchcontrol.setOnClickListener(this);
        ibNext.setOnClickListener(this);
        ibFullscreen.setOnClickListener(this);

//发送得到网速的消息
        handler.sendEmptyMessage(SHOW_NET_SPEED);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {

            unregisterReceiver(receiver);
            receiver = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}
