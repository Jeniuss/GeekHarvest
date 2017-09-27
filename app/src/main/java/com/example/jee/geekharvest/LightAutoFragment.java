package com.example.jee.geekharvest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jee.geekharvest.m_Firebase.FirebaseHelper;
import com.example.jee.geekharvest.m_UI.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LightAutoFragment extends Fragment {
    public DatabaseReference mDatabase;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private Spinner spinner;
    String sSelected;
    Button submit;
    TextView showTime;

    public LightAutoFragment() {
        //Firebase
        mAuth = FirebaseAuth.getInstance(); //importance call
        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(uid);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Light Setup");
        spinner = (Spinner) view.findViewById(R.id.spinner);
        submit = (Button) view.findViewById(R.id.bntSubmit);
        showTime = (TextView) view.findViewById(R.id.txtShowTime);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.array_hour));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sSelected=parentView.getItemAtPosition(position).toString();
                //Toast.makeText(getActivity().getApplicationContext(),sSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int light_feed = Integer.parseInt(sSelected);
                mDatabase.child("light_feed").setValue(light_feed);
                Toast.makeText(getActivity().getApplicationContext(),sSelected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light_auto, container, false);
    }


}
