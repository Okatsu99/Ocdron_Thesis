package com.example.ocdronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class phFeed extends AppCompatActivity {
    private final String DEVICE_ADDRESS="00:21:09:00:50:B4";//
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice device;
    private BluetoothSocket socket;
    String phScaling;
    ImageButton return_btn,save_btn, phConnect_btn, phDisconnect_btn;
    TextView phLvl;
    private OutputStream outputStream;
    private InputStream inputStream;
    boolean deviceConnected=false;
    byte buffer[];
    boolean stopThread;

    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phlevelfeed);
        phLvl = (TextView) findViewById(R.id.txt_PhLevel);
        save_btn = (ImageButton) findViewById(R.id.imgbtn_SavePh);
        phConnect_btn = (ImageButton) findViewById(R.id.imgbtn_PhConnect);
        phDisconnect_btn = (ImageButton) findViewById(R.id.imgbtn_PhDisconnect);
        return_btn = (ImageButton) findViewById(R.id.imgbtn_Return);

        phDisconnect_btn.setEnabled(false);
        save_btn.setEnabled(false);
    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesn't support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please pair the device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
            return_btn.setEnabled(false);
            save_btn.setEnabled(true);
            phDisconnect_btn.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getApplicationContext(),"Connection to Bluetooth device failed", Toast.LENGTH_LONG).show();
        }


        return connected;
    }

    public void connect(View view) {
        Toast.makeText(getApplicationContext(),"Connecting....", Toast.LENGTH_LONG).show();
        if(BTinit())
        {
            if(BTconnect())
            {
                deviceConnected=true;
                getPhData();
                Toast.makeText(getApplicationContext(),"Connection opened and getting pH data",Toast.LENGTH_SHORT).show();
            }
        }
    }

    void getPhData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String phLevel=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run(){
                                    Double colorChange = Double.parseDouble(phLvl.getText().toString());
                                    phLvl.setText(phLevel);
                                    if( colorChange < 1 && colorChange >= 0){
                                        phLvl.setTextColor(Color.parseColor("#7D1010"));
                                    }
                                    else if(colorChange < 3 &&  colorChange >= 2){
                                        phLvl.setTextColor(Color.parseColor("#FF1616"));
                                    }
                                    else if ( colorChange < 4 && colorChange >= 3){
                                        phLvl.setTextColor(Color.parseColor("#C85103"));
                                    }
                                    else if ( colorChange < 5 && colorChange >= 4){
                                        phLvl.setTextColor(Color.parseColor("#DA6220"));
                                    }
                                    else if ( colorChange < 6 && colorChange >=  5){
                                        phLvl.setTextColor(Color.parseColor("#F9943B"));
                                    }
                                    else if ( colorChange < 7 && colorChange >=  6){
                                        phLvl.setTextColor(Color.parseColor("#FFEB5B"));
                                    }
                                    else if ( colorChange < 8 && colorChange >=  7){
                                        phLvl.setTextColor(Color.parseColor("#509645"));
                                    }
                                    else if ( colorChange < 9 && colorChange >=  8){
                                        phLvl.setTextColor(Color.parseColor("#0D0DA8"));
                                    }
                                    else if ( colorChange < 10 && colorChange >=  9){
                                        phLvl.setTextColor(Color.parseColor("#040472"));
                                    }
                                    else if ( colorChange < 11 && colorChange >=  10){
                                        phLvl.setTextColor(Color.parseColor("#391C5D"));
                                    }
                                    else if ( colorChange < 12 && colorChange >=  11){
                                        phLvl.setTextColor(Color.parseColor("#602E9E"));
                                    }
                                    else if ( colorChange < 13 && colorChange >=  12){
                                        phLvl.setTextColor(Color.parseColor("#6D4FDD"));
                                    }
                                    else if ( colorChange < 14 && colorChange >=  13){
                                        phLvl.setTextColor(Color.parseColor("#924A92"));
                                    }
                                    else if ( colorChange < 15 && colorChange >=  14){
                                        phLvl.setTextColor(Color.parseColor(" #800080"));
                                    }
                                }
                            });
                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }

    public void disconnectBT(View view){ //closes the connection of bluetooth
        try {
            stopThread = true;
            outputStream.close();
            socket.close();
            deviceConnected = false;
            return_btn.setEnabled(true);
            save_btn.setEnabled(false);
            phDisconnect_btn.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Connection closed and return home button is enabled", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void returnHome(View view) throws IOException {
        Intent i = new Intent(getApplicationContext(), phSensorController.class);
        startActivity(i);
        finish();
    }

    public void save(View view) throws IOException,NumberFormatException{
        DBHelper myDB =  new DBHelper(phFeed.this);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String formattedDate = df.format(c);
        Double phVal = Double.parseDouble(phLvl.getText().toString());
        if( phVal < 7 && phVal > 0){
            phScaling = "ACIDIC";
        }
        else if(phVal < 8 &&  phVal >= 7   ){
            phScaling = "NEUTRAL";
        }
        else if(phVal < 15 && phVal >= 8){
            phScaling = "ALKALINE";
        }
        else{
            phScaling = "No Value";
        }
        myDB.addRecord(phLvl.getText().toString(), phScaling,formattedDate,currentTime);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), phSensorController.class);
        startActivity(i);
        finish();
    }
}