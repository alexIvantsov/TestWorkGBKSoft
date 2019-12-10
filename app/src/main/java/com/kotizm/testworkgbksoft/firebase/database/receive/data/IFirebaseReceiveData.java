package com.kotizm.testworkgbksoft.firebase.database.receive.data;

import com.kotizm.testworkgbksoft.model.FirebaseData;

import java.util.List;

public interface IFirebaseReceiveData {

    interface IOnFinishedListener {
        void onFinished(List<FirebaseData> dataFirebase);
        void onFailure(Throwable throwable);
    }
    void getReceiveData(IOnFinishedListener onFinishedListener);
}