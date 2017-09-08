package com.example.jee.geekharvest;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private TextView txtNamePlant, txtEmail;
    private String nameOfPlant, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);


        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
        plantDB = FirebaseDatabase.getInstance().getReference().child(uid).child("nameOfPlant");
        emailDB = FirebaseDatabase.getInstance().getReference().child(uid).child("email");
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtNamePlant = (TextView) findViewById(R.id.txtNamePlant);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        getNamePlant();
        getEmail();

    }
    public void getNamePlant(){
        mDatabase.child("nameOfPlant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameOfPlant = dataSnapshot.getValue().toString();
                txtNamePlant.setText(nameOfPlant);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getEmail(){
        mDatabase.child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue().toString();
                txtEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
