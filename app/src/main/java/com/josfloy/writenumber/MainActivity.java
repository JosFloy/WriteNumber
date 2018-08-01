package com.josfloy.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.josfloy.writenumber.util.MusicPool;

public class MainActivity extends Activity {
    static boolean isPlay = true;  //音乐播放状态变量
    MusicPool mMusicPool;
    Button music_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music_btn = findViewById(R.id.btn_music);
        mMusicPool = new MusicPool();
        mMusicPool.initMediaPlayer(this, R.raw.main_music);

        mMusicPool.playMusic();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isPlay) {
            mMusicPool.stopMusic();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMusicPool.stopMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicPool.destroyMusic();
    }

    public void OnPlay(View v) {
        startActivity(new Intent(MainActivity.this, SelectActivity.class));
    }

    public void OnAbout(View v) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    public void OnMusic(View v) {
        if (isPlay) {
            if (mMusicPool != null) {
                mMusicPool.stopMusic();
                music_btn.setBackgroundResource(R.drawable.btn_music2);
                isPlay = false;
            }
        }else {
            mMusicPool.playMusic();
            music_btn.setBackgroundResource(R.drawable.btn_music1);
            isPlay = true;
        }
    }
}
