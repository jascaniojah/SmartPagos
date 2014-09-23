package com.example.jascaniojah.smartpagos;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

public class Principal extends ActionBarActivity {

        private ViewPager mPager;

    ActionBar mActionbar;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /** Getting a reference to action bar of this activity */
        mActionbar = getSupportActionBar();

        /** Set tab navigation mode */
        mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        /** Getting a reference to ViewPager from the layout */
        mPager = (ViewPager) findViewById(R.id.pager);

        /** Getting a reference to FragmentManager */
        FragmentManager fm = getSupportFragmentManager();

        /** Defining a listener for pageChange */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionbar.setSelectedNavigationItem(position);
        }
        };

        /** Setting the pageChange listener to the viewPager */
        mPager.setOnPageChangeListener(pageChangeListener);
        /** Creating an instance of FragmentPagerAdapter */
        TabPagerAdapter fragmentPagerAdapter = new TabPagerAdapter(fm);

        /** Setting the FragmentPagerAdapter object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);

        mActionbar.setDisplayShowTitleEnabled(true);

        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            }

            public void onTabSelected(Tab tab, FragmentTransaction ft) {
            mPager.setCurrentItem(tab.getPosition());
            }


            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            }
            };
        mActionbar.addTab(mActionbar.newTab().setText("Consultar Saldo").setTabListener(tabListener));
        mActionbar.addTab(mActionbar.newTab().setText("Consultar Transacciones").setTabListener(tabListener));
        mActionbar.addTab(mActionbar.newTab().setText("Vender").setTabListener(tabListener));
        mActionbar.addTab(mActionbar.newTab().setText("Notificar Deposito").setTabListener(tabListener));
    }
    }




