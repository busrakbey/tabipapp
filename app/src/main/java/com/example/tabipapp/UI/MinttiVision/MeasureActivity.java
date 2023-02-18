package com.example.tabipapp.UI.MinttiVision;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.example.tabipapp.Adapter.MeasureFragmentAdapter;
import com.example.tabipapp.R;
import com.example.tabipapp.base.BaseVBindingActivity;
import com.example.tabipapp.ble.dfu.DfuService;
import com.example.tabipapp.databinding.ActivityMeasureBinding;
import com.google.android.material.tabs.TabLayout;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.mintti.visionsdk.ble.DeviceType;
import com.mintti.visionsdk.ble.callback.IDeviceVersionCallback;
import com.mintti.visionsdk.common.LogUtils;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.callback.IBleConnectionListener;



import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

public class MeasureActivity extends BaseVBindingActivity<ActivityMeasureBinding> implements IBleConnectionListener, DfuProgressListener {
    private File zipDir;
    private ProgressDialog progressDialog;
    private boolean isDfu = false;
    private String firmWareVersion = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
        BleManager.getInstance().getDeviceVersionInfo(new IDeviceVersionCallback() {
            @Override
            public void onDeviceVersionInfo(String s) {
                firmWareVersion = s;
            }
        });
        initView();
    }

    @Override
    public ActivityMeasureBinding getViewBinding() {
        return ActivityMeasureBinding.inflate(getLayoutInflater());
    }

    private void initView() {
        isDfu = false;
        MeasureFragmentAdapter fragmentAdapter = new MeasureFragmentAdapter(getSupportFragmentManager(), this);
        getBinding().viewPager.setAdapter(fragmentAdapter);
        getBinding().tabLayout.setupWithViewPager(getBinding().viewPager);
        getBinding().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //初始化progressdialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.firmware_upgrading));
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMax(100);

    }

    @Override
    protected void onResume() {
        super.onResume();
        BleManager.getInstance().addConnectionListener(this);
        DfuServiceListenerHelper.registerProgressListener(this,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().removeConnectionListener(this);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        if (BleManager.getInstance().isConnected()) {

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.whether_disconnect)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BleManager.getInstance().disconnect();
                            dialog.cancel();
                            finish();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            alertDialog.show();


        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnectSuccess(String mac) {

    }

    @Override
    public void onConnectFailed(String mac) {

    }

    @Override
    public void onDisconnected(String mac, boolean isActiveDisconnect) {
        Log.e("MeasureActivity", "onDisconnected: "+mac + "  "+isActiveDisconnect);
        if (!isActiveDisconnect && !isDfu){
            TipDialog.show(R.string.ble_disconnected, TipDialog.TYPE.WARNING)
                    .setCancelable(false)
                    .setDialogLifecycleCallback(new DialogLifecycleCallback<WaitDialog>() {
                        @Override
                        public void onDismiss(WaitDialog dialog) {
                            super.onDismiss(dialog);
                            finish();
                        }
                    });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BleManager.getInstance().isConnected() && BleManager.getInstance().getDeviceType() == DeviceType.TYPE_MINTTI_VISION) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_measure,menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_upgrade){

            Toast.makeText(this, R.string.firmware_upgrade, Toast.LENGTH_LONG).show();
            copyZip();
            File[] files = zipDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(firmWareVersion.substring(0,firmWareVersion.indexOf(".")));
                }
            });
            if ( files != null && files.length > 0){
                showFirmwareListDialog(files);
            }else {
                Toast.makeText(this, R.string.no_firmware, Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * 开启固件升级服务
     */
    @SuppressLint("MissingPermission")
    private void startUpdateServer(String path) {
        isDfu = true;
        final DfuServiceInitiator starter = new DfuServiceInitiator(BleManager.getInstance().getBluetoothDevice().getAddress());
        starter.setDeviceName(BleManager.getInstance().getBluetoothDevice().getName())
                .setDisableNotification(true)
                .setKeepBond(true)
                .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
        starter.setForeground(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            starter.createDfuNotificationChannel(this);
        }
        starter.setZip(path);
        starter.start(MeasureActivity.this, DfuService.class);
        LogUtils.d("MeasureActivity", "startUpdateServer: ");
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setMessage("Start update progress");
        progressDialog.show();
    }

    /**
     * 将固件包复制到zip文件目录下
     */
    private void copyZip(){
        zipDir = new File(getExternalFilesDir(""),"zip");
        if ( !zipDir.exists() ){
            zipDir.mkdirs();
        }
        try {
            AssetManager assetManager = getAssets();
            String[] firmwareFiles = assetManager.list("zip");
            for (String firmwareFile : firmwareFiles) {
                File file = new File(zipDir.getAbsolutePath()+"/"+firmwareFile);
                if (file.exists()){
                    continue;
                }
                InputStream is = assetManager.open("zip/"+firmwareFile);
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void showFirmwareListDialog(File[] files){
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_firmware);
        builder.setItems(fileNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = files[which];
                startUpdateServer(file.getAbsolutePath());
            }
        }).create().show();


    }



    @Override
    public void onDeviceConnecting(String deviceAddress) {

    }

    @Override
    public void onDeviceConnected(String deviceAddress) {

    }

    @Override
    public void onDfuProcessStarting(String deviceAddress) {

    }

    @Override
    public void onDfuProcessStarted(String deviceAddress) {
        progressDialog.setMessage("Start update progress");
    }

    @Override
    public void onEnablingDfuMode(String deviceAddress) {

    }

    @Override
    public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
        progressDialog.setProgress(percent);
    }

    @Override
    public void onFirmwareValidating(String deviceAddress) {

    }

    @Override
    public void onDeviceDisconnecting(String deviceAddress) {

    }

    @Override
    public void onDeviceDisconnected(String deviceAddress) {

    }

    @Override
    public void onDfuCompleted(String deviceAddress) {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Upgrade successful" + deviceAddress, Toast.LENGTH_LONG).show();
        new Handler().postDelayed(() -> {
            // if this activity is still open and upload process was completed, cancel the notification
            final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(DfuService.NOTIFICATION_ID);
        }, 200);
        showToast("Update success,please restart device");
        finish();
    }

    @Override
    public void onDfuAborted(String deviceAddress) {

    }

    @Override
    public void onError(String deviceAddress, int error, int errorType, String message) {
        showToast("upgrade failed");
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
        finish();
    }
}

