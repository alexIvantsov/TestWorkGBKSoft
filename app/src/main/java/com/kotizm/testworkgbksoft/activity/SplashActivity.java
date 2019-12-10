package com.kotizm.testworkgbksoft.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.kotizm.testworkgbksoft.R;
import com.kotizm.testworkgbksoft.utils.activity.IMyStartActivity;
import com.kotizm.testworkgbksoft.utils.activity.MyStartActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        Class<?> aClass;
        if (FirebaseAuth.getInstance().getCurrentUser() == null) aClass = LoginActivity.class;
        else aClass = MainActivity.class;

        IMyStartActivity iStartActivity = new MyStartActivity(this);
        iStartActivity.startNewActivity(aClass);
    }
}