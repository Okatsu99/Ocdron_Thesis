package com.example.ocdronapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class phSensorController extends AppCompatActivity {
    private final String DEVICE_ADDRESS = "00:21:09:00:15:2A";//00:21:13:02:B5:5A
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice BTdevice;
    private BluetoothSocket BTsocket;
    private OutputStream outputStream = new OutputStream() {
        @Override
        public void write(int i) throws IOException {
        }
    };
    char command;
    boolean stopThread;
    boolean deviceConnected=false;
    ImageButton phFeed_btn, dropLime_btn, backToHome_btn, onPhSensor_btn, offPhSensor_btn, phDisconnect_btn, phConnect_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phselection);

        phFeed_btn = (ImageButton) findViewById(R.id.imgbtn_PhFeed);
        dropLime_btn = (ImageButton) findViewById(R.id.imgbtn_DropLime);
        backToHome_btn = (ImageButton) findViewById(R.id.imgbtn_PhGoToHome);
        onPhSensor_btn = (ImageButton) findViewById(R.id.imgbtn_PhSensorOn);
        offPhSensor_btn = (ImageButton) findViewById(R.id.imgbtn_PhSensorOff);
        phDisconnect_btn = (ImageButton) findViewById(R.id.imgbtn_PhSensorDisconnect);
        phConnect_btn = (ImageButton) findViewById(R.id.imgbtn_PhSensorConnect);

        setButtonsOff();
        phDisconnect_btn.setEnabled(false);

        View.OnClickListener goToPhFeed = new View.OnClickListener(){
            public void onClick(View view){
                Intent in = new Intent(phSensorController.this, phFeed.class);
                startActivity(in);
                finish();
            }
        };
        phFeed_btn.setOnClickListener(goToPhFeed);

        onPhSensor_btn.setOnTouchListener(new View.OnTouchListener() { // function for turning the ph sensor on
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = '2';
                    Toast.makeText(getApplicationContext(), "pH sensor on", Toast.LENGTH_SHORT).show();
                    Log.d("pH sensor on",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        offPhSensor_btn.setOnTouchListener(new View.OnTouchListener() { // function for turning the ph sensor off
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = '3';
                    Toast.makeText(getApplicationContext(), "pH sensor off", Toast.LENGTH_SHORT).show();
                    Log.d("pH sensor off",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        dropLime_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = '4';
                    Toast.makeText(getApplicationContext(), "Dropping Lime Extract", Toast.LENGTH_SHORT).show();
                    Log.d("Dropping Lime Extract",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    public void connect(View view) {
        Toast.makeText(getApplicationContext(),"Connecting....", Toast.LENGTH_LONG).show();
        if(BTinit())
        {
            if(BTconnect())
            {
                deviceConnected=true;
            }
        }
    }

    //Initializes bluetooth module
    public boolean BTinit(){
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()){ //Checks for paired bluetooth devices
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else{
            for(BluetoothDevice iterator : bondedDevices){
                if(iterator.getAddress().equals(DEVICE_ADDRESS)){
                    BTdevice = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect(){
        boolean connected = true;
        try{
            BTsocket = BTdevice.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            BTsocket.connect();
            backToHome_btn.setEnabled(false);
            phFeed_btn.setEnabled(false);
            phDisconnect_btn.setEnabled(true);
            setButtonsOn();
            Toast.makeText(getApplicationContext(),"Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e){
            e.printStackTrace();
            connected = false;
        }

        if(connected){
            try{
                outputStream = BTsocket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Connection to bluetooth device failed", Toast.LENGTH_LONG).show();
        }
        return connected;
    }

    public void disconnectBT(View view){ //closes the connection of bluetooth
        try {
            stopThread = true;
            outputStream.close();
            BTsocket.close();
            deviceConnected = false;
            backToHome_btn.setEnabled(true);
            phFeed_btn.setEnabled(true);
            phDisconnect_btn.setEnabled(false);
            setButtonsOff();
            Toast.makeText(getApplicationContext(), "Connection closed and return home button is enabled", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void returnHome(View view) throws IOException {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void setButtonsOff(){ // disables buttons
        dropLime_btn.setEnabled(false);
        onPhSensor_btn.setEnabled(false);
        offPhSensor_btn.setEnabled(false);
    }

    public void setButtonsOn(){ // enables buttons
        dropLime_btn.setEnabled(true);
        onPhSensor_btn.setEnabled(true);
        offPhSensor_btn.setEnabled(true);
    }

}