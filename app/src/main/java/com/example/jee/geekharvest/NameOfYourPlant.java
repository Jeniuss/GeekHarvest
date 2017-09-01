package com.example.jee.geekharvest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jee on 5/14/2017 AD.
 */

public class NameOfYourPlant extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText name;
    private Button setName, selectPlant;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setnameofplant);

        mAuth = FirebaseAuth.getInstance(); //importance call
        name = (EditText)findViewById(R.id.edtNamePlant);
        setName = (Button)findViewById(R.id.bntSetName);
        selectPlant = (Button)findViewById(R.id.bntSelectPlant);
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);

        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePlant = name.getText().toString().trim();
                mDatabase.child("nameOfPlant").setValue(namePlant);
            }
        });

        selectPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getApplicationContext(), Select.class);
                startActivity(go);
                finish();
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
