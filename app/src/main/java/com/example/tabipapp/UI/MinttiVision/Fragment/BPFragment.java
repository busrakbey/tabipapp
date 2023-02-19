package com.example.tabipapp.UI.MinttiVision.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.tabipapp.R;
import com.example.tabipapp.base.BaseVBindingFragment;
import com.example.tabipapp.databinding.FragmentBpBinding;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.bean.MeasureType;
import com.mintti.visionsdk.ble.callback.IBleWriteResponse;
import com.mintti.visionsdk.ble.callback.IBpResultListener;
import com.mintti.visionsdk.ble.callback.IRawBpDataCallback;


/**
 * Created by leopold on 2021/3/8
 * Description:
 */
public class BPFragment extends BaseVBindingFragment<FragmentBpBinding> implements IBleWriteResponse, IBpResultListener, IRawBpDataCallback,Handler.Callback{
    private static final int MSG_BP_RESULT = 1;
    private static final int MSG_BP_LEAK = 2;
    private static final int MSG_BP_ERROR = 3;
    private static final int MSG_BP_PRESSURE = 4;
    private static final String SYSTOLIC = "systolic";
    private static final String DIASTOLIC = "diastolic";
    private static final String HR = "hr";
    private final Handler handler = new Handler(Looper.getMainLooper(),this);

    public static BPFragment newInstance() {

        Bundle args = new Bundle();
        BPFragment fragment = new BPFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected FragmentBpBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBpBinding.inflate(inflater,container,false);
    }

    @Override
    protected void initView(View rootView) {
        BleManager.getInstance().setBpResultListener(this);
        BleManager.getInstance().setRawBpDataCallback(this);
        getBinding().btMeasureBp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBinding().btMeasureBp.getText().equals(getString(R.string.start_measure))){

                    //开始测量
                    resetValue();
                    BleManager.getInstance().startMeasure(MeasureType.TYPE_BP, BPFragment.this);
                    getBinding().btMeasureBp.setText(R.string.stop_measure);


                }else {
                    //停止测量
                    BleManager.getInstance().stopMeasure(MeasureType.TYPE_BP, BPFragment.this);
                    getBinding().btMeasureBp.setText(R.string.start_measure);

                }
            }
        });
    }


    @Override
    public void onWriteSuccess() {
        Log.e("BpFragment", "onWriteSuccess: BP" );
    }

    @Override
    public void onWriteFailed() {
        Log.e("BpFragment", "onWriteFailed: BP" );
    }




    private void resetValue(){
        getBinding().tvSystolicValue.setText("-- / mmHg");
        getBinding().tvDiastolicValue.setText("-- / mmHg");
        getBinding().tvHrValue.setText("-- / BPM");
        getBinding().tvPressure.setText("0");

    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {

        if (msg.what == MSG_BP_RESULT) {
            Bundle bundle = msg.getData();
            int hr = bundle.getInt(HR);
            int systolic = bundle.getInt(SYSTOLIC);
            int diastolic = bundle.getInt(DIASTOLIC);
            getBinding().tvSystolicValue.setText(systolic + " / mmHg");
            getBinding().tvDiastolicValue.setText(diastolic + " / mmHg");
            getBinding().tvHrValue.setText(hr + " / BPM");
            getBinding().tvPressure.setText("0");
            getBinding().btMeasureBp.setText(R.string.start_measure);

            return true;
        }else if (msg.what == MSG_BP_LEAK){
            MessageDialog.show(R.string.hint,R.string.bp_air_leak,R.string.confirm);
            getBinding().btMeasureBp.setText(R.string.start_measure);
            return true;
        }else if (msg.what == MSG_BP_ERROR){
            MessageDialog.show(R.string.hint,R.string.bp_measure_error,R.string.confirm);
            getBinding().btMeasureBp.setText(R.string.start_measure);
        }else if (msg.what == MSG_BP_PRESSURE) {
            getBinding().tvPressure.setText(msg.arg1+"");
        }
        return false;

    }

    @Override
    public void onBpResult(int systolic, int diastolic, int heartRate) {
        Message msg = handler.obtainMessage(MSG_BP_RESULT);
        Bundle bundle = new Bundle();
        bundle.putInt(SYSTOLIC,systolic);
        bundle.putInt(DIASTOLIC,diastolic);
        bundle.putInt(HR,heartRate);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void onLeadError() {
        Log.e("BpFragment", "onLeadError: " );
        handler.sendEmptyMessage(MSG_BP_LEAK);
    }

    @Override
    public void onBpError() {
        Log.e("BpFragment", "onBpError: " );
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
}
