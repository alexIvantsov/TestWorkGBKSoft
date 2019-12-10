package com.kotizm.testworkgbksoft.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.firebase.auth.FirebaseAuthApp;
import com.kotizm.testworkgbksoft.firebase.auth.FirebaseAuthPresenter;
import com.kotizm.testworkgbksoft.firebase.auth.IFirebaseAuthPresenter;
import com.kotizm.testworkgbksoft.firebase.auth.IFirebaseAuthView;
import com.kotizm.testworkgbksoft.utils.activity.IMyStartActivity;
import com.kotizm.testworkgbksoft.utils.activity.MyStartActivity;

public class LoginActivity extends AppCompatActivity implements IFirebaseAuthView {

    private static final int RC_SIGN_IN = 2019;
    private IFirebaseAuthPresenter authPresenter;
    private static final String LOG_MESSAGE = "log_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authPresenter = new FirebaseAuthPresenter(LoginActivity.this,
                        new FirebaseAuthApp(LoginActivity.this, FirebaseAuth.getInstance()));
                authPresenter.setListener();

                Intent signInIntent = authPresenter.getGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onResponseFirebaseAuth(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.i(LOG_MESSAGE, "currentUser = " + currentUser.getUid());
            IMyStartActivity iStartActivity = new MyStartActivity(this);
            iStartActivity.startNewActivity(MainActivity.class);
        } else Log.i(LOG_MESSAGE, "currentUser = " + currentUser);
    }

    @Override
    public void onFailureFirebaseAuth(Throwable throwable) {
        Toast.makeText(this, getString(R.string.firebase_auth_error) + throwable, Toast.LENGTH_LONG).show();
        Log.e(LOG_MESSAGE, getString(R.string.firebase_auth_error) + throwable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (authPresenter != null) authPresenter.signIn(account);
            } catch (ApiException apiException) {
                Toast.makeText(this, getString(R.string.firebase_auth_error) + apiException, Toast.LENGTH_LONG).show();
                Log.e(LOG_MESSAGE, getString(R.string.firebase_auth_error) + apiException);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (authPresenter != null) authPresenter.onDestroy();
    }
}