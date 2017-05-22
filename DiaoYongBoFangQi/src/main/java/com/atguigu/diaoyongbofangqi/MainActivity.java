package com.atguigu.diaoyongbofangqi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void diaoYongBoFangQi(View v) {
        Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse("http://192.168.31.168:8080/yellow.mp4"),"video/*");
//                intent.setDataAndType(Uri.parse("http://vf1.mtime.cn/Video/2017/05/17/mp4/170517102706759383.mp4"),"video/*");
        intent.setDataAndType(Uri.parse("http://vfx.mtime.cn/Video/2017/05/17/mp4/170517102706759383.mp4"), "video/*");
//                intent.setDataAndType(Uri.parse("http://vf2.mtime.cn/Video/2017/05/17/mp4/170517091619822234.mp4"),"video/*");
        startActivity(intent);


    }
}
