package com.kotizm.testworkgbksoft.utils.permission;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.kotizm.testworkgbksoft.R;

import java.util.Objects;

public abstract class Permission {
    public static void requestPermission(AppCompatActivity activity, int requestId, String permission, boolean finishActivity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Display a dialog with rationale.
            Permission.RationaleDialog.newInstance(requestId, finishActivity)
                    .show(activity.getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);
        }
    }

    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults, String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    /**
     * A dialog that displays a permission denied message.
     */
    public static class PermissionDeniedDialog extends DialogFragment {

        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
        private boolean mFinishActivity = false;

        /**
         * Creates a new instance of this dialog and optionally finishes the calling Activity
         * when the 'Ok' button is clicked.
         */
        public static PermissionDeniedDialog newInstance(boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);

            PermissionDeniedDialog dialog = new PermissionDeniedDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (getArguments() != null) {
                mFinishActivity = getArguments().getBoolean(ARGUMENT_FINISH_ACTIVITY);
            }

            return new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.DialogAlert)
                    .setMessage(getActivity().getString(R.string.location_permission_denied))
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }

        @Override
        public void onDismiss(@NonNull DialogInterface dialog) {
            super.onDismiss(dialog);
            if (mFinishActivity) {
                Toast.makeText(getActivity(), R.string.permission_required_toast,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class RationaleDialog extends DialogFragment {

        private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";

        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";

        private boolean mFinishActivity = false;

        static RationaleDialog newInstance(int requestCode, boolean finishActivity) {
            Bundle arguments = new Bundle();
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
            RationaleDialog dialog = new RationaleDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int requestCode = 0;
            if (arguments != null) {
                requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
                mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY);
            }

            final int finalRequestCode = requestCode;
            return new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.DialogAlert)
                    .setMessage(getActivity().getString(R.string.permission_rationale_location))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // After click on Ok, request the permission.
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    finalRequestCode);
                            // Do not finish the Activity while requesting permission.
                            mFinishActivity = false;
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        @Override
        public void onDismiss(@NonNull DialogInterface dialog) {
            super.onDismiss(dialog);
            if (mFinishActivity) {
                Toast.makeText(getActivity(),
                        R.string.permission_required_toast,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}