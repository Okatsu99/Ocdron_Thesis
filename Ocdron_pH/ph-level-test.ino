#include <Wire.h>
#include <SoftwareSerial.h>

 
float calibration_value = 24.4;
int phval = 0; 
unsigned long int avgval; 
int buffer_arr[10],temp;
 
float phValue;

void setup() 
{
  Wire.begin();
 Serial.begin(9600);


}
void loop() {
 // Initiates SimpleTimer
 for(int i=0;i<10;i++) 
 { 
 buffer_arr[i]=analogRead(A0);
 delay(30);
 }
 for(int i=0;i<9;i++)
 {
   for(int j=i+1;j<10;j++)
   {
     if(buffer_arr[i]>buffer_arr[j])
     {
       temp=buffer_arr[i];
       buffer_arr[i]=buffer_arr[j];
       buffer_arr[j]=temp;
     }
    }
 }
 
 avgval=0;
 for(int i=2;i<8;i++)
 avgval+=buffer_arr[i];
 float volt=(float)avgval*5.0/1024/6; 
 phValue = -5.70 * volt + calibration_value;
 String ph_Value ="";
 ph_Value.concat(phValue);
 Serial.println(ph_Value);
 delay(1000); 
}
 
