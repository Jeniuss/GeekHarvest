package com.example.jee.geekharvest;

import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Jee on 7/11/2017 AD.
 */

public class NotiMoisture extends BroadcastReceiver {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String soil_status;
    public String text, temp, temp_soil_status;


    @Override
    public void onReceive(final Context context, Intent intent){
        context.startService(new Intent(context.getApplicationContext(), StarterService.class));
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);


        mDatabase.child("temp_soil_status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp_soil_status = dataSnapshot.getValue().toString();
                temp = temp_soil_status ;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { //firebaseทำงานแบบ a asynchronous
            @Override
            public void run() {
                mDatabase.child("soil_status").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        soil_status = dataSnapshot.getValue().toString();
                        Log.d("Soil Status1 ", soil_status);

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Log.d("Test", "here");
                        System.out.println("temp " + temp);

                        if(!(soil_status.equals(temp))) {
                            setText(soil_status);

                            android.app.Notification notification =
                                    new NotificationCompat.Builder(context) // this is context
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Advice!!!")
                                            .setContentText(text)
                                            .setAutoCancel(true)
                                            .build();

                            notificationManager.notify(100, notification);
                        }
                        mDatabase.child("temp_soil_status").setValue(soil_status);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }, 500);


    }

    public void setText(String soil){
        if(soil.equals("Low Humidity")){
            text = "YOUR PLANT WILL BE DRY, LET'S FILL UP A WATER!";
        }else if(soil.equals("High Humidity")){
            text = "YOUR PLANT WILL BE DRY, LET'S TURN ON THE LED!";
        }else if(soil.equals("Normal Humidity")){
            text = "YOUR PLANT IS HAPPY!";
        }else{
            text = "LET'S CROP THE PLANT";
        }
    }




}
