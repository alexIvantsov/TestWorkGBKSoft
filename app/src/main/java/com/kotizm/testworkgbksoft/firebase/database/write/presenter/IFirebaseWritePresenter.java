package com.kotizm.testworkgbksoft.firebase.database.write.presenter;

import com.kotizm.testworkgbksoft.model.FirebaseData;

public interface IFirebaseWritePresenter {
    void writeItem(FirebaseData item);
    void onDestroy();
}