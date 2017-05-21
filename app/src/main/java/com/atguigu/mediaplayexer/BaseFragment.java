package com.atguigu.mediaplayexer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/5/19.
 */

public abstract class BaseFragment extends Fragment {
    public Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

    }

    protected abstract void initData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }

    public abstract View initView();


}

