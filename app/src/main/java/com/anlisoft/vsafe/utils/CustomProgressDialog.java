package com.anlisoft.vsafe.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomProgressDialog {

    private ProgressDialog mProgressDialog;

    public CustomProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
        }
    }

    public void showDialogWithMsg(CharSequence charSequence){

        mProgressDialog.setMessage(charSequence);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void dismissProgressDialog(){
        mProgressDialog.dismiss();
    }

}
