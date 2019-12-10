package com.kotizm.testworkgbksoft.firebase.database.delete.data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kotizm.testworkgbksoft.model.FirebaseData;

@SuppressLint("Registered")
public class FirebaseDeleteData extends AppCompatActivity
        implements IFirebaseDeleteData, ValueEventListener {

    private FirebaseData item;
    private IOnFinishedListener onFinishedListener;

    @Override
    public void deleteItem(IOnFinishedListener onFinishedListener, FirebaseData item) {
        this.item = item;
        this.onFinishedListener = onFinishedListener;

        FirebaseDatabase.getInstance().getReference()
                .orderByChild("id")
                .equalTo(item.getId())
                .addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for(DataSnapshot post : dataSnapshot.getChildren() ){
            post.getRef().removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        onFinishedListener.onDeleteFailure(databaseError.toException());
                    } else {
                        onFinishedListener.onDeleteFinished(item);
                    }
                }
            });
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        onFinishedListener.onDeleteFailure(databaseError.toException());
    }
}