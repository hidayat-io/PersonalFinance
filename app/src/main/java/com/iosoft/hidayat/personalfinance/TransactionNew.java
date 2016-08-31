package com.iosoft.hidayat.personalfinance;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by hidayat on 27/08/16.
 */
public class TransactionNew extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    EditText txtDate, txtNominal, txtKategori, txtIdKategori;
    ImageView imgCateg;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(R.string.title_new_trans);

        setContentView(R.layout.activity_new_transaction);

        txtDate = (EditText) findViewById(R.id.txtTgl);
        txtNominal = (EditText) findViewById(R.id.txtNominal);
        txtKategori = (EditText) findViewById(R.id.txtKat);
        txtIdKategori = (EditText) findViewById(R.id.txtIdKat);
        imgCateg = (ImageView)findViewById(R.id.imgCateg);

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

    public void showCategoryChoice(View v){

        Intent i = new Intent(TransactionNew.this, TransactionCategory.class);
        startActivityForResult(i, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode){

            case 1:
                if(resultCode == RESULT_OK){

                    Bundle res = data.getExtras();
                    String result = res.getString("result");
                    String[] splitResult = result.split("#");
                    txtKategori.setText(splitResult[0]);
                    txtIdKategori.setText(splitResult[1]);

                    int imageResource = this.getResources().getIdentifier(splitResult[2],"drawable",
                            this.getPackageName());
                    Drawable imgKat = this.getResources().getDrawable(imageResource);

                    imgCateg.setImageDrawable(imgKat);
                }
                break;
        }
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
            case R.id.btrTransOk:
                saveData();
                break;
        }
        return true;
    }

    private void saveData(){


    }
}
