package com.kotizm.testworkgbksoft.utils.dialog.addpoint;

public interface IAddPointDialogReceiveData {
    interface IOnFinishedListener {
        void onFinished(String pointName);
    }
    void getPointName(IOnFinishedListener onFinishedListener);
}