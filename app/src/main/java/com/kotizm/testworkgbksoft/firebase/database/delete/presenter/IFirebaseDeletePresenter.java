package com.kotizm.testworkgbksoft.firebase.database.delete.presenter;

import com.kotizm.testworkgbksoft.model.FirebaseData;

public interface IFirebaseDeletePresenter {
    void deleteItem(FirebaseData item);
    void onDestroy();
}