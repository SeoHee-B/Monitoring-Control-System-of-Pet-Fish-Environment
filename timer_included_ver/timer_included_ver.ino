
#include <Firebase_Arduino_WiFiNINA.h>
#include <Firebase_Arduino_WiFiNINA_HTTPClient.h>

 
#include <Arduino_LSM6DS3.h>
 
#define FIREBASE_HOST "pongdang-90f15-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "KDWyOG6Ga9SWBjEN2KTn8oa7y4V47Vnwg8eot961"
#define WIFI_SSID "Dene"
#define WIFI_PASSWORD "1234567890" 
 
FirebaseData firebaseData;


#include <OneWire.h>
#include <SimpleTimer.h>

#define Vref 4.95
SimpleTimer firstTimer;
OneWire  ds(5);


int sensorValue = 0;
float tdsValue = 0;
float Voltage = 0;

int Liquid_level=0;

unsigned long int avgValue;     //Store the average value of the sensor feedback
int i=0;

///////////

int motor = 12; //모터1 릴레이 핀
int motor_2 = 11; //모터2 릴레이 핀
int led = 13; //Led 릴레이핀

///////////////

void setup() {

 Serial.begin(9600);
 delay(1000);
 Serial.println();
    



 Serial.print("Connecting to WiFi…");
 int status = WL_IDLE_STATUS;
 while (status != WL_CONNECTED) {
 status = WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
 Serial.print(".");
 delay(300);
 }
 Serial.print(" IP: ");
 Serial.println(WiFi.localIP());
 Serial.println();
 
 Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH, WIFI_SSID, WIFI_PASSWORD);
 Firebase.reconnectWiFi(true);






  
    pinMode(5, INPUT);
    pinMode(A0, INPUT);
    pinMode(A1, INPUT);
    pinMode(A2, OUTPUT);

//////
 pinMode(motor,OUTPUT);
 pinMode(motor_2,OUTPUT);
 pinMode(led,OUTPUT);
/////
    
    Serial.begin(9600);
    TDS();
    PH();
    WL();
    temp();
    firstTimer.setInterval(12000);      // get sensing value every 3min

}


    int t=0;
    int t_set=0;

void loop() {

  if(firstTimer.isReady()){
    TDS();
    PH();
    WL();
    temp();
    firstTimer.reset();
  }
  int t = 0;

  if (Firebase.getInt(firebaseData, "temp")) {

    //Success, then read the payload value

    //Make sure payload value returned from server is integer
    //This prevent you to get garbage data
    if (firebaseData.dataType() == "int") {
      t = firebaseData.intData();
      Serial.println(t);
    }

  } else {
    //Failed, then print out the error detail
    Serial.println(firebaseData.errorReason());
  }
  int temp_set = 0;

  if (Firebase.getInt(firebaseData, "temp_set")) {

    //Success, then read the payload value

    //Make sure payload value returned from server is integer
    //This prevent you to get garbage data
    if (firebaseData.dataType() == "int") {
      temp_set = firebaseData.intData();
      Serial.println(temp_set);
    }

  } else {
    //Failed, then print out the error detail
    Serial.println(firebaseData.errorReason());
  }
  
   if(temp_set > t){
    Firebase.setInt(firebaseData,"heater",1);
    Serial.println("on");
  }
  if(temp_set <= t){
    Firebase.setInt(firebaseData,"heater",0);
     Serial.println("off");
  }

////////////////////////////////////////////////

  int li = 0;

  if (Firebase.getInt(firebaseData, "light")) {
    if (firebaseData.dataType() == "int") {
      li = (firebaseData.intData());
      Serial.println(li);
    }

     if (li == 1){      //light버튼이 켜져있으면 LED를  점등
      digitalWrite(led,HIGH);
      Serial.println("LED on");
   }else{
      digitalWrite(led,LOW);
      Serial.println("LED off");
   }


  } else {
    //Failed, then print out the error detail
    Serial.println(firebaseData.errorReason());
  }
  
  
  int f = 0;

  if (Firebase.getInt(firebaseData, "flow")) {

    //Success, then read the payload value

    //Make sure payload value returned from server is integer
    //This prevent you to get garbage data
    if (firebaseData.dataType() == "int") {
      f = (firebaseData.intData());
      Serial.println(f);
    }
    if ( f ==  1 )   // flow버튼이 켜져 있으면 가동 아니면 꺼짐
   {
   digitalWrite(motor,HIGH);
   Serial.println("motor on");
   digitalWrite(motor_2,HIGH);
   Serial.println("motor_2 on");
 }
 else   {
   digitalWrite(motor,LOW);
   Serial.println("motor off");
   digitalWrite(motor_2,LOW);
   Serial.println("motor_2 off");
 } 

  } else {
    //Failed, then print out the error detail
    Serial.println(firebaseData.errorReason());
  }
  


   

/////////////////////////////////////////////////


  delay(6000);
  
}

