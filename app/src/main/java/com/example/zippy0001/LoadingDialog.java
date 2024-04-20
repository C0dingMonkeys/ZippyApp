package com.example.zippy0001;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog dialog;
    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }
    void iniciarAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void fecharAlertDialog(){
        dialog.dismiss();
    }
}
