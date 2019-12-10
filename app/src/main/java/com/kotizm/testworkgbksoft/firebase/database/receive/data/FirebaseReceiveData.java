package com.kotizm.testworkgbksoft.firebase.database.receive.data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kotizm.testworkgbksoft.model.FirebaseData;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class FirebaseReceiveData extends AppCompatActivity
        implements IFirebaseReceiveData, ValueEventListener {

    private IFirebaseReceiveData.IOnFinishedListener onFinishedListener;

    @Override
    public void getReceiveData(IFirebaseReceiveData.IOnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;

        FirebaseDatabase.getInstance().getReference("/").orderByChild("name").addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<FirebaseData> dataFirebase = new ArrayList<>();

        for (DataSnapshot data : dataSnapshot.getChildren()) {
            FirebaseData firebaseData = data.getValue(FirebaseData.class);
            dataFirebase.add(firebaseData);
        }
        onFinishedListener.onFinished(dataFirebase);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        onFinishedListener.onFailure(databaseError.toException());
    }
}