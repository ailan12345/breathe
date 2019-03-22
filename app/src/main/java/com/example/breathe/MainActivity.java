package com.example.breathe;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Service;

public class MainActivity extends AppCompatActivity {

    /*private BluetoothAdapter bluetoothAdapter;*/
    private static final int REQUEST_ENABLE_BT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            setVibrate(1000); // 震動 1 秒
            Toast.makeText(getApplicationContext(),"請開啟藍芽",
                    Toast.LENGTH_LONG).show();
        }

        Button  desBtn1 = (Button) findViewById(R.id.button);//呼吸
        desBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent godescription = new Intent();
                godescription.setClass(MainActivity.this  , description.class);
                startActivity(godescription);
            }
        });

        Button  meditationBtn = (Button) findViewById(R.id.button2);//冥想
        meditationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gomeditation = new Intent();
                gomeditation.setClass(MainActivity.this  , meditation.class);
                startActivity(gomeditation);
            }
        });

        Button  desBtn = (Button) findViewById(R.id.button3);//說明
        desBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent godescription = new Intent();
                godescription.setClass(MainActivity.this  , description.class);
                startActivity(godescription);
            }
        });

        Button btnBluetoothAdapter  = (Button) findViewById(R.id.button4);//藍芽
        btnBluetoothAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                    Toast.makeText(getApplicationContext(),"開啟中"
                            ,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"已經開啟",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }
}
