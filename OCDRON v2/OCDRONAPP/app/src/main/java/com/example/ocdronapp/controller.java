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

public class controller extends AppCompatActivity{
    private final String DEVICE_ADDRESS = "00:21:09:00:15:2A"; //MAC Address of Bluetooth Module in arduino 00:21:13:02:B5:5A
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // variable for bluetooth port
    private BluetoothDevice BTdevice;
    private BluetoothSocket BTsocket;
    private OutputStream outputStream = new OutputStream() {
        @Override
        public void write(int i) throws IOException {
        }
    };
    ImageButton forward_btn, backward_btn, left_btn, right_btn, home_btn, connect_btn,disconnect_btn, preset_btn; // imagebutton variables for controls
    char command; // to store the command being sent by the imagebuttons
    boolean stopThread;
    boolean deviceConnected=false;// for checking device connected to bluetooth
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        //button variables
        preset_btn = (ImageButton) findViewById(R.id.imgbtnPreset);
        forward_btn = (ImageButton) findViewById(R.id.imgbtnUp);
        backward_btn = (ImageButton) findViewById(R.id.imgbtnDown);
        left_btn = (ImageButton) findViewById(R.id.imgbtnLeft);
        right_btn = (ImageButton) findViewById(R.id.imgbtnRight);
        home_btn = (ImageButton) findViewById(R.id.imgbtnHome);
        connect_btn = (ImageButton) findViewById(R.id.imgbtnConnect);
        disconnect_btn = (ImageButton) findViewById(R.id.imgbtnDisconnect);

        //sets controls off since there is no connection to the device
        setbtn_Off();
        disconnect_btn.setEnabled(false);

        preset_btn.setOnTouchListener(new View.OnTouchListener() { // function for auto movement
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = '1';
                    Log.d("Auto Movement", String.valueOf(command));
                    try {
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try {
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        forward_btn.setOnTouchListener(new View.OnTouchListener() { // function for forward movement
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = 'F'; // command to be sent to OCDRON
                    Log.d("forward",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try {
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        backward_btn.setOnTouchListener(new View.OnTouchListener(){ // function for backward movement
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = 'B'; // command to be sent to OCDRON
                    Log.d("backward",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try {
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        left_btn.setOnTouchListener(new View.OnTouchListener(){ // function for left movement
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = 'L'; // command to be sent to OCDRON
                    Log.d("left",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try {
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        right_btn.setOnTouchListener(new View.OnTouchListener(){ // function for right movement
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    command = 'R'; // command to be sent to OCDRON
                    Log.d("right",String.valueOf(command));
                    try{
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP){
                    command = 'S';
                    try {
                        outputStream.write((String.valueOf(command)).getBytes());
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    public void connect(View view) { // connecting of bluetooth to phone
        Toast.makeText(getApplicationContext(),"Connecting....", Toast.LENGTH_LONG).show();
        if(BTinit() && BTconnect() ) // check if bluetooth is initialized and connected
        {
                deviceConnected=true;
        }
    }


    public boolean BTinit(){  //initializes bluetooth module
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) //checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()){ //checks if bluetooth is enabled. if not, the program will ask permission from the user to enable it
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()){ //checks for paired bluetooth devices
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else{
            for(BluetoothDevice iterator : bondedDevices){
                if(iterator.getAddress().equals(DEVICE_ADDRESS)){//checking if the connected bluetooth is equal to the bluetooth module
                    BTdevice = iterator;
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect(){ //function for bluetooth connection
        boolean connected = true;
        Toast.makeText(getApplicationContext(),"Connecting....", Toast.LENGTH_LONG).show();
        try{
            BTsocket = BTdevice.createRfcommSocketToServiceRecord(PORT_UUID); //creates a socket to handle the outgoing connection
            BTsocket.connect();
            home_btn.setEnabled(false);
            disconnect_btn.setEnabled(true);
            setbtn_On();
            Toast.makeText(getApplicationContext(),"Connection to the Bluetooth device is successful", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(),"Connection to the Bluetooth device failed", Toast.LENGTH_LONG).show();
        }
        return connected;
    }
    public void disconnectBT(View view){ //closes the connection of bluetooth
        try {
            stopThread = true;
            outputStream.close();
            BTsocket.close();
            deviceConnected = false;
            home_btn.setEnabled(true);
            disconnect_btn.setEnabled(false);
            setbtn_Off();
            Toast.makeText(getApplicationContext(), "Connection closed and return home button is enabled", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void returnHome(View view) throws IOException { // return to menu function
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

    public void setbtn_Off(){
        preset_btn.setEnabled(false);
        forward_btn.setEnabled(false);
        backward_btn.setEnabled(false);
        left_btn.setEnabled(false);
        right_btn.setEnabled(false);
    }

    public void setbtn_On(){
        preset_btn.setEnabled(true);
        forward_btn.setEnabled(true);
        backward_btn.setEnabled(true);
        left_btn.setEnabled(true);
        right_btn.setEnabled(true);
    }
}