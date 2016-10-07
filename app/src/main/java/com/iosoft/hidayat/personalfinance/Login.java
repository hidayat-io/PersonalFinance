package com.iosoft.hidayat.personalfinance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.iosoft.hidayat.personalfinance.adapter.AdapterCategoryChoice;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

/**
 * Created by hidayat on 29/08/16.
 */
public class Login extends AppCompatActivity{

    private DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new DBHelper(this);
        setContentView(R.layout.activity_login);
    }

    public void doLogin(View v){

        EditText loginInput = (EditText) findViewById(R.id.txtLogin);

        String pwd = myDB.getLoginPass();
        String inputedPwd = loginInput.getText().toString();

        if(!inputedPwd.matches(pwd)){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Password tidak sesuai.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }
        else{

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
