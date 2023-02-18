package com.example.tabipapp.base;

import android.app.Application;
import android.util.Log;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.style.IOSStyle;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.callback.IBleInitCallback;
import com.mintti.visionsdk.common.LogUtils;

public class App extends Application {
    public static final String TAG = "App";
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        BleManager.getInstance().init(this, new IBleInitCallback() {
            @Override
            public void onInitSuccess() {
                Log.e(TAG, "onInitSuccess: " );
            }

            @Override
            public void onInitFailed() {
                Log.e(TAG, "onInitFailed: " );
            }
        });
        app = this;
        initDialogX();
    }

    public static App getInstance(){
        return app;
    }



    private void initDialogX() {

        //初始化
        DialogX.init(this);

        //开启调试模式，在部分情况下会使用 Log 输出日志信息
        DialogX.DEBUGMODE = true;

        //设置主题样式
        DialogX.globalStyle = new IOSStyle();

        //设置亮色/暗色（在启动下一个对话框时生效）
        DialogX.globalTheme = DialogX.THEME.AUTO;

//        //设置对话框最大宽度（单位为像素）
//        DialogX.dialogMaxWidth = 1920;
//
//        //设置 InputDialog 自动弹出键盘
//        DialogX.autoShowInputKeyboard = true;
//
        //限制 PopTip 一次只显示一个实例（关闭后可以同时弹出多个 PopTip）
        DialogX.onlyOnePopTip = true;
//
//
//        //设置默认对话框背景颜色（值为ColorInt，为-1不生效）
//        DialogX.backgroundColor = Color.WHITE;
//
//        //设置默认对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
//        DialogX.cancelable = true;

        //设置默认提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
        DialogX.cancelableTipDialog = false;








    }

}
