package com.example.jee.geekharvest;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.support.design.widget.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.util.Date;


/**
 * Created by Jee on 5/14/2017 AD.
 */

public class Plant extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView sun, water, humitemp;
    private int num, month1, mLength1,month2, mLength2,fd,fy,countDate, mCount, numPhase; //เลขของ id ใน php ว่าเราเอาค่ามาเพื่อดึงให้ตรงกับพืชที่เราต้องการเทียบ phase ของมัน
    private String[] phase;
    private String plantID, dateOfPlant, currentDateString, text, valve, humidity, temp, light;
    private String response = null;
    private String m[] = new String[2];
    private String d[] = new String[2];
    private String y[] = new String[2];
    private String word1[], word2[] = new String[3];
    private TextView txtDate, txtWater, txtHumiTemp, txtLight;
    private String soil_status;
    public boolean checkrun1 = true;
    public boolean checkrun2 = true;
    public boolean checkrun3 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant);


        //Firebase
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);


        sun = (ImageView)findViewById(R.id.imgSun);
        water = (ImageView)findViewById(R.id.imgWater);
        humitemp = (ImageView)findViewById(R.id.imgHumiTemp);
        txtDate = (TextView) findViewById(R.id.txtDay);
        txtWater = (TextView) findViewById(R.id.txtWater);
        txtHumiTemp = (TextView) findViewById(R.id.txtHumiTemp);
        txtLight = (TextView) findViewById(R.id.txtLight);
        checkrun1 = true;

        currentDateString = DateFormat.getDateInstance().format(new Date());
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

        getNum();
        dateFromFirebase();
        //getSoilStatus();


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

    public void getNum(){
        mDatabase.child("plant id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                plantID = dataSnapshot.getValue().toString();
                num = Integer.parseInt(plantID);
                Log.d("Num ", num+"");
                dateFromDB();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void dateFromFirebase(){
        mDatabase.child("date of plant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dateOfPlant = dataSnapshot.getValue().toString();
                Log.d("Datejaaa", dateOfPlant);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() { //firebaseทำงานแบบ a asynchronous
                    @Override
                    public void run() {
                        spiltDate();
                        define();
                        compareDate();
                        noti();
                        txtDate.setText(fd + " Days");
                    }
                }, 500);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void dateFromDB(){
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getHttp http = new getHttp();

        try {
            response = http.run("http://geekharvest.sit.kmutt.ac.th/connect.php?plant_id="+num); //ส่งค่าplant_idกลับไป php และส่งกลับมา android
            Log.d("DBCONNECT2", "http://geekharvest.sit.kmutt.ac.th/connect.php?plant_id="+num);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        spiteWord(response);
        Log.d("Phase", phase[0]);
    }

    public class getHttp {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    public void spiteWord(String response){
        phase = response.split(" ");
        for(int i = 0; i < phase.length; i++){
            System.out.println(phase[i]);
        }
    }

    public void noti(){
        if(fd == Integer.parseInt(phase[0])){
            text = "You are phase 1";
            System.out.println(text);
            numPhase = 1;
            System.out.println(numPhase);
        }else if(fd == Integer.parseInt(phase[1])){
            text = "You are phase 2";
            System.out.println(text);
            numPhase = 2;
            System.out.println(numPhase);
        }else if(fd == Integer.parseInt(phase[2])){
            text = "You are phase 3";
            System.out.println(text);
            numPhase = 3;
            System.out.println(numPhase);
        }else {
            text = "In Process";
            System.out.println(text);
            numPhase = 4;
            System.out.println(numPhase);
        }
    }

    public void spiltDate(){
        word1 = dateOfPlant.split(" ");
        m[0] = word1[0];
        d[0] = word1[1].substring(0, word1[1].length()-1);
        y[0] = word1[2];
        word2 = currentDateString.split(" ");
        m[1] = word2[0];
        d[1] = word2[1].substring(0, word2[1].length()-1);
        y[1] = word2[2];

    }

    public void define(){
        switch(m[0]){
            case "Jan": month1 = 1; mLength1 = 31; break;
            case "Feb": month1= 2; mLength1 = 28; break;
            case "Mar": month1 = 3; mLength1 = 31; break;
            case "Apr": month1= 4; mLength1 = 30; break;
            case "May": month1 = 5; mLength1 = 31; break;
            case "Jun": month1 = 6; mLength1 = 30; break;
            case "Jul": month1 = 7; mLength1 = 31; break;
            case "Aug": month1 = 8; mLength1 = 31; break;
            case "Sep": month1 = 9; mLength1 = 30; break;
            case "Oct": month1 = 10; mLength1 = 31; break;
            case "Nov": month1 = 11; mLength1 = 30; break;
            case "Dec": month1 = 12; mLength1 = 31; break;
        }

        switch(m[1]){
            case "Jan": month2 = 1; mLength2 = 31; break;
            case "Feb": month2 = 2; mLength2 = 28; break;
            case "Mar": month2 = 3; mLength2 = 31; break;
            case "Apr": month2 = 4; mLength2 = 30; break;
            case "May": month2 = 5; mLength2 = 31; break;
            case "Jun": month2 = 6; mLength2 = 30; break;
            case "Jul": month2 = 7; mLength2 = 31; break;
            case "Aug": month2 = 8; mLength2 = 31; break;
            case "Sep": month2 = 9; mLength2 = 30; break;
            case "Oct": month2 = 10; mLength2 = 31; break;
            case "Nov": month2 = 11; mLength2 = 30; break;
            case "Dec": month2 = 12; mLength2 = 31; break;

        }

    }

    public void compareDate(){
        fy = Integer.parseInt(y[1]) - Integer.parseInt(y[0]);

        mCount = month1;
        mCount++;

        if(fy == 0)
        {
            if(month1 == month2)
            {
                fd = Integer.parseInt(d[1]) - Integer.parseInt(d[0])+1;
                System.out.println("Case 1 ="+fd);
            }
            else if (month2 - month1 == 1)
            {

                fd = mLength1 - Integer.parseInt(d[0]);
                fd += Integer.parseInt(d[1])+1;
                System.out.println("Case 2 ="+fd);
            }
            else
            {
                fd = mLength1 - Integer.parseInt(d[0]);
                for(; mCount < month2; mCount++)
                {
                    countDate();
                }
                fd+= countDate +Integer.parseInt(d[1])+1;
                System.out.println("Case 3 ="+fd);
            }
        }

        else
        {
            fd = mLength1 - Integer.parseInt(d[0]);

            while(fy != 0)
            {
                for(; mCount <= 12 ; mCount++)
                {
                    countDate();
                }
                fy--;
                if(mCount == 13) mCount = 1;
            }

            for(; mCount < month2; mCount++)
            {
                countDate();
            }

            fd+= countDate +Integer.parseInt(d[1])+1;
            System.out.println("Case 4 ="+fd);
        }
    }

    public void countDate(){
        switch(mCount){
            case 1: countDate += 31; break;
            case 2: countDate += 28; break;
            case 3: countDate += 31; break;
            case 4: countDate += 30; break;
            case 5: countDate += 31; break;
            case 6: countDate += 30; break;
            case 7: countDate += 31; break;
            case 8: countDate += 31; break;
            case 9: countDate += 30; break;
            case 10: countDate += 31; break;
            case 11: countDate += 30; break;
            case 12: countDate += 31; break;
        }
    }

    public void notification(){startActivity(new Intent(getApplicationContext(), Setting.class));}

    public void signout(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        //inflater.inflate(R.menu.activity_navigator_drawer_drawer, menu);

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
