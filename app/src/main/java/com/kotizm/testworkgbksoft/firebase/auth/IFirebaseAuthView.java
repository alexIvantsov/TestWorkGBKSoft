package com.kotizm.testworkgbksoft.firebase.auth;

import com.google.firebase.auth.FirebaseUser;

public interface IFirebaseAuthView {
    void onResponseFirebaseAuth(FirebaseUser currentUser);
    void onFailureFirebaseAuth(Throwable throwable);
}