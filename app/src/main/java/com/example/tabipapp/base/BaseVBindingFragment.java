package com.example.tabipapp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

/**
 * Created by leopold on 2021/3/26
 * Description:
 */
public abstract class BaseVBindingFragment<VB extends ViewBinding> extends Fragment {
    private VB vBinding;
    protected Toast toast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        vBinding = getViewBinding(inflater, container);
        initView(vBinding.getRoot());
        return vBinding.getRoot();
    }
    protected VB getBinding(){
        return this.vBinding;
    }
    protected abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);


    protected void initView(View rootView) {
    }

    protected void showToast(String msg){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null){
                    toast.cancel();
                }
                toast = Toast.makeText(requireContext(),msg,Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vBinding = null;
    }
}
