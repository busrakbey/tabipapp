package com.example.tabipapp.UI.MinttiVision;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.tabipapp.Adapter.BleDeviceAdapter;
import com.example.tabipapp.R;
import com.example.tabipapp.base.App;
import com.example.tabipapp.base.BaseVBindingActivity;
import com.example.tabipapp.databinding.BluetoothActivityBinding;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.mintti.visionsdk.ble.BleDevice;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.callback.IBleConnectionListener;
import com.mintti.visionsdk.ble.callback.IBleInitCallback;
import com.mintti.visionsdk.ble.callback.IBleScanCallback;

import java.util.ArrayList;
import java.util.List;

public  class BluetoothActivity extends  BaseVBindingActivity<BluetoothActivityBinding> implements IBleConnectionListener, Handler.Callback {
    private static final int MSG_SCAN_DELAY = 0x01;

    private List<BleDevice> deviceList = new ArrayList<>();
    private BleDeviceAdapter deviceAdapter;
    private BleDevice mBleDevice;
    private ActionBar actionBar;
    private Handler mHandler;
    private boolean isScanning = false;
    public String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    public List<String> permissionList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
        checkPermission();
        mHandler = new Handler(getMainLooper(), this);
        initView();
    }

    @Override
    public BluetoothActivityBinding getViewBinding() {
        return BluetoothActivityBinding.inflate(getLayoutInflater());
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions = new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT};
        }
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(BluetoothActivity.this, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }

            if (!permissionList.isEmpty()) {
                String[] requestPermission = permissionList.toArray(new String[permissionList
                        .size()]);
                ActivityCompat.requestPermissions(BluetoothActivity.this, requestPermission, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(BluetoothActivity.this, permissions[i], Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        }
    }

    private void initView() {

        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getBinding().recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        deviceAdapter = new BleDeviceAdapter(deviceList);
        getBinding().recyclerView.setAdapter(deviceAdapter);
        deviceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BleManager.getInstance().stopScan();
                getBinding().swipeRefreshLayout.setRefreshing(false);
                showProgressDialog(getString(R.string.ble_connecting), false);
                BluetoothActivity.this.mBleDevice = deviceList.get(position);
                BleManager.getInstance().connect(mBleDevice);
            }
        });
        getBinding().btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BleManager.getInstance().isBluetoothEnable()) {
                    if (getBinding().btScan.getText().equals(getString(R.string.scan))) {
                        deviceList.clear();
                        deviceAdapter.notifyDataSetChanged();
                        getBinding().swipeRefreshLayout.setRefreshing(true);
                        startScan();
                        getBinding().btScan.setText(R.string.stop_scan);
                    } else {
                        BleManager.getInstance().stopScan();
                        isScanning = false;
                        getBinding().swipeRefreshLayout.setRefreshing(false);
                        getBinding().btScan.setText(R.string.scan);
                        mHandler.removeMessages(MSG_SCAN_DELAY);
                    }
                } else {
                    BleManager.getInstance().openBluetooth(BluetoothActivity.this, 2);
                }


            }
        });

        getBinding().swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("BluetoothActivity", "onRefresh: ");
                if (!isScanning) {
                    startScan();
                    getBinding().btScan.setText(R.string.stop_scan);
                }

                //swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void startScan() {
        mHandler.sendEmptyMessageDelayed(MSG_SCAN_DELAY, 10 * 1000);
        isScanning = true;
        BleManager.getInstance().startScan(new IBleScanCallback() {
            @Override
            public void onScanResult(BleDevice bleDevice) {

                if (TextUtils.isEmpty(bleDevice.getName())
                        || TextUtils.isEmpty(bleDevice.getMac())
                        || (!bleDevice.getName().equals("Mintti-Vision") && !bleDevice.getName().equals("I4Clinic") && !bleDevice.getName().contains("HC"))) {
                    return;
                }
                boolean hasDevice = false;
                for (BleDevice device : deviceList) {
                    if (device.getMac().equals(bleDevice.getMac())) {
                        hasDevice = true;
                        device.setRssi(bleDevice.getRssi());
                    }
                }
                if (!hasDevice) {
                    deviceList.add(bleDevice);
                }
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFailed(int errorCode) {
                isScanning = false;
                mHandler.removeMessages(MSG_SCAN_DELAY);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        BleManager.getInstance().addConnectionListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BleManager.getInstance().removeConnectionListener(this);
    }

    @Override
    public void onConnectSuccess(String mac) {

        TipDialog.show(R.string.ble_connect_success, TipDialog.TYPE.SUCCESS)
                .setDialogLifecycleCallback(new DialogLifecycleCallback<WaitDialog>() {
                    @Override
                    public void onDismiss(WaitDialog dialog) {
                        super.onDismiss(dialog);
                        startActivity(new Intent(BluetoothActivity.this,MeasureActivity.class));
                    }
                });

    }

    @Override
    public void onConnectFailed(String mac) {
        TipDialog.show(R.string.ble_connect_failed, WaitDialog.TYPE.ERROR);
    }

    @Override
    public void onDisconnected(String mac, boolean isActiveDisconnect) {

    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == MSG_SCAN_DELAY){
            getBinding().swipeRefreshLayout.setRefreshing(false);
            BleManager.getInstance().stopScan();
            isScanning = false;
            getBinding().btScan.setText("Scan");
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK){
            BleManager.getInstance().init(App.getInstance(), new IBleInitCallback() {
                @Override
                public void onInitSuccess() {

                }

                @Override
                public void onInitFailed() {

                }
            });
            getBinding().btScan.performClick();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().release();
    }
}
