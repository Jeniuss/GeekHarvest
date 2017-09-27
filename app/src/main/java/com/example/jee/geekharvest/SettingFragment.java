package com.example.jee.geekharvest;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public DatabaseReference mDatabase;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private Switch sw1,sw2,sw3,sw4,sw5,sw6;
    private String soil_status, autoValve;
    SharedPreferences sp;
    private String text;
    private int num1, check1, num2, check2, numPhase;
    private boolean correct = true;
    private Button reset;
    private String num;


    public SettingFragment() {
        //Firebase
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Setting");
        getSoilStatus();
//        comparePhase();

        sw1 = (Switch) view.findViewById(R.id.switch1);
        sw2 = (Switch) view.findViewById(R.id.switch2);
        sw3 = (Switch) view.findViewById(R.id.switch3);
        sw4 = (Switch) view.findViewById(R.id.switch4);
        sw5 = (Switch) view.findViewById(R.id.switch5);
        sw6 = (Switch) view.findViewById(R.id.switch6);
        reset = (Button) view.findViewById(R.id.bntReset);

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


        sw1.setOnClickListener(new View.OnClickListener() { //humidity
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), NotiMoisture.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

                if(sw1.isChecked() == true) {
                    Calendar calendar = Calendar.getInstance();
                    mDatabase.child("setting_humidity").setValue("1");
                    Log.d("Test ", "I'm here 1");

                    /* Repeating on every 1 minutes interval */
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 15,pendingIntent);
                }else{
                    Log.d("Test ", "I'm here 2");
                    alarmManager.cancel(pendingIntent);
                    mDatabase.child("setting_humidity").setValue("0");
                }
            }
        });


        sw3.setOnClickListener(new View.OnClickListener() { //phaseofPlant
            Intent intent = new Intent(getActivity().getApplicationContext(), NotiPhase.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            @Override
            public void onClick(View v) {
                if (sw3.isChecked() == true) {
                    Calendar calendar = Calendar.getInstance();
                    mDatabase.child("setting_phase").setValue("1");
                    Log.d("Test ", "I'm here 1");

                    /* Repeating on every 1 minutes interval */
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * 15,pendingIntent);
                }else {
                    Log.d("Test ", "I'm here 2");
                    alarmManager.cancel(pendingIntent);
                    mDatabase.child("setting_phase").setValue("0");
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
            Intent intent = new Intent(getActivity().getApplicationContext(), NotiNewPlant.class);
            @Override
            public void onClick(View v) {
                if (sw5.isChecked() == true) {
                    Log.d("Test ", "I'm here 1");
                    getActivity().sendBroadcast(intent);
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
                    mDatabase.child("setting_humidity").setValue("0");
                }
            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sw1.setChecked(false);
                mDatabase.child("setting_humidity").setValue("0");
                sw2.setChecked(false);
                sw3.setChecked(false);
                sw4.setChecked(false);
                sw5.setChecked(false);
                sw6.setChecked(false);
                mDatabase.child("auto valve").setValue("0");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    public void getSoilStatus(){
        mDatabase.child("soil_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                soil_status = dataSnapshot.getValue().toString();
                Log.d("Soil Status ", soil_status);

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

}
