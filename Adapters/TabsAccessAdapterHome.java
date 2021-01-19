package com.pheonix.org.hallaka.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pheonix.org.hallaka.Activity.Home.Fragments.Home.HomeFragment;
import com.pheonix.org.hallaka.Activity.Home.Fragments.Shop.ShopFragment;

public class TabsAccessAdapterHome extends FragmentPagerAdapter {

    public TabsAccessAdapterHome(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ShopFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Shop";

            default:
                return null;
        }
    }
}
