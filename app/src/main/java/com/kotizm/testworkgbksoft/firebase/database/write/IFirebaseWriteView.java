package com.kotizm.testworkgbksoft.firebase.database.write;

import com.kotizm.testworkgbksoft.model.FirebaseData;

public interface IFirebaseWriteView {
    void onWriteSuccess(String id, FirebaseData item);
    void onWriteFailure(Throwable throwable);
}