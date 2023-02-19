package com.example.tabipapp.UI.MinttiVision;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tabipapp.R;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.bean.MeasureType;
import com.mintti.visionsdk.ble.callback.IBleConnectionListener;
import com.mintti.visionsdk.ble.callback.IBleWriteResponse;
import com.mintti.visionsdk.ble.callback.IBpResultListener;
import com.mintti.visionsdk.ble.callback.IBtResultListener;
import com.mintti.visionsdk.ble.callback.IRawBpDataCallback;

public class AllMeasureActivity extends AppCompatActivity implements IBtResultListener, IBpResultListener, IBleWriteResponse,
        IRawBpDataCallback, IBleConnectionListener, Handler.Callback {
    private static final int MSG_BP_RESULT = 1;
    private static final int MSG_BP_LEAK = 2;
    private static final int MSG_BP_ERROR = 3;
    private static final int MSG_BP_PRESSURE = 4;
    private static final String SYSTOLIC = "systolic";
    private static final String DIASTOLIC = "diastolic";
    private static final String HR = "hr";
    private final Handler handler = new Handler(Looper.getMainLooper(), AllMeasureActivity.this);
    Button bt_measure_bt;
    Button bt_button, bp_button, bg_button, ecg_button, spo2_button;
    TextView tv_bt, tv_systolic_value, tv_hr_value, tv_diastolic_value, tv_pressure;
    Boolean bt_button_enabled = false, bp_button_enabled = false, bg_button_enabled = false, ecg_button_enabled = false, spo2_button_enabled = false;
    LinearLayout bt_layout;
    ConstraintLayout bp_layout;
    private final boolean isDfu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bt);

        bt_measure_bt = (Button) findViewById(R.id.bt_measure_bt);
        bt_button = (Button) findViewById(R.id.bt_measure_bt);
        bp_button = (Button) findViewById(R.id.bp_button);
        bg_button = (Button) findViewById(R.id.bg_button);
        spo2_button = (Button) findViewById(R.id.spo2_button);
        ecg_button = (Button) findViewById(R.id.ecg_button);
        bt_layout = (LinearLayout) findViewById(R.id.bt_layout);
        bp_layout = (ConstraintLayout) findViewById(R.id.bp_layout);
        tv_systolic_value = (TextView) findViewById(R.id.tv_systolic_value);
        tv_hr_value = (TextView) findViewById(R.id.tv_hr_value);
        tv_diastolic_value = (TextView) findViewById(R.id.tv_diastolic_value);
        tv_pressure = (TextView) findViewById(R.id.tv_pressure);

        tv_bt = (TextView) findViewById(R.id.tv_bt);


        bt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleManager.getInstance().setBtResultListener(AllMeasureActivity.this);
                bt_button_enabled = true;
                bt_layout.setVisibility(View.VISIBLE);
                bp_layout.setVisibility(View.GONE);
            }
        });

        bp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleManager.getInstance().setBpResultListener(AllMeasureActivity.this);
                BleManager.getInstance().setRawBpDataCallback(AllMeasureActivity.this);
                bp_button_enabled = true;
                bt_layout.setVisibility(View.GONE);
                bp_layout.setVisibility(View.VISIBLE);
            }
        });

        bt_measure_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_button_enabled) {
                    if (bt_measure_bt.getText().equals(getString(R.string.start_measure))) {
                        BleManager.getInstance().startMeasure(MeasureType.TYPE_BT, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.stop_measure);
                    } else {
                        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BT, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.start_measure);

                    }

                } else if (bp_button_enabled) {

                    if (bt_measure_bt.getText().equals(getString(R.string.start_measure))) {

                        //开始测量
                        resetValue();
                        BleManager.getInstance().startMeasure(MeasureType.TYPE_BP, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.stop_measure);


                    } else {
                        //停止测量
                        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BP, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.start_measure);

                    }
                }


            }
        });
    }

    @Override
    public void onWriteSuccess() {
        Log.e("BTFragment", "onWriteSuccess: BT");
    }

    @Override
    public void onWriteFailed() {
        Log.e("BTFragment", "onWriteFailed: BT");

    }

    @Override
    public void onBtResult(double temperature) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_bt.setText(temperature + " ℃");
                bt_measure_bt.setText(R.string.start_measure);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().removeConnectionListener(AllMeasureActivity.this);

    }

    @Override
    public void onConnectSuccess(String mac) {

    }

    @Override
    public void onConnectFailed(String mac) {

    }

    @Override
    public void onDisconnected(String mac, boolean isActiveDisconnect) {
        Log.e("MeasureActivity", "onDisconnected: " + mac + "  " + isActiveDisconnect);
        if (!isActiveDisconnect && !isDfu) {
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
    public void onBpResult(int systolic, int diastolic, int heartRate) {
        Message msg = handler.obtainMessage(MSG_BP_RESULT);
        Bundle bundle = new Bundle();
        bundle.putInt(SYSTOLIC, systolic);
        bundle.putInt(DIASTOLIC, diastolic);
        bundle.putInt(HR, heartRate);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void onLeadError() {
        Log.e("BpFragment", "onLeadError: ");
        handler.sendEmptyMessage(MSG_BP_LEAK);
    }

    @Override
    public void onBpError() {
        Log.e("BpFragment", "onBpError: ");
        handler.sendEmptyMessage(MSG_BP_ERROR);
    }

    @Override
    public void onPressurizationData(short i) {

    }

    @Override
    public void onDecompressionData(short i) {

    }

    @Override
    public void onPressure(short i) {
        Message msg = handler.obtainMessage(MSG_BP_PRESSURE);
        msg.arg1 = i;
        handler.sendMessage(msg);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {

        if (msg.what == MSG_BP_RESULT) {
            Bundle bundle = msg.getData();
            int hr = bundle.getInt(HR);
            int systolic = bundle.getInt(SYSTOLIC);
            int diastolic = bundle.getInt(DIASTOLIC);
            tv_systolic_value.setText(systolic + " / mmHg");
            tv_diastolic_value.setText(diastolic + " / mmHg");
            tv_hr_value.setText(hr + " / BPM");
            tv_pressure.setText("0");
            bt_measure_bt.setText(R.string.start_measure);

            return true;
        } else if (msg.what == MSG_BP_LEAK) {
            MessageDialog.show(R.string.hint, R.string.bp_air_leak, R.string.confirm);
            bt_measure_bt.setText(R.string.start_measure);
            return true;
        } else if (msg.what == MSG_BP_ERROR) {
            MessageDialog.show(R.string.hint, R.string.bp_measure_error, R.string.confirm);
            bt_measure_bt.setText(R.string.start_measure);
        } else if (msg.what == MSG_BP_PRESSURE) {
            tv_pressure.setText(msg.arg1 + "");
        }
        return false;

    }

    private void resetValue() {
        tv_systolic_value.setText("-- / mmHg");
        tv_diastolic_value.setText("-- / mmHg");
        tv_hr_value.setText("-- / BPM");
        tv_pressure.setText("0");

    }


}
