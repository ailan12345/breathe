package com.example.breathe;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Application;
import android.app.Service;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.util.Timer;

public class start extends AppCompatActivity {

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

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

        ImageView image = (ImageView) findViewById(R.id.image);
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
            TextView myAwesomeTextView = (TextView)findViewById(R.id.textView7);
            @Override
            public void onTick(long millisUntilFinished) {
                //倒數秒數中要做的事
                setVibrate(200); // 震動 0.4 秒
                i += 1;
                new CountDownTimer(frequency*1000, 1000) {
                    TextView mTextView = (TextView) findViewById(R.id.textView6);
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (i % 2 == 1) {
                            myAwesomeTextView.setText("吸氣" + ((millisUntilFinished / 1000)));
                        } else {
                            myAwesomeTextView.setText("吐吐氣" + ((millisUntilFinished / 1000)));
                        }
                    } @Override
                    public void onFinish() {
                        //倒數完成後要做的事
                    }
                }.start();
            }
            @Override
            public void onFinish() {
                //倒數完成後要做的事
                setVibrate(200); // 震動 0.4 秒
                myAwesomeTextView.setText("Done!");
            }
        }.start();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        Toast.makeText(start.this,
                "test" , Toast.LENGTH_SHORT).show();
        yourCountDownTimer.cancel();
        yourCountDownTimer2.cancel();
    }
}
