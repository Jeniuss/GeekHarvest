package com.example.jee.geekharvest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jee on 5/25/2017 AD.
 */

public class Setting extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Switch sw1,sw2,sw3,sw4,sw5,sw6;
    private DatabaseReference mDatabase;
    private String soil_status, autoValve;
    SharedPreferences sp;
    private String text;
    private int num1, check1, num2, check2, numPhase;
    private boolean correct = true;
    private Button reset;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);


        getSoilStatus();
//        comparePhase();

        sw1 = (Switch) findViewById(R.id.switch1);
        sw2 = (Switch) findViewById(R.id.switch2);
        sw3 = (Switch) findViewById(R.id.switch3);
        sw4 = (Switch) findViewById(R.id.switch4);
        sw5 = (Switch) findViewById(R.id.switch5);
        sw6 = (Switch) findViewById(R.id.switch6);
        reset = (Button) findViewById(R.id.bntReset);

        mDatabase.child("setting_humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue().toString();
                if(num.equals("1")){
                    sw1.setChecked(true);
                }else{
                    sw1.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("setting_light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue().toString();
                if(num.equals("1")){
                    sw2.setChecked(true);
                }else{
                    sw2.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("setting_phase").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue().toString();
                if(num.equals("1")){
                    sw3.setChecked(true);
                }else{
                    sw3.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("setting_timeAuto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue().toString();
                if(num.equals("1")){
                    sw4.setChecked(true);
                }else{
                    sw4.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("setting_newPlant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue().toString();
                if(num.equals("1")){
                    sw5.setChecked(true);
                }else{
                    sw5.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("auto valve").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = dataSnapshot.getValue().toString();
                if(num.equals("1")){
                    sw6.setChecked(true);
                }else{
                    sw6.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        sw1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(correct){
//                    if(sw1.isChecked() == true && check2 != num2) {
//                        System.out.print("2" + num2);
//                        System.out.print("3" + check2);
//                        android.app.Notification notification =
//                                new NotificationCompat.Builder(Setting.this) // this is context
//                                        .setSmallIcon(R.mipmap.ic_launcher)
//                                        .setContentTitle("DevAhoy News")
//                                        .setContentText(soil_status)
//                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                        .setAutoCancel(true)
//                                        .setTicker(getString(R.string.poptext))
//                                        .build();
//
//                        NotificationManager notificationManager =
//                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        notificationManager.notify(1, notification);
//                        check2 = num2;
//                        System.out.print("4" + check2);
//                    }
//                    correct = true;
//                }else{
//
//                }
//            }
//        });

        sw1.setOnClickListener(new View.OnClickListener() { //humidity
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NotiMoisture.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                if(sw1.isChecked() == true) {
                    Calendar calendar = Calendar.getInstance();
                    mDatabase.child("setting_humidity").setValue("1");
                    Log.d("Test ", "I'm here 1");

                    /* Repeating on every 1 minutes interval */
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 60,pendingIntent);
                }else{
                    Log.d("Test ", "I'm here 2");
                    alarmManager.cancel(pendingIntent);
                    mDatabase.child("setting_humidity").setValue("0");
                }
            }
        });


        sw3.setOnClickListener(new View.OnClickListener() { //phaseofPlant
            Intent intent = new Intent(Setting.this, NotiPhase.class);
            @Override
            public void onClick(View v) {
                if (sw3.isChecked() == true) {
                    Log.d("Test ", "I'm here 1");
                    sendBroadcast(intent);
                }else {
                    Log.d("Test ", "I'm here 2");
                }
            }
        });

        sw4.setOnClickListener(new View.OnClickListener() { //timeusingAuto
            @Override
            public void onClick(View v) {
                if(sw4.isChecked() == true) {

                }else{

                }
            }

        });

        sw5.setOnClickListener(new View.OnClickListener() { //newPlant
            Intent intent = new Intent(Setting.this, NotiNewPlant.class);
            @Override
            public void onClick(View v) {
                if (sw5.isChecked() == true) {
                    Log.d("Test ", "I'm here 1");
                    sendBroadcast(intent);
                }else {
                    Log.d("Test ", "I'm here 2");
                }
            }
        });

        sw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //autoValve
                if(sw6.isChecked() == true) {
                    mDatabase.child("auto valve").setValue("1");
                }else{
                    mDatabase.child("auto valve").setValue("0");
                }
            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sw1.setChecked(false);
                sw2.setChecked(false);
                sw3.setChecked(false);
                sw4.setChecked(false);
                sw5.setChecked(false);
                sw6.setChecked(false);
            }
        });

    }

    public void getSoilStatus(){
        mDatabase.child("soil_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                soil_status = dataSnapshot.getValue().toString();
                Log.d("Soil Status ", soil_status+"");

                if(soil_status.equals("Low Humidity")){
                    num2 = 1;
                }else if(soil_status.equals("Normal Humidity")){
                    num2 = 2;
                }else if(soil_status.equals("High Humidity")){
                    num2 = 3;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


//    public void comparePhase(){
////        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
////        numPhase = sp.getInt("numPhase", 0);
//        Bundle bundle = getIntent().getExtras();
//        numPhase = bundle.getInt("numPhase");
//        if(numPhase == 1){
//            text = "You are phase 1";
//        }else if(numPhase == 2){
//            text = "You are phase 2";
//        }else if(numPhase == 3){
//            text = "You are phase 3";
//        }else{
//            text = "In Process";
//        }
//    }

    public void signout(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // เก็บค่า id ของปุ่ม action butoon ที่กดเลือก
        int id = item.getItemId();

        // ตรวจสอบค่า ว่า เป็น id ใด  แล้วเรียกใช้ method ที่เราสร้างขึ้น
        // ในตัวอย่างนี้ เราจะส่งค่าข้อความเข้าไปใน method
        switch (id) {
            case R.id.action_signout:
                mAuth.signOut();
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
