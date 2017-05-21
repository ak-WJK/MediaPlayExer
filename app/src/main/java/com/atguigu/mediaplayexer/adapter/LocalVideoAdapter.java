package com.atguigu.mediaplayexer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.mediaplayexer.R;
import com.atguigu.mediaplayexer.domain.LocalVideoBean;
import com.atguigu.mediaplayexer.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/20.
 */

public class LocalVideoAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<LocalVideoBean> mDatas;
    private Utils utils;

    public LocalVideoAdapter(Context context, ArrayList<LocalVideoBean> mDatas) {

        this.context = context;
        this.mDatas = mDatas;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public LocalVideoBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.local_video_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LocalVideoBean videoBean = mDatas.get(position);

        viewHolder.tvName.setText(videoBean.getVideoName());
        viewHolder.tvDuration.setText(utils.stringForTime((int) videoBean.getDuration()));
        viewHolder.tvSize.setText(Formatter.formatFileSize(context, videoBean.getSize()));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_duration)
        TextView tvDuration;
        @Bind(R.id.tv_size)
        TextView tvSize;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
