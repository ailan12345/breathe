package com.example.breathe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    /*private BluetoothAdapter bluetoothAdapter;*/
    private static final int REQUEST_ENABLE_BT = 2;
    private ConnectThread connectThread;
    BluetoothAdapter bluetoothAdapter;
    UUID MY_UUID;
    private OutputStream tmpOut ;//輸出流
    private InputStream tmpIn ;//輸入流
    TextView osValue;
    int signalquality;
    int attention;
    int meditation;


//    private final BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    TextView tvDevices = (TextView)findViewById(R.id.textView3);
//                    tvDevices.append(device.getName()+ ":" + device.getAddress());
//                }
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
////已搜素完成
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); //5 min
        startActivity(discoverableIntent);

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            setVibrate(1000); // 震動 1 秒
            Toast.makeText(getApplicationContext(),"請開啟藍芽",
                    Toast.LENGTH_LONG).show();
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ListView listview = (ListView) findViewById(R.id.lv_devices);
        ArrayList<String> ar = new ArrayList<String>();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                ar.add(deviceName + "|||" + deviceHardwareAddress);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ar);
        listview.setAdapter(adapter);
        BluetoothDevice device = bluetoothAdapter.getDefaultAdapter().getRemoteDevice("8C:DE:52:44:A7:40");
        MY_UUID = device.getUuids()[0].getUuid();
        TextView mTeView = (TextView) findViewById(R.id.textView3);
        mTeView.setText("UUID:"+MY_UUID.randomUUID().toString());

        connectThread = new ConnectThread(device);
        connectThread.run();
        osValue = (TextView) findViewById(R.id.osValue);
        osValue.setText(tmpIn.toString());
//        switch(tmpIn.toString()){
//            case tmpIn.POOR_SIGNAL:
//                reciprocal = 20000;
//                break;
//            case R.id.radioButton3:
//                reciprocal = 120000;
//                break;
//            case R.id.radioButton4:
//                reciprocal = 180000;
//                break;
//            case R.id.radioButton5:
//                reciprocal = 240000;
//                break;
//        }


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
        TextView tvDevices = (TextView)findViewById(R.id.textView3);
        btnBluetoothAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
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

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };


    public void setVibrate(int time){
        Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), String.valueOf(msg.obj),
                    Toast.LENGTH_LONG).show();
            super.handleMessage(msg);
        }
    };



    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e("TGA", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();
            int t =0;
            char c;
            int i;
            try {
                mmSocket.connect();
                tmpOut  = mmSocket.getOutputStream();
                tmpIn = mmSocket.getInputStream();

//                while((i=tmpIn.read())!=-1)
//                {
//                    // int to character
//                    c=(char)i;
//                    // print char
//                    Log.e("TGAC", "Character Read: "+c);
//                }
                while(t!=1024) {
                    t++;
                    byte[] buffer =new byte[256];
                    int count = tmpIn.read(buffer);
                    Message msg = new Message();
                    msg.obj = new String(buffer, 0, count, "UTF-8");
                    handler.sendMessage(msg);
                }
            } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("TGA", "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
//            manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("TGA", "Could not close the client socket", e);
            }
        }
    }


}
