package com.atguigu.mediaplayexer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.mediaplayexer.BaseFragment;
import com.atguigu.mediaplayexer.R;
import com.atguigu.mediaplayexer.adapter.NetVideoAdapter;
import com.atguigu.mediaplayexer.domain.LocalVideoBean;
import com.atguigu.mediaplayexer.domain.MoveInfo;
import com.atguigu.mediaplayexer.videoPlayer.SystemLocalVideoPlayer;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class NetVideoFragment extends BaseFragment {

    private ListView lv;
    private TextView tv_nodata;
    private NetVideoAdapter adapter;
    private ArrayList<LocalVideoBean> mDatas;

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.net_video_layout, null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MoveInfo.TrailersBean item = adapter.getItem(position);

//                Intent intent = new Intent(context, SystemLocalVideoPlayer.class);
//                intent.setDataAndType(Uri.parse(item.getUrl()), "video/*");
//                startActivity(intent);

                if (mDatas != null && mDatas.size() > 0) {

                    Intent intent = new Intent(context, SystemLocalVideoPlayer.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mDatas", mDatas);
                    intent.putExtras(bundle);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }

            }
        });

        return view;
    }

    @Override
    protected void initData() {
        getDataFromNet();

    }

    public void getDataFromNet() {
        RequestParams request = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(request, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "result" + result);
                processData(result);
                //手动解析数据传递列表实现点击播放上一个下一个
                try {
                    analysisJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "onError" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    //手动解析数据
    private void analysisJson(String result) throws JSONException {

        JSONObject jsonObject = new JSONObject(result);

        mDatas = new ArrayList<>();

        JSONArray trailers = jsonObject.optJSONArray("trailers");
        if (trailers != null && trailers.length() > 0) {
            for (int i = 0; i < trailers.length(); i++) {
                JSONObject object = (JSONObject) trailers.get(i);

                String movieName = object.optString("movieName");
                String url = object.optString("url");
                String coverImg = object.optString("coverImg");
                long videoLength = object.optLong("videoLength");

                mDatas.add(new LocalVideoBean(movieName, videoLength, videoLength, url));


            }

        }


    }

    private void processData(String result) {
        MoveInfo moveInfo = new Gson().fromJson(result, MoveInfo.class);
        List<MoveInfo.TrailersBean> datas = moveInfo.getTrailers();
        if (datas != null && datas.size() > 0) {

            tv_nodata.setVisibility(View.GONE);

            adapter = new NetVideoAdapter(context, datas);
            lv.setAdapter(adapter);

        } else {
            tv_nodata.setVisibility(View.VISIBLE);
        }


    }
}
