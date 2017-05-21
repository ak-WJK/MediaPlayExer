package com.atguigu.mediaplayexer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.atguigu.mediaplayexer.fragment.LocalAudioFragment;
import com.atguigu.mediaplayexer.fragment.LocalVideoFragment;
import com.atguigu.mediaplayexer.fragment.NetAudioFragment;
import com.atguigu.mediaplayexer.fragment.NetVideoFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        initFragment();
        rg_main.check(R.id.rb_local_video);
        isGrantExternalRW(this);

    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoFragment());
        fragments.add(new LocalAudioFragment());
        fragments.add(new NetVideoFragment());
        fragments.add(new NetAudioFragment());

    }

    private int position;
    private Fragment tempFragment;

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            switch (checkedId) {
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_local_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
            }
            BaseFragment currFragment = fragments.get(position);
            addFragment(currFragment);
        }

    }
    private void addFragment(Fragment currFragment) {
        if (tempFragment != currFragment) {
            //开启事务
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();

            if (!currFragment.isAdded()) {
                if (tempFragment != null) {
                    fm.hide(tempFragment);
                }
                fm.add(R.id.fl_add_fragment, currFragment);

            } else {

                if (tempFragment != null) {
                    fm.hide(tempFragment);
                }
                fm.show(currFragment);
            }
            fm.commit();
            tempFragment = currFragment;

        }


    }


    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     *
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


}
