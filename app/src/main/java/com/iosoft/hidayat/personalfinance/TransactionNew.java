package com.iosoft.hidayat.personalfinance;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by hidayat on 27/08/16.
 */
public class TransactionNew extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    EditText txtDate, txtNominal;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(R.string.title_new_trans);

        setContentView(R.layout.activity_new_transaction);

        txtDate = (EditText) findViewById(R.id.txtTgl);
        txtNominal = (EditText) findViewById(R.id.txtNominal);

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        txtDate.setText(mDay+"/"+(mMonth+1)+"/"+mYear);
        txtNominal.requestFocus();
    }

    public void showDatePickerDialog(View v) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionNew.this,
                new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        txtDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }

                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contextual_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
