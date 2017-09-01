package com.example.jee.geekharvest;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jee on 8/23/2017 AD.
 */

public class NotiNewPlant extends BroadcastReceiver {

    private String response = null;
    private int cntPlantID, tempCnt;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("Test ", "I'm hereeeeeeee eiei");
        comparePlantID(context);

    }

    public void dateFromDB(){
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        NotiNewPlant.getHttp http = new NotiNewPlant.getHttp();

        try {
            response = http.run("http://geekharvest.sit.kmutt.ac.th/countPlant.php"); //ส่งค่าplant_idกลับไป php และส่งกลับมา android
            Log.d("DBCONNECT2", "http://geekharvest.sit.kmutt.ac.th/countPlant.php");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tempCnt = Integer.parseInt(response);

        Log.d("cnt", tempCnt+"");
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

    public void comparePlantID(final Context context){
        dateFromDB();
        if(tempCnt != cntPlantID){
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("Test", "here");

            android.app.Notification notification =
                    new NotificationCompat.Builder(context) // this is context
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Advice!!!")
                            .setContentText("NEW PLANT IS UPDATED LET'S CHECK IT OUT")
                            .setAutoCancel(true)
                            .build();

            notificationManager.notify(11, notification);
            tempCnt = cntPlantID;
        }else{

        }
    }

}
