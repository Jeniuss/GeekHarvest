#include <DHT.h>
#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>
#include <ESP8266WiFi.h>
#include <Wire.h> // connect to other board data link
#include <time.h> // get time api from c library
#include <Arduino.h>

// Config wifi network
#define WIFI_SSID "HONG"
#define WIFI_PASSWORD "0816122165"

// Config firebase
#define FIREBASE_HOST "geekharvest-b3a81.firebaseio.com"
#define FIREBASE_AUTH "kJBCWxaKG2QtAYlffVwXaK018gYtDPXOmx6x6fTV"
// Config device

const String deviceNumber = "ELnD58NxBTUkwxzqdhTu1Fm0dHs1";
const String deviceName = "Jee's Farm";
// Config time

int delayCount = 0; // delayCount to push static data
int soilPin = A0;  // sensor YL69
int soilValue = 0;
int ldr_pin = A0;
float ldr_value = 0.0;
String light_status;
float soil = 0.0;
String soil_status = "normal";
int time1,realTime,tempTime,suggest,day,cnt;
int lightTimeStatus[24];
boolean check = false;
DHT dht;

//test
const int valve_pin = D6;//relay solenoid valve
const int light_pin = D5;//relay light


//define pin
//#define valve_pin D6

void setup() {
  Serial.begin(115200);
  pinMode(valve_pin, OUTPUT);
  pinMode(light_pin, OUTPUT);
  pinMode(ldr_pin,INPUT);
  dht.setup(D3);

  Serial.println();
  Serial.print("Booted ");
  
  // connect to wifi.
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to wifi..");

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to: ");
  Serial.println(WiFi.localIP());

  Serial.println();

  // Initial connecting to firebase
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Serial.println("Connected to firebase!!");
}

void loop() {
  Serial.println("Start reading sensor...");
//  realTime = Firebase.getInt(deviceNumber + "/realTime");
  getTime();
  ldrLight();
  cntLight();
  light();
  setStaticData();
  dhtSensor();
  valve();
  autoValve();

  delay(1000);
}

void getTime() {
  WiFiClient client;
  while (!!!client.connect("google.com", 80)) {
    Serial.println("connection failed, retrying...");
  }

  client.print("HEAD / HTTP/1.1\r\n\r\n");
  while(!!!client.available()) {
     yield();
  }

  while(client.available()){
    if (client.read() == '\n') {    
      if (client.read() == 'D') {    
        if (client.read() == 'a') {    
          if (client.read() == 't') {    
            if (client.read() == 'e') {    
              if (client.read() == ':') {    
                client.read();
                String theDate = client.readStringUntil('\r');
                String newTime = theDate.substring(17,19);
                time1 = newTime.toInt();
                client.stop();
                switch(time1){
                  case 0:
                    realTime = 7;
                    Serial.println(7);
                  break;
                  case 1:
                    realTime = 8;
                    Serial.println(8);
                  break;
                  case 2:
                    realTime = 9;
                    Serial.println(9);
                  break;
                  case 3:
                    realTime = 10;
                    Serial.println(10);
                  break;
                  case 4:
                    realTime = 11;
                    Serial.println(11);
                  break;
                  case 5:
                    realTime = 12;
                    Serial.println(12);
                  break;
                  case 6:
                    realTime = 13;
                    Serial.println(13);
                  break;
                  case 7:
                    realTime = 14;
                    Serial.println(14);
                  break;
                  case 8:
                    realTime = 15;
                    Serial.println(15);
                  break;
                  case 9:
                    realTime = 16;
                    Serial.println(16);
                  break;
                  case 10:
                    realTime = 17;
                    Serial.println(17);
                  break;
                  case 11:
                    realTime = 18;
                    Serial.println(18);
                  break;
                  case 12:
                    realTime = 19;
                    Serial.println(19);
                  break;
                  case 13:
                    realTime = 20;
                    Serial.println(20);
                  break;
                  case 14:
                    realTime = 21;
                    Serial.println(21);
                  break;
                  case 15:
                    realTime = 22;
                    Serial.println(22);
                  break;
                  case 16:
                    realTime = 23;
                    Serial.println(23);
                  break;
                  case 17:
                    realTime = 0;
                    Serial.println(0);
                  break;
                  case 18:
                    realTime = 1;
                    Serial.println(1);
                  break;
                  case 19:
                    realTime = 2;
                    Serial.println(2);
                  break;
                  case 20:
                    realTime = 3;
                    Serial.println(3);
                  break;
                  case 21:
                    realTime = 4;
                    Serial.println(4);
                  break;
                  case 22:
                    realTime = 5;
                    Serial.println(5);
                  break;
                  case 23:
                    realTime = 6;
                    Serial.println(6);
                  break;
                }
                Firebase.setInt(deviceNumber + "/realTime", realTime);
              }
            }
          }
        }
      }
    }
  }
}

