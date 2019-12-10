package com.kotizm.testworkgbksoft.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.activity.LoginActivity;
import com.kotizm.testworkgbksoft.firebase.auth.FirebaseAuthApp;
import com.kotizm.testworkgbksoft.firebase.auth.FirebaseAuthPresenter;
import com.kotizm.testworkgbksoft.firebase.auth.IFirebaseAuthPresenter;
import com.kotizm.testworkgbksoft.firebase.auth.IFirebaseAuthView;
import com.kotizm.testworkgbksoft.utils.activity.IMyStartActivity;
import com.kotizm.testworkgbksoft.utils.activity.MyStartActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment implements IFirebaseAuthView {

    private AppCompatActivity activity;
    private IMyStartActivity iStartActivity;

    private IFirebaseAuthPresenter authPresenter;
    private static final String LOG_MESSAGE = "log_message";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        activity = (AppCompatActivity) Objects.requireNonNull(getActivity());

        Button outButton = root.findViewById(R.id.sign_out_button);
        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authPresenter = new FirebaseAuthPresenter(ProfileFragment.this,
                        new FirebaseAuthApp(activity, FirebaseAuth.getInstance()));
                authPresenter.setListener();
                authPresenter.signOut();
            }
        });
        return root;
    }

    @Override
    public void onResponseFirebaseAuth(FirebaseUser currentUser) {
        if (currentUser == null) {
            iStartActivity = new MyStartActivity(activity);
            iStartActivity.startNewActivity(LoginActivity.class);
        }
    }

    @Override
    public void onFailureFirebaseAuth(Throwable throwable) {
        Log.e(LOG_MESSAGE, getString(R.string.firebase_auth_error) + throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (authPresenter != null) authPresenter.onDestroy();
        if (iStartActivity != null) iStartActivity = null;
    }
}