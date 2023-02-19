package com.example.tabipapp.UI.MinttiVision.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tabipapp.R;
import com.example.tabipapp.base.BaseVBindingFragment;
import com.example.tabipapp.databinding.FragmentBtBinding;
import com.mintti.visionsdk.ble.BleManager;
import com.mintti.visionsdk.ble.bean.MeasureType;
import com.mintti.visionsdk.ble.callback.IBleWriteResponse;
import com.mintti.visionsdk.ble.callback.IBtResultListener;

public class BTFragment extends BaseVBindingFragment<FragmentBtBinding> implements IBtResultListener, IBleWriteResponse {
    public static BTFragment newInstance() {
        Bundle args = new Bundle();
        BTFragment fragment = new BTFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected FragmentBtBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBtBinding.inflate(inflater,container,false);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        BleManager.getInstance().setBtResultListener(this);
        getBinding().btMeasureBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBinding().btMeasureBt.getText().equals(getString(R.string.start_measure))){

                    //开始测量
                    BleManager.getInstance().startMeasure(MeasureType.TYPE_BT, BTFragment.this);
                    getBinding().btMeasureBt.setText(R.string.stop_measure);

                }else {
                    //停止测量
                    BleManager.getInstance().stopMeasure(MeasureType.TYPE_BT, BTFragment.this);
                    getBinding().btMeasureBt.setText(R.string.start_measure);

                }
            }
        });
    }

    @Override
    public void onWriteSuccess() {
        Log.e("BTFragment", "onWriteSuccess: BT" );
    }

    @Override
    public void onWriteFailed() {
        Log.e("BTFragment", "onWriteFailed: BT" );

    }

    @Override
    public void onBtResult(double temperature) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getBinding().tvBt.setText(temperature+" ℃");
                getBinding().btMeasureBt.setText(R.string.start_measure);
            }
        });
    }
}
