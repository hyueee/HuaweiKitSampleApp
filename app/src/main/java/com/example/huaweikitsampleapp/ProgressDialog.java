package com.example.huaweikitsampleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

public class ProgressDialog {

    Activity activity;
    AlertDialog loadingDialog;

    ProgressDialog(Activity myActivity){
        activity = myActivity;

    }

    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        loadingDialog = builder.create();
        loadingDialog.show();

    }

    void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }
}