#include <AFMotor.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>

int relayA1 = 7;
int relayA2 = 8;
int relayB1 = 9;
int relayB2 = 10;

char command;
//Servo
int max_Servo1 = 590; // max degree to rotate for Servo1
int min_Servo1 = 135; // min degree to rotate for Servo1
int max_Servo2 = 600; // max degree to rotate for Servo2
int min_Servo2 = 365; // min degree to rotate for Servo2
int Servo1 = 0; // indicate what pin the servo connect
int Servo2 = 4; // indicate what pin the servo connect

Adafruit_PWMServoDriver servocontroller = Adafruit_PWMServoDriver();

void setup()
{
    pinMode(relayA1, OUTPUT);// set pin as output for relay 1
    pinMode(relayA2, OUTPUT);// set pin as output for relay 2
    pinMode(relayB1, OUTPUT);// set pin as output for relay 3
    pinMode(relayB2, OUTPUT);// set pin as output for relay 4

    // keep the motor off by keeping both HIGH
    digitalWrite(relayA1, HIGH); 
    digitalWrite(relayA2, HIGH); 
    digitalWrite(relayB1, HIGH); 
    digitalWrite(relayB2, HIGH); 

    //Set the baud rate to your Bluetooth module.
    Serial.begin(9600);
  
    //Set servo controller
    servocontroller.begin();
    servocontroller.setPWMFreq(60);
}
void Stop() {
  // turn off all motors
  digitalWrite(relayA1, HIGH); 
  digitalWrite(relayA2, HIGH); 
  digitalWrite(relayB1, HIGH); 
  digitalWrite(relayB2, HIGH);
}
void forward() {
  // turn on motor going forward
  digitalWrite(relayA1, HIGH);// turn relay 1 OFF
  digitalWrite(relayA2, LOW);// turn relay 2 ON  
  digitalWrite(relayB1, HIGH);// turn relay 1 OFF
  digitalWrite(relayB2, LOW);// turn relay 2 ON   
}

void backward() {
  // turn on motor going backward
  digitalWrite(relayA1, LOW);// turn relay 1 ON
  digitalWrite(relayA2, HIGH);// turn relay 2 OFF   
  digitalWrite(relayB1, LOW);// turn relay 1 ON
  digitalWrite(relayB2, HIGH);// turn relay 2 OFF
}
void left() {
  // turn on motor going right
  digitalWrite(relayA1, HIGH);// turn relay 1 OFF
  digitalWrite(relayA2, LOW);// turn relay 2 ON 
  digitalWrite(relayB1, LOW);// turn relay 1 ON
  digitalWrite(relayB2, HIGH);// turn relay 2 OFF
}
void right() {
  // turn on motor going left*
  digitalWrite(relayA1, LOW);// turn relay 1 ON
  digitalWrite(relayA2, HIGH);// turn relay 2 OFF 
  digitalWrite(relayB1, HIGH);// turn relay 1 OFF
  digitalWrite(relayB2, LOW);// turn relay 2 ON   
}
void drop_sensor() {
  //Turning on servo on 180 degrees
  servocontroller.setPWM(Servo2, 0, max_Servo2);
  delay(2000);
}
void lift_sensor() {
  //Turning on servo on 90 degrees
  servocontroller.setPWM(Servo2, 0, min_Servo2);
  delay(2000);
}
void drop_limestone() {
  //Turning on servo on 180 degrees and going back on 0 degrees
  servocontroller.setPWM(Servo1, 0, min_Servo1);
  delay(5000);
  servocontroller.setPWM(Servo1, 0, max_Servo1);
  delay(2000);
}
void automatic_movement() {
  //for auto movement that goes forward and after it goes backward again
  forward();
  delay(4000);
  backward();
  delay(10000);
  
}

void loop()
{
  if (Serial.available() > 0) {
    command = Serial.read(); 
    Stop(); //initialize with motors stoped  
    Serial.println(command);
    switch(command){
      case 'F':  
        forward();
        break;
      case 'B':  
         backward();
        break;
      case 'L':  
        left();
        break;
      case 'R':
        right();
        break;
      case '1':
        automatic_movement();
        break;
      case '2':
        drop_sensor();
        break;
      case '3':
        lift_sensor();
        break;
      case '4':
        drop_limestone();
        break;
      default:
        Stop();
        break;
    }
  }
}
