package com.atguigu.mediaplayexer.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.atguigu.mediaplayexer.BaseFragment;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NetAudioFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @Override
    protected void initData() {
        textView.setText("网络音频页面");
    }
}
