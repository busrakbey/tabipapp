package com.example.tabipapp.UI.MinttiVision.Fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.example.tabipapp.base.BaseVBindingFragment;
import com.mintti.visionsdk.ble.handle.HandleDataCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;


public abstract class BaseMeasureFragment<VB extends ViewBinding> extends BaseVBindingFragment<VB> implements HandleDataCallback {

    protected boolean isNotify = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    protected File createFile(String fileName){
        try {
            Date date = new Date(System.currentTimeMillis());
            File fileDir = requireContext().getExternalFilesDir("data");
            for (File file : fileDir.listFiles()) {
                if (file.getName().endsWith(fileName)){
                    file.delete();
                }
            }
            File file = new File(fileDir,
                    UUID.randomUUID().toString().substring(0,8)+"_"+fileName);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected FileOutputStream createFos(File file){
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected abstract void closeFile();

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeFile();
    }
}
