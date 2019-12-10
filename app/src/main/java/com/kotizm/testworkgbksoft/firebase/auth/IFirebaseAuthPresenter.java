package com.kotizm.testworkgbksoft.firebase.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface IFirebaseAuthPresenter {
    void setListener();
    void onDestroy();

    void signOut();
    void signIn(GoogleSignInAccount acct);
    GoogleSignInClient getGoogleSignInClient();
}