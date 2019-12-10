package com.kotizm.testworkgbksoft.firebase.database.write.data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kotizm.testworkgbksoft.model.FirebaseData;

@SuppressLint("Registered")
public class FirebaseWriteData extends AppCompatActivity implements IFirebaseWriteData {

    private String id;

    @Override
    public void writeItem(final IOnFinishedListener onFinishedListener, final FirebaseData item) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        id = databaseReference.push().getKey();
        item.setId(id);

        databaseReference.push().setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onFinishedListener.onWriteFinished(id, item);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        onFinishedListener.onWriteFailure(exception);
                    }
                });
    }
}