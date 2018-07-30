package com.josfloy.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //代码中实现全屏
        //        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      /*  this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_start);

        Timer timer = new Timer();
        //创建TimerTask
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //从启动界面跳转到游戏主界面
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        };

        timer.schedule(timerTask, 2000);//设置显示启动界面2s后，跳转到游戏主界面
    }
}
