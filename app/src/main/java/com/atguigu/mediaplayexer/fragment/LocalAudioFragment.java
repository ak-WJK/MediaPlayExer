package com.atguigu.mediaplayexer.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.atguigu.mediaplayexer.BaseFragment;

/**
 * Created by Administrator on 2017/5/19.
 */

public class LocalAudioFragment extends BaseFragment {
    private TextView textView;
    @Override
    protected void initData() {
        textView.setText("本地音频页面");
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}
