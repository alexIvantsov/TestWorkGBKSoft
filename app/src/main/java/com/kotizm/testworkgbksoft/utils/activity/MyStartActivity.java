package com.kotizm.testworkgbksoft.utils.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MyStartActivity implements IMyStartActivity {

    private AppCompatActivity activity;

    public MyStartActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void startNewActivity(Class<?> aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}