package com.example.jee.geekharvest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jee on 6/11/2017 AD.
 */


public class ConnectDB extends AppCompatActivity {
    TextView txt1, txt2;
    Button bnt1;
    String plant_id;
    int num = 2;
    private String[] phase;
    String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_db_test);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        bnt1 = (Button) findViewById(R.id.bnt1);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        getHttp http = new getHttp();
        //ConnectDB conn = new ConnectDB();

        try {
            response = http.run("http://geekharvest.ilab.sit.kmutt.ac.th/connect.php?plant_id="+num); //ส่งค่าplant_idกลับไป php และส่งกลับมา android
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        spiteWord(response);

        txt1.setText(phase[1]);

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

}
