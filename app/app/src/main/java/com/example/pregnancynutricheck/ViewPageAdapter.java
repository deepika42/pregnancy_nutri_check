package com.example.pregnancynutricheck;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        First_Fragment first_fragment= new First_Fragment();

        HomeFragment home_fragment= new HomeFragment();
        DietFragment diet_fragment= new DietFragment();
        AppointmentFragment appointment_fragment= new AppointmentFragment();

        position=position+1;
        if(position==1){
            Bundle bundle=new Bundle();
//            bundle.putString("message","Fragment :"+position);
            home_fragment.setArguments(bundle);
            return home_fragment;
        }else if(position==2){
            Bundle bundle=new Bundle();
//            bundle.putString("message","Fragment :"+position);
            diet_fragment.setArguments(bundle);
            return diet_fragment;
        }else{
            Bundle bundle=new Bundle();
//            bundle.putString("message","Fragment :"+position);
            appointment_fragment.setArguments(bundle);
            return appointment_fragment;
        }
        /*Bundle bundle=new Bundle();
        bundle.putString("message","Fragment :"+position);
        first_fragment.setArguments(bundle);
        return first_fragment;*/
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        position=position+1;

        switch (position) {
            case 1:
                return "Home";
            case 2:
                return "Diet";
            case 3:
                return "Appointments";
            default:
                return null;
        }
//        return "Fragment "+position;
    }
}
