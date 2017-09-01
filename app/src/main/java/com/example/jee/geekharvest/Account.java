package com.example.jee.geekharvest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Account extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button signin, signup;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        mAuth = FirebaseAuth.getInstance(); // importance call

        signin = (Button)findViewById(R.id.bntSignIn);
        signup = (Button)findViewById(R.id.bntSignUp);
        email = (EditText)findViewById(R.id.edtEmail);
        password = (EditText)findViewById(R.id.edtPassword);



        //check if User already LoggedIn
        if(mAuth.getCurrentUser() != null){
            //user not login
            finish();

            startActivity(new Intent(getApplicationContext(), SignIn.class));
        }

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String getEmail = email.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                callSignIn(getEmail, getPassword);


                Log.d("Testing", "I'm here1");
                if (mAuth.getCurrentUser() != null) {
                    String email = mAuth.getCurrentUser().getEmail();
                    String uid = mAuth.getCurrentUser().getUid();
                    Log.d("Testing", "I'm here2");
                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    dataMap.put("email", email);
                    dataMap.put("uid", uid);
                    dataMap.put("soil", 0);
                    dataMap.put("soil_status", "");
                    dataMap.put("valve", "0");
                    dataMap.put("light", "0");
                    dataMap.put("light_status", "");
                    dataMap.put("temperature", 0);
                    dataMap.put("humidity", 0);
                    dataMap.put("nameOfPlant", "");
                    dataMap.put("date of plant", "");
                    dataMap.put("time of plant", "");
                    dataMap.put("plant id", "");
                    dataMap.put("auto valve", "0");
                    dataMap.put("setting_humidity", "0");
                    dataMap.put("setting_light", "0");
                    dataMap.put("setting_phase", "0");
                    dataMap.put("setting_timeAuto", "0");
                    dataMap.put("setting_newPlant", "0");

                    mDatabase.child(uid).setValue(dataMap);
                }else {
                    Log.d("Testing", "I'm here3");
                }




            }
        });


        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

    }


    //Now Start Sign In Process
    //Sign in process
    private void callSignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Testing", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Testing", "signInWithEmail:failed", task.getException());
                            Toast.makeText(Account.this, "Failed", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent i = new Intent(Account.this, SignIn.class);
                            finish();
                            startActivity(i);

                        }

                        // ...
                    }
                });
    }



}
