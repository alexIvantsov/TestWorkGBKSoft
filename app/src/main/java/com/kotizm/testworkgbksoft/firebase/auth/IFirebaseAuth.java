package com.kotizm.testworkgbksoft.firebase.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

public interface IFirebaseAuth {

    interface IOnFinishedListener {
        void onFinished(FirebaseUser currentUser);
        void onFailure(Throwable throwable);
    }

    void signOut();
    void signIn(GoogleSignInAccount acct);

    GoogleSignInClient getGoogleSignInClient();
    void setListener(IOnFinishedListener onFinishedListener);
}