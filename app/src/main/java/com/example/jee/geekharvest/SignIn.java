package com.example.jee.geekharvest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jee on 5/10/2017 AD.
 */

public class SignIn extends AppCompatActivity{
    private Button start;
    TextView username;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String plantID;

    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 3000L;

    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);

        mAuth = FirebaseAuth.getInstance(); //importance call
        username = (TextView)findViewById(R.id.txtWelcome);
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);

        //Agian check if the user is Already Logged in or Not
        if(mAuth.getCurrentUser() == null){
            //User not Log in
            finish();
            startActivity(new Intent(getApplicationContext(), Account.class));
        }



        //Fetch the Display name of current User
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            username.setText("Welcome to");
        }

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
                Log.d("Testing ", "I'm here1");

            }
        };






    }

    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }




    public void signout(){
        startActivity(new Intent(getApplicationContext(), Account.class));
    }
    public void notification(){
        startActivity(new Intent(getApplicationContext(), Setting.class));
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
