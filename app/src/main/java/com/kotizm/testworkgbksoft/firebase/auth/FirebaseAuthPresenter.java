package com.kotizm.testworkgbksoft.firebase.auth;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthPresenter implements IFirebaseAuthPresenter, IFirebaseAuth.IOnFinishedListener {

    private IFirebaseAuth getNoticeIntractor;
    private IFirebaseAuthView iFirebaseAuthView;

    public FirebaseAuthPresenter(IFirebaseAuthView iFirebaseAuthView, IFirebaseAuth getNoticeIntractor) {
        this.iFirebaseAuthView = iFirebaseAuthView;
        this.getNoticeIntractor = getNoticeIntractor;
    }

    @Override
    public void setListener() {
        getNoticeIntractor.setListener(this);
    }

    @Override
    public void signOut() {
        if(iFirebaseAuthView != null){
            getNoticeIntractor.signOut();
        }
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return getNoticeIntractor.getGoogleSignInClient();
    }

    @Override
    public void signIn(GoogleSignInAccount acct) {
        if(iFirebaseAuthView != null){
            getNoticeIntractor.signIn(acct);
        }
    }

    @Override
    public void onFinished(FirebaseUser currentUser) {
        if(iFirebaseAuthView != null){
            iFirebaseAuthView.onResponseFirebaseAuth(currentUser);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if(iFirebaseAuthView != null){
            iFirebaseAuthView.onFailureFirebaseAuth(throwable);
        }
    }

    @Override
    public void onDestroy() {
        iFirebaseAuthView = null;
    }
}