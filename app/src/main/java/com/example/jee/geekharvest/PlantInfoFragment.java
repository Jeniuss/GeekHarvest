package com.example.jee.geekharvest;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jee.geekharvest.m_Firebase.FirebaseHelper;
import com.example.jee.geekharvest.m_Model.*;
import com.example.jee.geekharvest.m_Model.PlantInfo;
import com.example.jee.geekharvest.m_UI.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlantInfoFragment extends Fragment {

    DatabaseReference db;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv ;
    View view;
    String uid;
    public PlantInfoFragment() {
        //INITIALIZE FIREBASE DB
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference().child(uid).child("value");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Plant Information");
    }

    private Handler handler;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_info, container, false);

        helper = new FirebaseHelper(db);
        helper.retrieve();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(!helper.getPlantInfosList().isEmpty()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv = (ListView) view.findViewById(R.id.lv1);
                            adapter = new CustomAdapter(view.getContext(), helper.getPlantInfosList());
                            lv.setAdapter(adapter);

//                            System.out.println("size : "+helper.getPlantInfosList().size());
//                            System.out.println("$$$light_feed : "+helper.getPlantInfosList().get(0).getLight_feed());
//                            System.out.println("$$$day : "+helper.getPlantInfosList().get(0).getDay());
//                            System.out.println("$$$def : "+helper.getPlantInfosList().get(0).getDes());
//                            System.out.println("$$$light_perday : "+helper.getPlantInfosList().get(0).getLightfeedperday());
                        }
                    });
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 500); // wait 1 sec and perform this job again.
                }
            }
        };
        handler.post(runnable); // first time execute.
        return view;
    }

}
