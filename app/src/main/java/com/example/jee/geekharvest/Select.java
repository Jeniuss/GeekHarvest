package com.example.jee.geekharvest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Select extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView imgChilli, imgCoriander, imgSunflower, imgBasil, imgCelery, imgEggPlant,
            imgKale, imgLemonBasil, imgRadish, imgSantaTomato, imgSpinach, imgSweetBasil,
            imgThaiEggPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);

        imgChilli = (ImageView) findViewById(R.id.imgChilli);
        imgCoriander = (ImageView) findViewById(R.id.imgCoriander);
        imgSunflower = (ImageView) findViewById(R.id.imgSunflower);
        imgBasil = (ImageView) findViewById(R.id.imgBasil);
        imgCelery = (ImageView) findViewById(R.id.imgCelery);
        imgEggPlant = (ImageView) findViewById(R.id.imgEggPlant);
        imgKale = (ImageView) findViewById(R.id.imgKale);
        imgLemonBasil = (ImageView) findViewById(R.id.imgLemon);
        imgRadish = (ImageView) findViewById(R.id.imgRadish);
        imgSantaTomato = (ImageView) findViewById(R.id.imgTomato);
        imgSpinach = (ImageView) findViewById(R.id.imgSpinach);
        imgSweetBasil = (ImageView) findViewById(R.id.imgSweetBasil);
        imgThaiEggPlant = (ImageView) findViewById(R.id.imgThaiEgg);
        mAuth = FirebaseAuth.getInstance(); //importance call



        imgChilli.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.equals(imgChilli)) {
                    Intent go = new Intent(getApplicationContext(), ChilliInfo.class);
                    startActivity(go);
                }
            }
        });


        imgCoriander.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgCoriander)){
                    Intent go = new Intent(getApplicationContext(), CorianderInfo.class);
                    startActivity(go);
                }
            }
        });

        imgBasil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgBasil)){
                    Intent go = new Intent(getApplicationContext(), BasilInfo.class);
                    startActivity(go);
                }
            }
        });

        imgCelery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgCelery)){
                    Intent go = new Intent(getApplicationContext(), CeleryInfo.class);
                    startActivity(go);
                }
            }
        });

        imgEggPlant.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgEggPlant)){
                    Intent go = new Intent(getApplicationContext(), EggPlantInfo.class);
                    startActivity(go);
                }
            }
        });

        imgKale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgKale)){
                    Intent go = new Intent(getApplicationContext(), KaleInfo.class);
                    startActivity(go);
                }
            }
        });

        imgLemonBasil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgLemonBasil)){
                    Intent go = new Intent(getApplicationContext(), LemonBasilInfo.class);
                    startActivity(go);
                }
            }
        });

        imgRadish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgRadish)){
                    Intent go = new Intent(getApplicationContext(), RadishInfo.class);
                    startActivity(go);
                }
            }
        });

        imgSantaTomato.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgSantaTomato)){
                    Intent go = new Intent(getApplicationContext(), SantaTomatoInfo.class);
                    startActivity(go);
                }
            }
        });

        imgSpinach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgSpinach)){
                    Intent go = new Intent(getApplicationContext(), SpinachInfo.class);
                    startActivity(go);
                }
            }
        });

        imgSweetBasil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgSweetBasil)){
                    Intent go = new Intent(getApplicationContext(), SweetBasilInfo.class);
                    startActivity(go);
                }
            }
        });

        imgThaiEggPlant.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgThaiEggPlant)){
                    Intent go = new Intent(getApplicationContext(), ThaiEggPlantInfo.class);
                    startActivity(go);
                }
            }
        });

        imgSunflower.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(imgSunflower)){
                    Intent go = new Intent(getApplicationContext(), SunflowerInfo.class);
                    startActivity(go);
                }
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
                notification();
                return true;
            case R.id.action_signout:
                mAuth.signOut();
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
