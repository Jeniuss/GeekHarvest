package com.example.jee.geekharvest;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by Jee on 5/23/2017 AD.
 */

public class Test extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, nDatabase;
    private FirebaseUser user;
    private TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
        nDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("date of plant");
        user = FirebaseAuth.getInstance().getCurrentUser();
        mEmail = (TextView) findViewById(R.id.txtEmailTest);

        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue().toString();
                mEmail.setText("date of plant : " + email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setContentTitle(String title){
        System.out.print(title);
    }

//    public void showNotification(View view){
//        Setting setting = new NotificationCompat.Builder(this) // this is context
//        .setSmallIcon(R.mipmap.ic_launcher)
//        .setContentTitle("DevAhoy News")
//        .setContentText("สวัสดีครับ ยินดีต้อนรับเข้าสู่บทความ Android Setting :)").setAutoCancel(true)
//        .build();
//    }

}
