package com.example.breathe;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.TextView;
import android.os.CountDownTimer;

import java.text.SimpleDateFormat;

public class meditation extends AppCompatActivity {
    RadioGroup rg;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addListenerOnButton();
    }

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }
    public void addListenerOnButton() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnDisplay = (Button) findViewById(R.id.button5);
        btnDisplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(meditation.this,
                        "準備計時" + radioButton.getText(), Toast.LENGTH_SHORT).show();
                int reciprocal = 10000;
                switch(radioButton.getId()){
                    case R.id.radioButton2:
                        reciprocal = 60000;
                        break;
                    case R.id.radioButton3:
                        reciprocal = 120000;
                        break;
                    case R.id.radioButton4:
                        reciprocal = 180000;
                        break;
                    case R.id.radioButton5:
                        reciprocal = 240000;
                        break;
                }
                Intent gostart = new Intent();
                gostart.setClass(meditation.this  , start.class);
                gostart .putExtra("reciprocal",reciprocal);
                startActivity(gostart);

            }
        });
    }
}
