package com.kotizm.testworkgbksoft.firebase.database.receive.view;

import com.kotizm.testworkgbksoft.model.FirebaseData;

import java.util.List;

public interface IFirebaseReceiveView {
    void onResponseSuccess(List<FirebaseData> dataFirebase);
    void onResponseFailure(Throwable throwable);
}