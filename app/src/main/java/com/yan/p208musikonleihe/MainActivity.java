package com.yan.p208musikonleihe;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageViewPlayPause;
    private TextView mTextViewCurrentTime,mTextViewDuration;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageViewPlayPause = findViewById(R.id.imagePlayPause);
        mTextViewCurrentTime = findViewById(R.id.textCurrentTime);
        mTextViewDuration = findViewById(R.id.textTotalDuration);
        mSeekBar = findViewById(R.id.playerSeekBar);


        mMediaPlayer = new MediaPlayer();

        mSeekBar.setMax(100);
        mImageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()){
                    mHandler.removeCallbacks(updater);
                    mMediaPlayer.pause();
                    mImageViewPlayPause.setImageResource(R.drawable.ic_play);
                }else{
                    mMediaPlayer.start();
                    mImageViewPlayPause.setImageResource(R.drawable.ic_pause);
                    updateSeekBar();
                }
            }
        });

        prepareMediaPlayer();


    }

    private void prepareMediaPlayer(){
        try {
            mMediaPlayer.setDataSource("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3");
            mMediaPlayer.prepare();
            mTextViewDuration.setText(milliSecToTimer(mMediaPlayer.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Runnable updater  = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mMediaPlayer.getCurrentPosition();
            mTextViewCurrentTime.setText(milliSecToTimer(currentDuration));
        }
    };

    private void updateSeekBar(){
        if (mMediaPlayer.isPlaying()){
            mSeekBar.setProgress((int)(((float)mMediaPlayer.getCurrentPosition()/mMediaPlayer.getDuration())*100));
            mHandler.postDelayed(updater,1000);
        }
    }

    private String milliSecToTimer(long milliSec){
        String timerString = "";
        String secondsString;
        int hours = (int)(milliSec/3600000);
        int minutes = (int)((milliSec%3600000)/60000);
        int seconds = (int)(((milliSec%3600000))%60000/1000);

        if (hours>0){
            timerString = hours+":";
        }

        if (seconds<10){
            secondsString ="0"+ seconds;
        }else{
            secondsString =""+seconds;
        }
        timerString = timerString + minutes+":"+secondsString;
        return  timerString;
    }
}
