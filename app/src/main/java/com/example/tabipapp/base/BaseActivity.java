package com.example.tabipapp.base;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialogx.dialogs.WaitDialog;

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;
    protected Toast toast;


    /**
     * 显示等待加载框
     *
     * @param msg 显示消息
     */
    protected void showProgressDialog(@NonNull String msg, boolean cancelable) {
        if (TextUtils.isEmpty(msg) ) {
            return;
        }
        WaitDialog.getInstance().setCancelable(cancelable);
        WaitDialog.show(msg);

    }

    /**
     * 关闭等待加载框
     */
    protected void disProgressDialog() {
        WaitDialog.dismiss();
    }

    protected void showToast(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null){
                    toast.cancel();
                }
                toast = Toast.makeText(BaseActivity.this,msg,Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

}
