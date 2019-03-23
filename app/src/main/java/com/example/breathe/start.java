package com.example.breathe;

import android.app.Service;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.content.Intent;

public class start extends AppCompatActivity {

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = this.getIntent();
        int reciprocal = intent.getIntExtra("reciprocal", 10000);

        ImageView gifImageView2 = (ImageView) findViewById(R.id.imageView);

        new CountDownTimer(reciprocal, 1000) {
            TextView mTextView = (TextView) findViewById(R.id.textView6);
            @Override
            public void onTick(long millisUntilFinished) {
                //倒數秒數中要做的事
//                        mTextView.setText("倒數時間:" + millisUntilFinished / 1000);
                TextView myAwesomeTextView = (TextView)findViewById(R.id.textView7);
                mTextView.setText("倒數時間:" + millisUntilFinished/1000);
                myAwesomeTextView.setText("aaa" + ((millisUntilFinished/1000)+1)%6);
                if(((millisUntilFinished/1000)+1)%6 == 0){
                    setVibrate(400); // 震動 0.4 秒
                }
            }
            @Override
            public void onFinish() {
                //倒數完成後要做的事
                mTextView.setText("Done!");
            }
        }.start();
    }

}
