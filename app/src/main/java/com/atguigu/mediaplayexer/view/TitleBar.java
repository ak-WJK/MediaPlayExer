package com.atguigu.mediaplayexer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.mediaplayexer.R;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    private final Context context;
    private TextView tv_search;
    private RelativeLayout rl_game;
    private ImageButton ib_history;

    public TitleBar(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_search = (TextView) getChildAt(1);
        rl_game = (RelativeLayout) getChildAt(2);
        ib_history = (ImageButton) getChildAt(3);


        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        ib_history.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Toast.makeText(context, "搜索", Toast.LENGTH_SHORT).show();

                break;
            case R.id.rl_game:
                Toast.makeText(context, "游戏", Toast.LENGTH_SHORT).show();

                break;
            case R.id.ib_history:
                Toast.makeText(context, "历史", Toast.LENGTH_SHORT).show();

                break;
        }

    }
}
