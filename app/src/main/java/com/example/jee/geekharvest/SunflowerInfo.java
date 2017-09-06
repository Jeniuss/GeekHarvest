package com.example.jee.geekharvest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Jee on 5/25/2017 AD.
 */

public class SunflowerInfo extends AppCompatActivity {
    private Button crop13;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    public String dateOfPlant, currentDateString, currentTimeString, plant_id;
    private String plantID, setID, nameOfPlant;
    private String[] blank, phase;
    private String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._sunflower);

        Date date = new Date();
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
        //user = FirebaseAuth.getInstance().getCurrentUser();
        setID = "13";


        crop13 = (Button)findViewById(R.id.bntCrop13);


        crop13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentDateString = DateFormat.getDateInstance().format(new Date());
                currentTimeString = DateFormat.getTimeInstance().format(new Date());


                mDatabase.child("plant id").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        plantID = dataSnapshot.getValue().toString();

                        if(plantID.equals("")){
                            mDatabase.child("plant id").setValue(setID);
                        }else{
                            Log.d("Testing ", plantID);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mDatabase.child("nameOfPlant").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nameOfPlant = dataSnapshot.getValue().toString();

                        if(nameOfPlant.equals("")){
                            mDatabase.child("nameOfPlant").setValue("SunFlower");
                        }else{
                            Log.d("Testing ", nameOfPlant);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mDatabase.child("date of plant").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Testing ", "d");
                        dateOfPlant = dataSnapshot.getValue().toString();

                        if(dateOfPlant.equals("")){
                            mDatabase.child("date of plant").setValue(currentDateString);
                        }

                        Log.d("Testing ", "p");
                        Log.d("Testing ", dateOfPlant);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mDatabase.child("time of plant").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Testing ", "d");
                        dateOfPlant = dataSnapshot.getValue().toString();

                        if(dateOfPlant.equals("")){
                            mDatabase.child("time of plant").setValue(currentTimeString);
                        }

                        Log.d("Testing ", "p");
                        Log.d("Testing ", dateOfPlant);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent go = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(go);
            }
        });


    }

    public void signout(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }
    public void notification(){startActivity(new Intent(getApplicationContext(), Setting.class));}

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
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), Setting.class));
                return true;
            case R.id.action_signout:
                mAuth.signOut();
                signout();
                return true;
            case R.id.action_db:
                startActivity(new Intent(getApplicationContext(), ConnectDB.class));
                return true;
            case R.id.action_nav_header:
                startActivity(new Intent(getApplicationContext(), NavHeader.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
