package com.example.tabipapp.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.tabipapp.R;
import com.mintti.visionsdk.ble.BleDevice;


import java.util.List;

public class BleDeviceAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {
    public BleDeviceAdapter(List<BleDevice> data) {
        super(R.layout.item_ble_device, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BleDevice bleDevice) {
        holder.setText(R.id.bleMac,bleDevice.getMac());
        holder.setText(R.id.ble_rssi,bleDevice.getRssi()+"dpm");
        holder.setText(R.id.bleName,bleDevice.getName());
    }
}
