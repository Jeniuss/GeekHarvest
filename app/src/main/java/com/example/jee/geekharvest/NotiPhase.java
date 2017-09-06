package com.example.jee.geekharvest;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotiPhase extends BroadcastReceiver {

    private DatabaseReference mDatabase, plantDB, emailDB;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView sun, water, humidity;
    private int num, month1, mLength1,month2, mLength2,fd,fy,countDate, mCount, numPhase, check; //เลขของ id ใน php ว่าเราเอาค่ามาเพื่อดึงให้ตรงกับพืชที่เราต้องการเทียบ phase ของมัน
    private String[] phase;
    private String plantID, dateOfPlant, currentDateString, text, valve;
    private String response = null;
    private String m[] = new String[2];
    private String d[] = new String[2];
    private String y[] = new String[2];
    private String word1[], word2[] = new String[3];
    private String soil_status;
    public boolean checkrun = true;

    @Override
    public void onReceive(final Context context, Intent intent){

        //Firebase
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);

        currentDateString = DateFormat.getDateInstance().format(new Date());
        getNum(context);
        dateFromFirebase();


    }

    public void comparePhase(final Context context){
        if(numPhase == 1){
            text = "Sprouting";
        }else if(numPhase == 2){
            text = "Growing";
        }else if(numPhase == 3){
            text = "Harvest";
        }else if(numPhase == 4) {
            text = "Complete";
        }else{
            text = "In Process";
        }

        System.out.println("here1 " + numPhase);
        System.out.println("here2 " + check);
        if( check != numPhase) {
            System.out.println("jee " + text);
            System.out.println("here3 " + numPhase);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("Test", "here");

            android.app.Notification notification =
                    new NotificationCompat.Builder(context) // this is context
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Advice!!!")
                            .setContentText(text)
                            .setAutoCancel(true)
                            .build();

            notificationManager.notify(10, notification);
            System.out.println("here4 " + check);
            check = numPhase;
            System.out.println("here5 " + check);
        } else {

        }
        System.out.println("here6" + numPhase);
    }
    public void getNum(final Context context){
        mDatabase.child("plant id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                plantID = dataSnapshot.getValue().toString();
                num = Integer.parseInt(plantID);
                Log.d("Num ", num+"");
                dateFromDB();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() { //firebaseทำงานแบบ a asynchronous
                    @Override
                    public void run() {
                        spiltDate();
                        define();
                        compareDate();
                        noti();
                        comparePhase(context);
                    }
                }, 500);
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

        NotiPhase.getHttp http = new NotiPhase.getHttp();

        try {
            response = http.run("http://geekharvest.ilab.sit.kmutt.ac.th/connect.php?plant_id="+num); //ส่งค่าplant_idกลับไป php และส่งกลับมา android
            Log.d("DBCONNECT2", "http://geekharvest.ilab.sit.kmutt.ac.th/connect.php?plant_id="+num);
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
            text = "Sprouting";
            System.out.println(text);
            numPhase = 1;
            System.out.println(numPhase);
        }else if(fd == Integer.parseInt(phase[1])){
            text = "Growing";
            System.out.println(text);
            numPhase = 2;
            System.out.println(numPhase);
        }else if(fd == Integer.parseInt(phase[2])){
            text = "Harvest";
            System.out.println(text);
            numPhase = 3;
            System.out.println(numPhase);
        }else if(fd > Integer.parseInt(phase[2])){
            text = "Complete";
            System.out.println(text);
            numPhase = 4;
            System.out.println(numPhase);
        }else {
            text = "In Process";
            System.out.println(text);
            numPhase = 5;
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
}
