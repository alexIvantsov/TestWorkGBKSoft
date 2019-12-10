package com.kotizm.testworkgbksoft.firebase.database.delete.data;

import com.kotizm.testworkgbksoft.model.FirebaseData;

public interface IFirebaseDeleteData {

    interface IOnFinishedListener {
        void onDeleteFinished(FirebaseData item);
        void onDeleteFailure(Throwable throwable);
    }
    void deleteItem(IOnFinishedListener onFinishedListener, FirebaseData item);
}