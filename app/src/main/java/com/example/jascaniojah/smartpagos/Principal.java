package com.example.jascaniojah.smartpagos;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class Principal extends ActionBarActivity {

    private ViewPager mPager;
    private ImageView Logo;
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
        Logo = (ImageView) findViewById(R.id.logoglobal);
        /** Getting a reference to FragmentManager */
        FragmentManager fm = getSupportFragmentManager();

        /** Defining a listener for pageChange */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
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
                switch (tab.getPosition()) {
                    case 0: {
                        tab.setIcon(R.drawable.ic_action_consultar_saldo);
                        break;
                    }
                    case 1: {
                        tab.setIcon(R.drawable.ic_action_consultar_t);
                        break;
                    }
                    case 2: {
                        tab.setIcon(R.drawable.ic_action_vender);
                        break;
                    }
                    case 3: {
                        tab.setIcon(R.drawable.ic_action_registrar_pago);
                        break;
                    }


                }


            }

            public void onTabSelected(Tab tab, FragmentTransaction ft) {

                mPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0: {
                        mActionbar.setTitle("Consultar Saldo");
                        tab.setIcon(R.drawable.ic_action_consultar_saldo_selected);
                        break;
                    }
                    case 1: {
                        mActionbar.setTitle("Consultar Transacciones");
                        tab.setIcon(R.drawable.ic_action_consultar_t_selected);

                        break;
                    }
                    case 2: {
                        mActionbar.setTitle("Vender Saldo");
                        tab.setIcon(R.drawable.ic_action_ic_action_vender_selected);
                        break;
                    }
                    case 3: {
                        mActionbar.setTitle("Notificar Pago");
                        tab.setIcon(R.drawable.ic_action_registrar_pago_selected);
                        Logo.setEnabled(false);
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

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrar:
                Log.i("ActionBar", "Cerrar!");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Desea cerrar sesion?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent upanel = new Intent(getApplicationContext(), LoginActivity.class);
                                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(upanel);
                                /**
                                 * Close Login Screen
                                 **/
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    CountDownTimer timer = new CountDownTimer(1 * 60 * 1000 * 30, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
            }

        public void onFinish() {
            //Logout
            salir();

        }
    };

    public void salir()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sesion Caducada")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent upanel = new Intent(getApplicationContext(), LoginActivity.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(upanel);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    };

    @Override
    public void onUserInteraction(){
        timer.cancel();
        timer.start();
    }

}



