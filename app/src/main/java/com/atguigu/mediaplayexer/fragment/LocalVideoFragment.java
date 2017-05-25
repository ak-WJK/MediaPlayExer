package com.atguigu.mediaplayexer.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mediaplayexer.BaseFragment;
import com.atguigu.mediaplayexer.R;
import com.atguigu.mediaplayexer.adapter.LocalVideoAdapter;
import com.atguigu.mediaplayexer.domain.LocalVideoBean;
import com.atguigu.mediaplayexer.videoPlayer.SystemVideoPlayer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/19.
 */

public class LocalVideoFragment extends BaseFragment {


    private ListView listview;
    private TextView tv_hint;
    private ArrayList<LocalVideoBean> mDatas;
    private LocalVideoAdapter adapter;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (mDatas != null) {
                tv_hint.setVisibility(View.GONE);


                adapter = new LocalVideoAdapter(context, mDatas, true);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new MyOnItemClickListener());


            } else {
                tv_hint.setVisibility(View.VISIBLE);
            }


        }
    };

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            LocalVideoBean item = adapter.getItem(position);

//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(item.getVideoAddress()), "video/*");
//            startActivity(intent);
//


//            Intent intent = new Intent(context,SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(item.getVideoAddress()), "video/*");
//            startActivity(intent);

            //播放列表的传递
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("mDatas", mDatas);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            startActivity(intent);


        }
    }


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.local_video_layout, null);
        listview = (ListView) view.findViewById(R.id.listview);
        tv_hint = (TextView) view.findViewById(R.id.tv_hint);

        return view;
    }

    @Override
    protected void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] obj = {
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DURATION

                };
                Cursor cursor = resolver.query(uri, obj, null, null, null);
                if (cursor != null) {
                    mDatas = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        String videoName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        String videoAddress = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                        mDatas.add(new LocalVideoBean(videoName, size, duration, videoAddress));

                    }
                }

                cursor.close();
                handler.sendEmptyMessage(0);

            }
        }.start();


    }
}
