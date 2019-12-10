package com.kotizm.testworkgbksoft.firebase.database.write.data;

import com.kotizm.testworkgbksoft.model.FirebaseData;

public interface IFirebaseWriteData {

    interface IOnFinishedListener {
        void onWriteFinished(String id, FirebaseData item);
        void onWriteFailure(Throwable throwable);
    }
    void writeItem(IOnFinishedListener onFinishedListener, FirebaseData item);
}