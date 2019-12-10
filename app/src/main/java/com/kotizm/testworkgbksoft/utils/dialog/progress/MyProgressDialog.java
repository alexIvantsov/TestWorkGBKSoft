package com.kotizm.testworkgbksoft.utils.dialog.progress;

import android.app.ProgressDialog;
import android.content.Context;

import com.kotizm.testworkgbksoft.R;

public class MyProgressDialog implements IMyProgressDialog{

    private Context context;
    private ProgressDialog mProgressDialog;

    public MyProgressDialog(Context context) {
        this.context = context;
    }

    @Override
    public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}