package com.example.jascaniojah.smartpagos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jascaniojah on 18/09/14.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub

        }
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    //Fragement for Android Tab
                    return new Saldo();
                case 1:
                    //Fragment for Ios Tab
                    return new Trans();
                case 2:
                    //Fragment for Windows Tab
                    return new Recarga();
                case 3:
                    //Fragment for Windows Tab
                    return new Notificar();
            }
            return null;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 4; //No of Tabs
        }
    }

