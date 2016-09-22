// don't forget to BISMILLAH before writing < code >

package com.iosoft.hidayat.personalfinance;

import com.iosoft.hidayat.personalfinance.MyApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        MyApp mApp = ((MyApp)getApplicationContext());
        int selectedMenu = mApp.getSelectedMenu();

        if(selectedMenu==0){

//            Fragment fragment = null;
//            Class fragmentClass = FragmentTrans.class;
//
//            try{
//
//                fragment = (Fragment) fragmentClass.newInstance();
//            }
//            catch (Exception e){
//
//                e.printStackTrace();
//            }
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            navigationView.setCheckedItem(R.id.nav_transaction);
            navigationView.getMenu().performIdentifierAction(R.id.nav_transaction, 0);
        }
        else{

            displayView(selectedMenu);
            navigationView.setCheckedItem(selectedMenu);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    private void displayView(int id){

        MyApp mApp = (MyApp)getApplicationContext();
        mApp.setSelectedMenu(id);

        Fragment fragment = null;
        Class fragmentClass = null;



        if (id == R.id.nav_transaction) {

            fragmentClass = FragmentTrans.class;
        }
        else if (id == R.id.nav_budget) {

            fragmentClass = FragmentBudget.class;
        }
        else if (id == R.id.nav_saving) {

            fragmentClass = FragmentSaving.class;
        }
        else if(id==R.id.nav_category){

            fragmentClass = FragmentCategoryIn.class;
        }

        try{

            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e){

            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
