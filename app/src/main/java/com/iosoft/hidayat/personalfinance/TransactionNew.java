package com.iosoft.hidayat.personalfinance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

/**
 * Created by hidayat on 27/08/16.
 */
public class TransactionNew extends AppCompatActivity {

    private DBHelper myDB;
    private int mYear, mMonth, mDay;
    EditText txtDate, txtNominal, txtKategori, txtIdKategori, txtDesc, txtTypeKat;
    ImageView imgCateg;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        myDB = new DBHelper(this);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(R.string.title_new_trans);

        setContentView(R.layout.activity_new_transaction);

        txtDesc = (EditText) findViewById(R.id.txtDesc);
        txtDate = (EditText) findViewById(R.id.txtTgl);
        txtNominal = (EditText) findViewById(R.id.txtNominal);
        txtKategori = (EditText) findViewById(R.id.txtKat);
        txtIdKategori = (EditText) findViewById(R.id.txtIdKat);
        txtTypeKat = (EditText) findViewById(R.id.txtTypeKat);
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
                    txtTypeKat.setText(splitResult[2]);

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

        String iDateText, iCat, iDesc, iAmountText, iType;
        int iAmount, intCat;
        Date iDate = null;

        iDateText = txtDate.getText().toString();
        iCat = txtIdKategori.getText().toString();
        iAmountText = txtNominal.getText().toString();
        iDesc = txtDesc.getText().toString();
        iType = txtTypeKat.getText().toString();

        if(iAmountText.matches("")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Opps, Nominal tidak boleh kosong.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }
        else{

            iAmount = Integer.parseInt(iAmountText);

            if(iAmount<=0){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Opps, Nominal harus lebih dari 0 (Nol).");
                builder.setPositiveButton("OK",null);
                builder.show();
                return;
            }
        }

        if(iDesc.matches("")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Opps, Keterangan tidak boleh kosong.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }

        if(iCat.matches("")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan pilih kategori.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }

        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatSave = new SimpleDateFormat("yyyy/MM/dd");
        String dateForSave;

        try {

            iDate = formater.parse(iDateText);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        dateForSave = formatSave.format(iDate);

        intCat = Integer.parseInt(iCat);

        myDB.saveTransaksi(dateForSave, iType, intCat, iDesc, iAmount);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
