package com.josfloy.writenumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.josfloy.writenumber.util.MusicPool;

public class SelectActivity extends Activity {
    MusicPool mMusicPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mMusicPool = new MusicPool();
        mMusicPool.initMediaPlayer(this, R.raw.music1);
        if (MainActivity.isPlay) {
            PlayMusic();
        }
    }

    private void PlayMusic() {
        mMusicPool.playMusic();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (MainActivity.isPlay) {
            PlayMusic();
        }
    }

    public void OnOne(View v) {
        startActivity(new Intent(SelectActivity.this, OneActivity.class));
    }
}
