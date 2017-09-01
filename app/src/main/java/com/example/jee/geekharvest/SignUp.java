package com.example.jee.geekharvest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText username, email, password;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup);

        mAuth = FirebaseAuth.getInstance(); //importance call
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username = (EditText)findViewById(R.id.edtUsername2);
        email = (EditText)findViewById(R.id.edtEmail);
        password = (EditText)findViewById(R.id.edtPassword);
        signup = (Button)findViewById(R.id.bntSignUp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                String getUsername = username.getText().toString().trim();
                callSignUp(getEmail, getPassword);
                startActivity(new Intent(getApplicationContext(), Account.class));
            }
        });


    }

    private void callSignUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Testing", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "SIGNUP FAILED", Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(SignUp.this, "CREATED ACCOUNT", Toast.LENGTH_SHORT).show();
                            Log.d("Testing", "Created Account");


                        }

                        // ...
                    }
                });
    }


}
