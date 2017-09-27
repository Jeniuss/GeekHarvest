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
#define WIFI_SSID "Jeniuss"
#define WIFI_PASSWORD "Jeniuss26"

// Config firebase
#define FIREBASE_HOST "geekharvest-b3a81.firebaseio.com"
#define FIREBASE_AUTH "kJBCWxaKG2QtAYlffVwXaK018gYtDPXOmx6x6fTV"
// Config device

const String deviceNumber = "ELnD58NxBTUkwxzqdhTu1Fm0dHs1";
const String deviceName = "Jee's Farm";

// Config pin
DHT dht;
const int valve_pin = D6;//relay solenoid valve
const int light_pin = D5;//relay light
 
//soil
int soilPin = A0;  // sensor YL69
int soilValue; //no of soil value
float soil;
String soil_status;

//light + LDR
String light_status;
int ldr_pin = A0;
float ldr_value;

//autolight
int light_need = 0;
int light_feed = 0;
int out_range = 12;
int active = 1;
int light_state = -1;
int at_min = -1;

//time
int current_hour = 0;
int current_min = 0;
int current_sec = 0;
int day = 0;

//dht
float humidity, temperature;


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
  Firebase.setInt(deviceNumber + "/active", active);
}

void loop() {
  Serial.println("Start reading sensor...");
  dhtSensor();
  ldrLight();
  light();
  setStaticData();
  valve();
  autoValve();
  set_current_time();
  Serial.println(String("") + current_hour + ":" + current_min + ":" + current_sec);
  autoLight();
  //test();
  delay(1000);
}

void test(){
  // PUSH DATA
  day = day+1;
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& valueObject = jsonBuffer.createObject();
  valueObject["day"] = String(day);
  valueObject["light_feed"] = String(light_feed);
  Firebase.push(deviceNumber + "/value", valueObject);
}

void set_current_time(){
  WiFiClient client;
  while(!client.connect("google.com", 80)) {
    Serial.println("Connection Failed");
  }
  
  client.print("HEAD / HTTP/1.1\r\n\r\n");
  while(!client.available()) {
     yield();
  }
  
  while(client.available()){
    if (client.read() == '\n' && client.read() == 'D' && client.read() == 'a' && client.read() == 't' && client.read() == 'e' && client.read() == ':') {    
      client.read();
      String current_time = client.readStringUntil('\r');
      current_hour = (current_time.substring(17,19).toInt()) + 7;
      if(current_hour > 23) current_hour -= 24;
      current_min = current_time.substring(20,22).toInt();
      current_sec = current_time.substring(23,25).toInt();
      client.stop();
    }
  }
}


void autoLight(){
  light_need = Firebase.getInt(deviceNumber + "/light_need");
  Firebase.setInt(deviceNumber + "/_time", current_hour);
  if(current_min != at_min){
    at_min = current_min;

    // COLLECT PHASE
    if(isLightStatus()){
      light_feed ++;
      Firebase.setInt(deviceNumber + "/light_feed", light_feed);
    }

    // USER INTERUPT PHASE
    if(light_state != -1){
      if((light_state == 0 && isLight()) || (light_state == 1 && !isLight())){
        active = 0;
        Firebase.setInt(deviceNumber + "/active", active);
      }
    }
    
    // RESET AT 6:00 AM
    if(current_hour == 6 && at_min == 0){
      reset();
    }

    // MORNING PHASE
    if(light_need > out_range && current_hour >= 6 && current_hour <= (17 - (light_need - out_range))){
      light_state = -1;
      Firebase.setInt(deviceNumber + "/light_state", light_state);
    } else if(light_need <= out_range && current_hour >= 6 && current_hour <= 17){
      light_state = -1;
      Firebase.setInt(deviceNumber + "/light_state", light_state);
    // EVERNING PHASE
    } else if (active == 1) {
      if(light_feed < (light_need * 60)){
        light_state = 1;
        Firebase.setInt(deviceNumber + "/light_state", light_state);
        Firebase.setString(deviceNumber + "/light", "1");
      } else {
        light_state = 0;
        Firebase.setInt(deviceNumber + "/light_state", light_state);
        Firebase.setString(deviceNumber + "/light", "0");
      }
    }
  }
  
}

boolean isLight(){
  String light = Firebase.getString(deviceNumber + "/light");
  if(light.equals("1")){
    return true;
  }else{
    return false;
  }
  
}

boolean isLightStatus(){
  String light_status = Firebase.getString(deviceNumber + "/light_status");
  if(light_status.equals("light")){
    return true;
  }else{
    return false;
  }
  
}

void reset(){
  // PUSH DATA
  day = day+1;
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& valueObject = jsonBuffer.createObject();
  valueObject["day"] = String(day);
  valueObject["lightfeedperday"] = String(light_feed);
  Firebase.push(deviceNumber + "/value", valueObject);

  // RESET VARIABLES
  light_feed = 0;
  Firebase.setInt(deviceNumber + "/light_feed", light_feed);
  active = 1;
  Firebase.setInt(deviceNumber + "/active", active);
}


void setStaticData() {
  soilValue = analogRead(soilPin);
  
  soil = ((1024-soilValue)*100.0)/1024.0;
    
  Serial.print("Moisture of Soil: ");
  Serial.print(soil);
  Serial.println("%");
  if(soil <= 40) {
    Serial.println("Soil Humidity: Low");
    soil_status = "Low Humidity";
  } else if(soil >= 41 && soil <= 70) {
    Serial.println("Soil moisture: Medium");
    soil_status = "Normal Humidity";
  } else if(soil >= 71) {
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
  }else
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
    if(soil_status == "Low Humidity"){
      Firebase.setString(deviceNumber + "/valve", "1");
    }else{
      Firebase.setString(deviceNumber + "/valve", "0");
    }
  }
}

void dhtSensor(){
  humidity = dht.getHumidity(); // ดึงค่าความชื้น
  temperature = (dht.getTemperature() * 0.00001) - 10; // ดึงค่าอุณหภูมิ
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
  if(ldr_value <= 550){
    light_status = "dark";
    Serial.println("dark");  
  }else {
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
    digitalWrite(light_pin, HIGH);
  }else
  if(light == "0"){ //switch off
    digitalWrite(light_pin, LOW);
  }
}

