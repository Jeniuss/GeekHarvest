package com.example.jee.geekharvest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public DatabaseReference mDatabase;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private ImageView sun, water, humitemp;
    private String plantID, dateOfPlant, currentDateString, text, valve, humidity, temp, light;
    private String response = null;
    private TextView txtDate, txtWater, txtHumiTemp, txtLight;
    private String soil_status;
    public boolean checkrun1 = true;
    public boolean checkrun2 = true;
    public boolean checkrun3 = true;
    private TextView txtNamePlant, txtEmail;
    private String nameOfPlant, email;



    public MainFragment() {
        //Firebase
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Plant");
        sun = (ImageView)view.findViewById(R.id.imgSun);
        water = (ImageView)view.findViewById(R.id.imgWater);
        humitemp = (ImageView)view.findViewById(R.id.imgHumiTemp);
        txtWater = (TextView) view.findViewById(R.id.txtWater);
        txtHumiTemp = (TextView) view.findViewById(R.id.txtHumiTemp);
        txtLight = (TextView) view.findViewById(R.id.txtLight);

        showStatusValve();
        getValve();
        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkrun1 == true){
                    checkrun1 = false;
                    mDatabase.child("valve").setValue("1");
                }else{
                    checkrun1 = true;
                    mDatabase.child("valve").setValue("0");
                }
            }
        });


        getHumidity();
        humitemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkrun2 == true) {
                    checkrun2 = false;
                    getTemp();
                    System.out.println(checkrun2 + " here1");
                } else if(checkrun2 == false){
                    getHumidity();
                    System.out.println(checkrun2 + " here2");
                    checkrun2 = true;
                }

            }
        });

        getLight();
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkrun3 == true){
                    checkrun3 = false;
                    mDatabase.child("light").setValue("1");
                }else{
                    checkrun3 = true;
                    mDatabase.child("light").setValue("0");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void getLight(){
        mDatabase.child("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                light = dataSnapshot.getValue().toString();
                if(light.equals("0")){
                    txtLight.setText("off");
                }else{
                    txtLight.setText("on");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getHumidity(){
        mDatabase.child("humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                humidity = dataSnapshot.getValue().toString();
                txtHumiTemp.setText("Humidity "+humidity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTemp(){
        mDatabase.child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp = dataSnapshot.getValue().toString();
                txtHumiTemp.setText("Temperature " + temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showStatusValve(){
        mDatabase.child("valve").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valve = dataSnapshot.getValue().toString();
                if(valve.equals("0")){
                    txtWater.setText("off");
                }else{
                    txtWater.setText("on");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void getValve(){
        mDatabase.child("valve").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valve = dataSnapshot.getValue().toString();
                if(valve.equals("0")){
                    txtWater.setText("off");
                }else{
                    txtWater.setText("on");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
