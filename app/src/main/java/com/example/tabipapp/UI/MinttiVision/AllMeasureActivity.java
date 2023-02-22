package com.example.tabipapp.UI.MinttiVision;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.example.tabipapp.R;
import com.example.tabipapp.UI.MinttiVision.Fragment.ecg.ChartView;
import com.example.tabipapp.base.utils.TimeUtils;
import com.kl.vision_ecg.ISmctAlgoCallback;
import com.kl.vision_ecg.SmctConstant;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.bean.MeasureType;
import com.mintti.visionsdk.ble.callback.IBgResultListener;
import com.mintti.visionsdk.ble.callback.IBleConnectionListener;
import com.mintti.visionsdk.ble.callback.IBleWriteResponse;
import com.mintti.visionsdk.ble.callback.IBpResultListener;
import com.mintti.visionsdk.ble.callback.IBtResultListener;
import com.mintti.visionsdk.ble.callback.IEcgResultListener;
import com.mintti.visionsdk.ble.callback.IRawBpDataCallback;
import com.mintti.visionsdk.ble.callback.ISpo2ResultListener;

public class AllMeasureActivity extends AppCompatActivity implements IBtResultListener,
        IBpResultListener, IBleWriteResponse, IRawBpDataCallback, IBleConnectionListener,
        ISpo2ResultListener, IEcgResultListener, ISmctAlgoCallback, Handler.Callback {
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
    TextView tv_bt, tv_systolic_value, tv_hr_value, tv_diastolic_value, tv_pressure, tvSpo2, tvHr;
    Boolean bt_button_enabled = false, bp_button_enabled = false, bg_button_enabled = false, ecg_button_enabled = false, spo2_button_enabled = false;
    LinearLayout bt_layout;
    ConstraintLayout bp_layout, spo2_layout, ecg_layout, bg_layout;
    WaveSurfaceView bo_wave_view;
    private final boolean isDfu = false;
    private static final int MSG_SPO = 0;
    private static final int MSG_HR = 1;
    private PPGDrawWave oxWave;
    private boolean isMeasureEnd;

    private static final int MSG_ADJUST_FAILED = 2;
    private static final int MSG_WAIT_INSERT = 3;
    private static final int MSG_WAIT_DRIP = 4;
    private static final int MSG_DRIP_BLOOD = 5;
    private static final int MSG_BG_MEASURE_OVER = 6;
    private static final int MSG_PAPER_USED = 7;
    private static final int MSG_GET_RESULT_TIMEOUT = 8;
    private static final String BUNDLE_BG_RESULT = "bg_result";
    protected String[] mTestPaperCodes;
    protected String mManufacturer;
    Guideline guide_line;
    ChartView ecg_view;
    TextView tv_ecg_duration, tv_rr_max_key, tv_rr_max_value, tv_rr_min_key, tv_rr_min_value, tv_avg_hr_key, tv_avg_hr_value, tv_hrv_key, tv_hrv_value, tv_resp_key, tv_resp_value;
    AppCompatSpinner gain_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bt);

        initItems();
        bt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleManager.getInstance().setBtResultListener(AllMeasureActivity.this);
                stopAllMeasure();
                bt_measure_bt.setVisibility(View.VISIBLE);
                bt_button_enabled = true;
                bp_button_enabled = false;
                ecg_button_enabled = false;
                spo2_button_enabled=false;
                bt_layout.setVisibility(View.VISIBLE);
                bp_layout.setVisibility(View.GONE);
                spo2_layout.setVisibility(View.GONE);
                ecg_layout.setVisibility(View.GONE);
            }
        });

        bp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleManager.getInstance().setBpResultListener(AllMeasureActivity.this);
                BleManager.getInstance().setRawBpDataCallback(AllMeasureActivity.this);

                stopAllMeasure();
                bt_measure_bt.setVisibility(View.VISIBLE);
                bt_button_enabled = false;
                bp_button_enabled = true;
                ecg_button_enabled = false;
                spo2_button_enabled=false;
                bt_layout.setVisibility(View.GONE);
                bp_layout.setVisibility(View.VISIBLE);
                spo2_layout.setVisibility(View.GONE);
                ecg_layout.setVisibility(View.GONE);
            }
        });

        spo2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleManager.getInstance().setSpo2ResultListener(AllMeasureActivity.this);
                oxWave = new PPGDrawWave();
                bo_wave_view.setDrawWave(oxWave);
                stopAllMeasure();
                bt_measure_bt.setVisibility(View.VISIBLE);
                bt_button_enabled = false;
                bp_button_enabled = false;
                spo2_button_enabled=true;
                ecg_button_enabled = false;
                bt_layout.setVisibility(View.GONE);
                bp_layout.setVisibility(View.GONE);
                spo2_layout.setVisibility(View.VISIBLE);
                ecg_layout.setVisibility(View.GONE);
            }
        });

        ecg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAllMeasure();
                bt_measure_bt.setVisibility(View.VISIBLE);
                bt_button_enabled = false;
                bp_button_enabled = false;
                spo2_button_enabled=false;
                ecg_button_enabled = true;
                bt_layout.setVisibility(View.GONE);
                bp_layout.setVisibility(View.GONE);
                spo2_layout.setVisibility(View.GONE);
                ecg_layout.setVisibility(View.VISIBLE);

                BleManager.getInstance().setEcgResultListener(AllMeasureActivity.this);
                gain_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String[] gainArray = getResources().getStringArray(R.array.ecg_gain);
                        if (position == 0) {
                            ecg_view.setGain(1);
                        } else if (position == 1) {
                            ecg_view.setGain(2);
                        } else {
                            ecg_view.setGain(5);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                float gain = ecg_view.getGain();
                if (gain == 1f) {
                    gain_spinner.setSelection(0);
                } else if (gain == 2f) {
                    gain_spinner.setSelection(1);
                } else {
                    gain_spinner.setSelection(2);
                }
                ecg_view.setSampleRate(BleManager.getInstance().getSampleRate());
            }
        });

        bt_measure_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_button_enabled == true) {
                    if (bt_measure_bt.getText().equals(getString(R.string.start_measure))) {
                        BleManager.getInstance().startMeasure(MeasureType.TYPE_BT, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.stop_measure);
                    } else {
                        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BT, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.start_measure);
                    }
                } else if (bp_button_enabled == true) {

                    if (bt_measure_bt.getText().equals(getString(R.string.start_measure))) {
                        resetValue();
                        BleManager.getInstance().startMeasure(MeasureType.TYPE_BP, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.stop_measure);
                    } else {
                        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BP, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.start_measure);

                    }
                } else if (spo2_button_enabled == true) {
                    if (bt_measure_bt.getText().equals(getString(R.string.start_measure))) {
                        resetSpo2();
                        BleManager.getInstance().startMeasure(MeasureType.TYPE_SPO2, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.stop_measure);
                    } else {
                        isMeasureEnd = true;
                        BleManager.getInstance().stopMeasure(MeasureType.TYPE_SPO2, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.start_measure);
//                    waveView.pause();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                oxWave.clear();
                            }
                        }, 500);


                    }
                } else if (ecg_button_enabled == true) {
                    if (bt_measure_bt.getText().equals(getString(R.string.start_measure))) {
                        resetResultEcg();
                        BleManager.getInstance().startMeasure(MeasureType.TYPE_ECG, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.stop_measure);
                    } else {
                        BleManager.getInstance().stopMeasure(MeasureType.TYPE_ECG, AllMeasureActivity.this);
                        bt_measure_bt.setText(R.string.start_measure);
                        ecg_view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ecg_view.clearDatas();
                            }
                        }, 500);

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
        oxWave.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        oxWave.clear();

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
            TipDialog.show(R.string.ble_disconnected, TipDialog.TYPE.WARNING).setCancelable(false).setDialogLifecycleCallback(new DialogLifecycleCallback<WaitDialog>() {
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

        if (bp_button_enabled == true) {
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
        } else if (spo2_button_enabled == true) {
            switch (msg.what) {
                case MSG_SPO:
                    Bundle bundle = msg.getData();
                    double spo = bundle.getDouble("spo");

                    if (spo > 90) {
                        tvSpo2.setText(spo + " %");
                    }

                    int heartRate = bundle.getInt("hr");
                    if (heartRate > 30 && heartRate < 200) {
                        tvHr.setText(heartRate + " bpm");
                    }


                    return true;
                case MSG_HR:
                    return true;
                default:
                    return false;
            }
        }
        return false;

    }

    private void resetValue() {
        tv_systolic_value.setText("-- / mmHg");
        tv_diastolic_value.setText("-- / mmHg");
        tv_hr_value.setText("-- / BPM");
        tv_pressure.setText("0");

    }


    @Override
    public void onWaveData(int waveData) {
        if (!isMeasureEnd) {
            oxWave.addData(waveData);
        }
    }

    @Override
    public void onBoResultData(int heartRate, double spo2) {
        Message message = handler.obtainMessage();
        message.what = MSG_SPO;
        Bundle bundle = new Bundle();
        bundle.putDouble("spo", spo2);
        bundle.putInt("hr", heartRate);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void onSpo2End() {
        handler.postDelayed(() -> {
            //停止测量
            isMeasureEnd = true;
            BleManager.getInstance().stopMeasure(MeasureType.TYPE_SPO2, AllMeasureActivity.this);
            bt_measure_bt.setText(R.string.start_measure);
            oxWave.clear();
        }, 500);
    }

    private void resetSpo2() {
        tvSpo2.setText("-- %");
        tvHr.setText("000 bpm");
        isMeasureEnd = false;
        bo_wave_view.reply();
        oxWave.clear();
    }


    @Override
    public void algoData(int key, int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                switch (key) {
                    case SmctConstant.KEY_ALGO_HEART_RATE:// 心率
//                        Log.e("ECGFragment", " 心率 = "+value );
                        tv_avg_hr_value.setText(value + " bpm");
                        break;
                    case SmctConstant.KEY_ALGO_ARRHYTHMIA:// 心律失常
                        Log.e("ECGFragment", " 心律失常 = " + value);
                        break;
                    case SmctConstant.KEY_ALGO_RESP_RATE:
//                        Log.e("ECGFragment", " 呼吸率 = "+value );
                        tv_resp_value.setText(value + "bpm");
                        break;

                }
            }
        });
    }

    @Override
    public void algoData(int i, int i1, int i2) {

        switch (i) {
            case SmctConstant.KEY_ALGO_RR_INTERVAL:
//                Log.e("ECGFragment", "KEY_ALGO_RR_INTERVAL  getIntAndDurDelay = "+i1 +" getInsRR() = " +i2);
                break;
        }


    }

    private void resetResult() {
        tv_rr_max_value.setText("-- ms");
        tv_rr_min_value.setText("-- ms");
        tv_hrv_value.setText("-- ms");
        tv_avg_hr_value.setText("-- ms");
        tv_resp_value.setText("-- ms");
        tv_ecg_duration.setText("00:00");
    }

    @Override
    public void onDrawWave(int wave) {
        ecg_view.addPoint(wave);
    }

    @Override
    public void onHeartRate(int heartRate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_avg_hr_value.setText(heartRate + " BPM");
            }
        });
    }

    @Override
    public void onRespiratoryRate(int respiratoryRate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_resp_value.setText(respiratoryRate + " BPM");
            }
        });
    }

    @Override
    public void onEcgResult(int rrMax, int rrMin, int hrv) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_rr_max_value.setText(rrMax + " ms");
                tv_rr_min_value.setText(rrMin + " ms");
                tv_hr_value.setText(hrv + " ms");
            }
        });
    }

    @Override
    public void onEcgDuration(int duration, boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_ecg_duration.setText(TimeUtils.formatDuration(duration));
            }
        });
    }

    private void resetResultEcg(){
        tv_rr_max_value.setText(  "-- ms");
        tv_rr_min_value.setText( "-- ms");
        tv_hrv_value.setText("-- ms");
        tv_avg_hr_value.setText("-- fms");
        tv_resp_value.setText("-- ms");
        tv_ecg_duration.setText("00:00");
    }

    void initItems(){
        oxWave = new PPGDrawWave();
        bt_measure_bt = (Button) findViewById(R.id.bt_measure_bt);
        bt_button = (Button) findViewById(R.id.bt_button);
        bp_button = (Button) findViewById(R.id.bp_button);
        bg_button = (Button) findViewById(R.id.bg_button);
        spo2_button = (Button) findViewById(R.id.spo2_button);
        ecg_button = (Button) findViewById(R.id.ecg_button);
        bt_layout = (LinearLayout) findViewById(R.id.bt_layout);
        ecg_layout = (ConstraintLayout) findViewById(R.id.ecg_layout);
        bp_layout = (ConstraintLayout) findViewById(R.id.bp_layout);
        spo2_layout = (ConstraintLayout) findViewById(R.id.spo2_layout);
        bg_layout = (ConstraintLayout) findViewById(R.id.bg_layout);
        tv_systolic_value = (TextView) findViewById(R.id.tv_systolic_value);
        tv_hr_value = (TextView) findViewById(R.id.tv_hr_value);
        tv_diastolic_value = (TextView) findViewById(R.id.tv_diastolic_value);
        tv_pressure = (TextView) findViewById(R.id.tv_pressure);
        bo_wave_view = (WaveSurfaceView) findViewById(R.id.bo_wave_view);
        tvSpo2 = (TextView) findViewById(R.id.tvSpo2);
        tvHr = (TextView) findViewById(R.id.tvHr);
        tv_rr_max_key = (TextView) findViewById(R.id.tv_rr_max_key);
        tv_rr_max_value = (TextView) findViewById(R.id.tv_rr_max_value);
        tv_rr_min_key = (TextView) findViewById(R.id.tv_rr_min_key);
        tv_rr_min_value = (TextView) findViewById(R.id.tv_rr_min_value);
        tv_avg_hr_key = (TextView) findViewById(R.id.tv_avg_hr_key);
        tv_avg_hr_value = (TextView) findViewById(R.id.tv_avg_hr_value);
        tv_hrv_key = (TextView) findViewById(R.id.tv_hrv_key);
        tv_hrv_value = (TextView) findViewById(R.id.tv_hrv_value);
        tv_resp_key = (TextView) findViewById(R.id.tv_resp_key);
        tv_resp_value = (TextView) findViewById(R.id.tv_resp_value);
        tv_bt = (TextView) findViewById(R.id.tv_bt);
        guide_line = (Guideline) findViewById(R.id.guide_line);
        ecg_view = (ChartView) findViewById(R.id.ecg_view);
        tv_ecg_duration = (TextView) findViewById(R.id.tv_ecg_duration);
        gain_spinner = (AppCompatSpinner) findViewById(R.id.gain_spinner);

    }
    void stopAllMeasure(){
        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BT, AllMeasureActivity.this);
        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BP, AllMeasureActivity.this);
        BleManager.getInstance().stopMeasure(MeasureType.TYPE_BG, AllMeasureActivity.this);
        BleManager.getInstance().stopMeasure(MeasureType.TYPE_ECG, AllMeasureActivity.this);
        BleManager.getInstance().stopMeasure(MeasureType.TYPE_SPO2, AllMeasureActivity.this);
        ecg_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                ecg_view.clearDatas();
            }
        },500);
        bt_measure_bt.setText(R.string.start_measure);
    }

}
