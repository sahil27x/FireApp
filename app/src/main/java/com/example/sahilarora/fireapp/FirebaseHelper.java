package com.example.sahilarora.fireapp;

/**
 * Created by sahilarora on 26/01/17.
 */

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<String> cars=new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE
    public Boolean save(UserCar uc)
    {
        if(uc==null)
        {
            saved=false;
        }else
        {
            try
            { DatabaseReference mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fireapp-49e2d.firebaseio.com/Cars");

                mRef.setValue(uc);
                /*db.child("Cars").push().setValue(uc);
                saved=true;*/
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    //READ
    /*public ArrayList<String> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return cars;
    }

    private void fetchData(DataSnapshot dataSnapshot)
    {


        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name=ds.getValue(UserCar.class).getName();
            cars.add(name);
        }
    }*/
}