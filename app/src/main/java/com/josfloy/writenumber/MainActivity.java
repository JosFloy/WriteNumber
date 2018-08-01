package com.josfloy.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    static boolean isPlay = true;  //音乐播放状态变量
    MediaPlayer mMediaPlayer;
    Button music_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music_btn = findViewById(R.id.btn_music);
        PlayMusic();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isPlay) {
            PlayMusic();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 进入主界面时播放音乐
     */
    private void PlayMusic() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.main_music);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    public void OnPlay(View v) {
        startActivity(new Intent(MainActivity.this, SelectActivity.class));
    }

    public void OnAbout(View v) {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    public void OnMusic(View v) {
        if (isPlay) {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                music_btn.setBackgroundResource(R.drawable.btn_music2);
                isPlay = false;
            } else {
                PlayMusic();
                music_btn.setBackgroundResource(R.drawable.btn_about1);
                isPlay = true;
            }
        }
    }

}
