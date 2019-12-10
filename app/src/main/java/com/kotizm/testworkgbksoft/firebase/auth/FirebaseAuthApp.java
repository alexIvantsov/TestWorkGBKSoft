package com.kotizm.testworkgbksoft.firebase.auth;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.utils.dialog.progress.IMyProgressDialog;
import com.kotizm.testworkgbksoft.utils.dialog.progress.MyProgressDialog;

import java.lang.ref.WeakReference;

@SuppressLint("Registered")
public class FirebaseAuthApp extends AppCompatActivity implements IFirebaseAuth {

    private FirebaseAuth firebaseAuth;
    private IMyProgressDialog iProgressDialog;

    private IOnFinishedListener onFinishedListener;
    private WeakReference<Context> activityReference;

    public FirebaseAuthApp(Context context, FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        activityReference = new WeakReference<>(context);
    }

    @Override
    public void setListener(final IOnFinishedListener onFinishedListener) {
        this.onFinishedListener = onFinishedListener;
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();

        getGoogleSignInClient().signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onFinishedListener.onFinished(null);
                    }
                });
    }

    @Override
    public void signIn(GoogleSignInAccount acct) {
        iProgressDialog = new MyProgressDialog(activityReference.get());
        iProgressDialog.showProgress();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onFinishedListener.onFinished(firebaseAuth.getCurrentUser());
                        } else {
                            onFinishedListener.onFailure(task.getException());
                            onFinishedListener.onFinished(null);
                        }
                        iProgressDialog.hideProgress();
                    }
                });
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activityReference.get().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(activityReference.get(), signInOptions);
    }
}