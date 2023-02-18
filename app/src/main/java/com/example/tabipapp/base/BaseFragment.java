package com.example.tabipapp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected abstract int setLayout();
    protected Toast toast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(setLayout(),container,false);

        initView(rootView);
        return rootView;
    }

    protected void initView(View rootView) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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


}