void cntLight(){
  String light = Firebase.getString(deviceNumber + "/light_status");
  if(realTime > tempTime && realTime >= 6 && realTime <= 18){
     if(light.equals("light")){
       cnt++;
       Firebase.setInt(deviceNumber + "/cntTimeOpenLight", cnt);
       Serial.println("I'm here1");
       Serial.println(tempTime);
     }
     tempTime = realTime;
     Serial.println("I'm here2");
     Serial.println(tempTime);
     check = true;
    }else if(realTime == 19 && check == true){  
     suggest = 16 - cnt;
     day = day+1;
     StaticJsonBuffer<200> jsonBuffer;
     JsonObject& valueObject = jsonBuffer.createObject();
     valueObject["cntLight"] = cnt;
     valueObject["suggest"] = suggest;
     Firebase.push(deviceNumber + "/Day"+day, valueObject);
     check = false;
     cnt = 0;
     Firebase.setInt(deviceNumber + "/cntTimeOpenLight", 0);
     tempTime = 0;
    }
}



void setStaticData() {
  soilValue = analogRead(soilPin);
  
  soil = ((1024-soilValue)*100.0)/1024.0;
    
  Serial.print("Moisture of Soil: ");
  Serial.print(soil);
  Serial.println("%");
  if(soil < 20) {
    Serial.println("Soil Humidity: Low");   
    soil_status = "Low Humidity";  
  } else if(soil >= 21 && soil <= 40) {
    Serial.println("Soil Humidity: Low");
    soil_status = "Low Humidity";
  } else if(soil >= 41 && soil <= 70) {
    Serial.println("Soil moisture: Medium");
    soil_status = "Normal Humidity";
  } else if(soil >= 71 && soil <= 80) {
    Serial.println("Soil moisture: High");
    soil_status = "High Humidity";
  } else if(soil >= 81) {
    Serial.println("Soil moisture: High");
    soil_status = "High Humidity";
  }
  
  Firebase.setFloat(deviceNumber + "/soil", soil);
  Firebase.setString(deviceNumber + "/soil_status", soil_status);
  if (Firebase.failed()) {
    Serial.print("Set dynamic sensor failed:");
    Serial.println(Firebase.error());
    return;
  }

}



void valve()
{
  String valve = Firebase.getString(deviceNumber + "/valve");
  Serial.print("Valve : ");
  Serial.println(valve);
  if(valve == "1"){ //switch on
    digitalWrite(valve_pin, LOW);
  }
  if(valve == "0"||soil_status == "Normal Humidity" || soil_status == "High Humidity"){ //switch off
    Firebase.setString(deviceNumber + "/valve", "0");
    digitalWrite(valve_pin, HIGH);
  }

}

void autoValve() 
{
  String valve = Firebase.getString(deviceNumber + "/valve");
  String soil_status = Firebase.getString(deviceNumber + "/soil_status");
  String auto_valve = Firebase.getString(deviceNumber + "/auto valve");
  if(auto_valve == "1"){
    if(soil_status != "Low Humidity"){
      Firebase.setString(deviceNumber + "/valve", "0");
    }else{
      Firebase.setString(deviceNumber + "/valve", "1");
    }
  }
}

void dhtSensor(){
  float humidity = dht.getHumidity(); // ดึงค่าความชื้น
  float temperature = dht.getTemperature() * 0.00001; // ดึงค่าอุณหภูมิ
  if(isnan(humidity) || isnan(temperature)){
    Serial.println("Sensor disconnect");
  }else{
    Serial.print("\t");
    Serial.print("Humidity ");
    Serial.print(humidity);
    Serial.print("%");
    Serial.print("\t\t");
    Serial.print("Temperature ");
    Serial.print(temperature);
    Serial.println("c");
  }
  Firebase.setFloat(deviceNumber + "/humidity", humidity);
  Firebase.setFloat(deviceNumber + "/temperature", temperature);
}

void ldrLight(){
  ldr_value=analogRead(ldr_pin); 
  if(ldr_value <= 300){
    light_status = "dark";
    Serial.println("dark"); 
  }else if(ldr_value > 300 && ldr_value < 800){
    light_status = "normal";
    Serial.println("normal"); 
  }else if(ldr_value >= 800 ){
    light_status = "light";
    Serial.println("light"); 
  }
  Serial.print("ldr = "); 
  Serial.println(ldr_value); 
  
  Firebase.setString(deviceNumber + "/light_status", light_status);
//  Firebase.setFloat(deviceNumber + "/light", ldr_value);
}

void light()
{
  String light = Firebase.getString(deviceNumber + "/light");
  Serial.print("Light : ");
  Serial.println(light);
  if(light == "1"){ //switch on
    digitalWrite(light_pin, LOW);
  }
  if(light == "0"||light_status == "normal" || light_status == "light"){ //switch off
    Firebase.setString(deviceNumber + "/light", "0");
    digitalWrite(light_pin, HIGH);
  }

}



