package com.example.jee.geekharvest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jee on 8/7/2017 AD.
 */

public class NavHeader extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, plantDB, emailDB;
    private FirebaseUser user;
    private TextView namePlant, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
        plantDB = FirebaseDatabase.getInstance().getReference().child(uid).child("nameOfPlant");
        emailDB = FirebaseDatabase.getInstance().getReference().child(uid).child("email");
        user = FirebaseAuth.getInstance().getCurrentUser();
        namePlant = (TextView) findViewById(R.id.txtNamePlant);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        plantDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameOfPlant = dataSnapshot.getValue().toString();
                namePlant.setText(nameOfPlant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        emailDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue().toString();
                txtEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
