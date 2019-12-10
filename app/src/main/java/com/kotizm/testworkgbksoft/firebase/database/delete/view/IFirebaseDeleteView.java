package com.kotizm.testworkgbksoft.firebase.database.delete.view;

import com.kotizm.testworkgbksoft.model.FirebaseData;

public interface IFirebaseDeleteView {
    void onDeleteSuccess(FirebaseData item);
    void onDeleteFailure(Throwable throwable);
}