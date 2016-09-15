package com.iosoft.hidayat.personalfinance;

import android.app.Application;

/**
 * Created by hidayat on 13/09/16.
 */
public class MyApp extends Application {

    private int selectedMenu = 0;

    public MyApp(){

    }

    public int getSelectedMenu() {

        return selectedMenu;
    }

    public void setSelectedMenu(int id) {

        this.selectedMenu = id;
    }
}