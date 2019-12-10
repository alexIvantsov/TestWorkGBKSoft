package com.kotizm.testworkgbksoft.utils.dialog.addpoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.kotizm.testworkgbksoft.R;

import java.lang.ref.WeakReference;

public class AddPointDialogReceiveData implements IAddPointDialogReceiveData {

    private WeakReference<Context> dialogReference;

    public AddPointDialogReceiveData(Context context) {
        dialogReference = new WeakReference<>(context);
    }

    @Override
    public void getPointName(final IOnFinishedListener onFinishedListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(dialogReference.get(), R.style.DialogAlert);
        alert.setTitle(R.string.alert_dialog_title);

        final EditText input = new EditText(dialogReference.get());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pointName = input.getText().toString();
                if (pointName.isEmpty()) pointName = "Santa Claus";
                onFinishedListener.onFinished(pointName);
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}