void TDS (){
   //TDS SENSOR CODE // 
    sensorValue = analogRead(A0);
    Voltage = sensorValue*5/1024.0; //Convert analog reading to Voltage
    tdsValue=(133.42/Voltage*Voltage*Voltage - 255.86*Voltage*Voltage + 857.39*Voltage)*0.5; //Convert voltage value to TDS value
    Serial.print("TDS Value = "); 
    Serial.print(tdsValue);
    Serial.println(" ppm");
    Firebase.setInt(firebaseData,"tds",tdsValue);
}

void PH (){
  // PH SENSOR CODE //
    float sensorValue;
    float PHvalue;
    int m;
    long sensorSum;
    int buf[40];                //buffer for read analog
  for(int i=0;i<40;i++)       //Get 10 sample value from the sensor for smooth the value
  { 
    buf[i]=analogRead(A1);//Connect the PH Sensor to A1 port
    delay(2.5);
  }
  for(int i=0;i<39;i++)        //sort the analog from small to large
  {
    for(int j=i+1;j<40;j++)
    {
      if(buf[i]>buf[j])
      {
        int temp=buf[i];
        buf[i]=buf[j];
        buf[j]=temp;
      }
    }
  }
       avgValue=0;
 
      for(int i=17;i<23;i++)                      //take the average value of 6 center sample
      avgValue+=buf[i];
    
     sensorValue =   avgValue/6;
    Serial.print(" the PH value is ");
    PHvalue = 7-1000*(sensorValue-365)*Vref/59.16/1023,2;
    Serial.print(PHvalue / 2 );
    Serial.println(" ");
    delay(1000);
    Firebase.setInt(firebaseData,"ph",PHvalue/2);
  
}

void WL(){
  //WATER LEVEL CODE//
    Liquid_level=digitalRead(5);
    Serial.print("Liquid_level= ");
    Serial.println(Liquid_level,DEC);
    delay(1000);
    Firebase.setInt(firebaseData,"water",Liquid_level);
}

void temp(){
  //temperature code
   byte i;
  byte present = 0;
  byte type_s;
  byte data[12];
  byte addr[8];
  float celsius, fahrenheit;
  
  if ( !ds.search(addr)) {
    Serial.println("No more addresses.");
    Serial.println();
    ds.reset_search();
    delay(250);
    return;
  }
  
  Serial.print("ROM =");
  for( i = 0; i < 8; i++) {
    Serial.write(' ');
    Serial.print(addr[i], HEX);
  }

  if (OneWire::crc8(addr, 7) != addr[7]) {
      Serial.println("CRC is not valid!");
      return;
  }
  Serial.println();
 
  // the first ROM byte indicates which chip
  switch (addr[0]) {
    case 0x10:
      Serial.println("  Chip = DS18S20");  // or old DS1820
      type_s = 1;
      break;
    case 0x28:
      Serial.println("  Chip = DS18B20");
      type_s = 0;
      break;
    case 0x22:
      Serial.println("  Chip = DS1822");
      type_s = 0;
      break;
    default:
      Serial.println("Device is not a DS18x20 family device.");
      return;
  } 

  ds.reset();
  ds.select(addr);
  ds.write(0x44,1);         // start conversion, with parasite power on at the end
  
  delay(1000);     // maybe 750ms is enough, maybe not
  // we might do a ds.depower() here, but the reset will take care of it.
  
  present = ds.reset();
  ds.select(addr);    
  ds.write(0xBE);         // Read Scratchpad

  Serial.print("  Data = ");
  Serial.print(present,HEX);
  Serial.print(" ");
  for ( i = 0; i < 9; i++) {           // we need 9 bytes
    data[i] = ds.read();
    Serial.print(data[i], HEX);
    Serial.print(" ");
  }
  Serial.print(" CRC=");
  Serial.print(OneWire::crc8(data, 8), HEX);
  Serial.println();

  // convert the data to actual temperature

  unsigned int raw = (data[1] << 8) | data[0];
  if (type_s) {
    raw = raw << 3; // 9 bit resolution default
    if (data[7] == 0x10) {
      // count remain gives full 12 bit resolution
      raw = (raw & 0xFFF0) + 12 - data[6];
    }
  } else {
    byte cfg = (data[4] & 0x60);
    if (cfg == 0x00) raw = raw << 3;  // 9 bit resolution, 93.75 ms
    else if (cfg == 0x20) raw = raw << 2; // 10 bit res, 187.5 ms
    else if (cfg == 0x40) raw = raw << 1; // 11 bit res, 375 ms
    // default is 12 bit resolution, 750 ms conversion time
  }
  celsius = (float)raw / 16.0;
  fahrenheit = celsius * 1.8 + 32.0;
  Serial.print("  Temperature = ");
  Serial.print(celsius);
  Serial.print(" Celsius, ");
  Serial.print(fahrenheit);
  Serial.println(" Fahrenheit");
  Firebase.setInt(firebaseData,"temp",celsius);
  

  
}
