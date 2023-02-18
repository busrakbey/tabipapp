package com.example.tabipapp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

/**
 * Created by leopold on 2021/3/26
 * Description:
 */
public abstract class BaseVBindingActivity<VB extends ViewBinding> extends BaseActivity {

    private VB vBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vBinding = getViewBinding();
        setContentView(vBinding.getRoot());

    }
    protected VB getBinding(){
        return vBinding;
    }
    public abstract VB getViewBinding();

}
