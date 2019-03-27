package com.example.breathe;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class start extends AppCompatActivity {
    //震動
    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }


    MediaPlayer  mediaPlayer;
    CountDownTimer yourCountDownTimer;
    CountDownTimer yourCountDownTimer2;
    int reciprocal = 10000;
    int frequency = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mediaPlayer = MediaPlayer.create(this, R.raw.train);
        mediaPlayer.start();
//    ImageView image = (ImageView) findViewById(R.id.image);
//        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.fadescalecombine);
//        image.startAnimation(hyperspaceJump);
//        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,
//                R.animator.breath);
//        set.setTarget(image);
//        set.start();
//        TextView myAwesomeTextView = (TextView)findViewById(R.id.textView7);
//        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.fadescalecombine);
//        myAwesomeTextView.startAnimation(scaleAnimation);

//        ImageView gifImageView2 = (ImageView) findViewById(R.id.imageView);
//        yourCountDownTimer;
    Intent intent = this.getIntent();
    reciprocal = intent.getIntExtra("reciprocal", 10000); //總進行時數
    frequency = intent.getIntExtra("frequency", 2); //頻率
    yourCountDownTimer = new CountDownTimer(reciprocal, 1000) {
        TextView mTextView = (TextView) findViewById(R.id.textView6);
        @Override
        public void onTick(long millisUntilFinished) {
            //倒數秒數中要做的事
            mTextView.setText("倒數時間:" + millisUntilFinished/1000 + " (秒)");
        }
        @Override
        public void onFinish() {
            //倒數完成後要做的事
            mTextView.setText("Done!");
        }
    }.start();
        yourCountDownTimer2 = new CountDownTimer(reciprocal, (frequency-1)*1000) {
            int i = 0;
            int frequencyTime = 0;
            ProgressBar myProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            TextView myAwesomeTextView = (TextView)findViewById(R.id.textView7);
            @Override
            public void onTick(long millisUntilFinished) {
                //倒數秒數中要做的事
                setVibrate(200); // 震動 0.2 秒
                i += 1;
                myProgressBar.setProgress(0);
                new CountDownTimer(frequency*1000, 1000) {
                    TextView mTextView = (TextView) findViewById(R.id.textView6);
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.v("Log_tag", "Tick of Progress" +(int)(frequency-(millisUntilFinished/1000))*(100/frequency) + "|||||" + millisUntilFinished);
                        myProgressBar.setProgress((int)(frequency-(millisUntilFinished/1000))*(100/frequency));
                        if (i % 2 == 1) {
                            myAwesomeTextView.setText("吸氣" + ((millisUntilFinished / 1000)));
                        } else {
                            myAwesomeTextView.setText("吐吐氣" + ((millisUntilFinished / 1000)));
                        }
                    } @Override
                    public void onFinish() {
                        //倒數完成後要做的事
                        myProgressBar.setProgress(100);
                    }
                }.start();
            }
            @Override
            public void onFinish() {
                //倒數完成後要做的事
                setVibrate(200); // 震動 0.2 秒
                myAwesomeTextView.setText("Done!");
            }
        }.start();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        Toast.makeText(start.this,
                "返回" , Toast.LENGTH_SHORT).show();
        yourCountDownTimer.cancel();
        yourCountDownTimer2.cancel();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}
