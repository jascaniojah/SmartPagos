package com.example.jascaniojah.smartpagos;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

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

                switch (tab.getPosition())
                {
                    case 0: {
                        mActionbar.setTitle("Consultar Saldo");
                        break;
                    }
                    case 1:{
                        mActionbar.setTitle("Consultar Transacciones");
                        break;}
                    case 2:{
                        mActionbar.setTitle("Vender Saldo");
                        break;
                    }
                    case 3:{
                        mActionbar.setTitle("Notificar Pago");
                        break;
                    }


                }

            }


            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            }
            };
        mActionbar.addTab(mActionbar.newTab().setIcon(R.drawable.ic_action_consultar_saldo).setTabListener(tabListener));
        mActionbar.addTab(mActionbar.newTab().setIcon(R.drawable.ic_action_consultar_t).setTabListener(tabListener));
        mActionbar.addTab(mActionbar.newTab().setIcon(R.drawable.ic_action_vender).setTabListener(tabListener));
        mActionbar.addTab(mActionbar.newTab().setIcon(R.drawable.ic_action_registrar_pago).setTabListener(tabListener));


    }

    long lastPress;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
            Toast.makeText(getBaseContext(), "Presionar BACK de nuevo para salir", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        }else{
            super.onBackPressed();
        }
    }

    }




