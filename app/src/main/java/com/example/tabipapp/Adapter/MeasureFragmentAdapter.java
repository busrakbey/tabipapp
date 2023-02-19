package com.example.tabipapp.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.tabipapp.R;
import com.example.tabipapp.UI.MinttiVision.Fragment.BPFragment;
import com.example.tabipapp.UI.MinttiVision.Fragment.BTFragment;

import java.util.ArrayList;
import java.util.List;

public class MeasureFragmentAdapter extends FragmentPagerAdapter {
    private final String[] title ;
    private final List<Fragment> fragmentList = new ArrayList<>();


    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();


    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }


    public MeasureFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        title = context.getResources().getStringArray(R.array.function_list);
       fragmentList.add(BPFragment.newInstance());
        fragmentList.add(BTFragment.newInstance());

       /* fragmentList.add(BGFragment.newInstance());
        fragmentList.add(Spo2Fragment.newInstance());
        fragmentList.add(ECGFragment.newInstance());
        fragmentList.add(DeviceInfoFragment.newInstance());*/


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }
}
