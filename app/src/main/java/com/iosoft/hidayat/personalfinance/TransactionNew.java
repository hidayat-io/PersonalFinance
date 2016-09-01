package com.iosoft.hidayat.personalfinance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    EditText txtDate, txtNominal, txtKategori,
            txtIdKategori, txtDesc, txtTypeKat,
            txtIdTrans;
    Button btnDelete;
    ImageView imgCateg;

    Locale id = new Locale("in","ID");
    final Calendar c = Calendar.getInstance();
    SimpleDateFormat formater = new SimpleDateFormat("EEEE, d MMMM yyyy",id);

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        myDB = new DBHelper(this);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_new_transaction);

        btnDelete = (Button) findViewById(R.id.btnDelete);

        txtDesc = (EditText) findViewById(R.id.txtDesc);
        txtDate = (EditText) findViewById(R.id.txtTgl);
        txtNominal = (EditText) findViewById(R.id.txtNominal);
        txtKategori = (EditText) findViewById(R.id.txtKat);
        txtIdKategori = (EditText) findViewById(R.id.txtIdKat);
        txtTypeKat = (EditText) findViewById(R.id.txtTypeKat);
        txtIdTrans = (EditText) findViewById(R.id.txtIdTrans);
        imgCateg = (ImageView)findViewById(R.id.imgCateg);

        txtNominal.addTextChangedListener(onNominalChangeListener());

        Intent i = getIntent();
        String iData = i.getStringExtra("iData");

        if(iData==null){ //new transaction

            this.getSupportActionBar().setTitle(R.string.title_new_trans);

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            txtNominal.requestFocus();

            btnDelete.setVisibility(View.GONE);
        }
        else{ //edit transaction with paramater iData

            this.getSupportActionBar().setTitle(R.string.title_update_trans);

            String[] splitData = iData.split("##");

            //hidden field
            txtIdTrans.setText(splitData[0]);
            txtIdKategori.setText(splitData[3]);
            txtTypeKat.setText(splitData[2]);

            txtDate.setText(splitData[1]);
            txtKategori.setText(splitData[6]);
            txtNominal.setText(splitData[5]);
            txtDesc.setText(splitData[4]);

            //set image kategori
            int imageResource = this.getResources().getIdentifier(splitData[7],"drawable",
                    this.getPackageName());
            Drawable imgKat = this.getResources().getDrawable(imageResource);

            imgCateg.setImageDrawable(imgKat);

            //format date
            String[] splittedDate = splitData[1].split("/");

            mYear = Integer.parseInt(splittedDate[0]);
            mMonth = Integer.parseInt(splittedDate[1])-1;
            mDay = Integer.parseInt(splittedDate[2]);

            c.set(mYear,mMonth,mDay);
        }

        txtDate.setText(formater.format(c.getTime()));
    }

    public void showDatePickerDialog(View v) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionNew.this,
                new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;

                        c.set(mYear,mMonth,mDay);

                        txtDate.setText(formater.format(c.getTime()));

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
                    txtTypeKat.setText(splitResult[3]);

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

    private TextWatcher onNominalChangeListener(){

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                txtNominal.removeTextChangedListener(this);

                try {

                    String oriString = s.toString();
                    Long longVal;

                    if(oriString.contains(",")){
                        oriString = oriString.replaceAll(",","");
                    }
                    longVal = Long.parseLong(oriString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

                    formatter.applyPattern("#,###,###");
                    String formattedString = formatter.format(longVal);

                    txtNominal.setText(formattedString);
                    txtNominal.setSelection(txtNominal.getText().length());

                }catch (NumberFormatException nfe){

                    nfe.printStackTrace();
                }

                txtNominal.addTextChangedListener(this);
            }
        };
    }

    private void saveData(){

        String iCat, iDesc, iAmountText, iType, idTrans;
        int iAmount, intCat;
        Date iDate = null;

        iCat = txtIdKategori.getText().toString();
        iAmountText = txtNominal.getText().toString();
        iAmountText = iAmountText.replace(",","");
        iDesc = txtDesc.getText().toString();
        iType = txtTypeKat.getText().toString();
        idTrans = txtIdTrans.getText().toString();

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

        SimpleDateFormat formatSave = new SimpleDateFormat("yyyy/MM/dd");
        String dateForSave;

        dateForSave = formatSave.format(c.getTime());

        intCat = Integer.parseInt(iCat);

        if(idTrans.matches("")){ //new transaction

            myDB.saveTransaksi(dateForSave, iType, intCat, iDesc, iAmount);
        }
        else{ //update transaction

            myDB.updateTransaksi(dateForSave, iType, intCat, iDesc, iAmount, Integer.parseInt(idTrans));
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void deleteTrans(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin akan menghapus transaksi ini ?");
        builder.setIcon(R.drawable.ic_remove_circle);
        builder.setTitle("Konfirmasi");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                myDB.deleteTransaksi(Integer.parseInt(txtIdTrans.getText().toString()));
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Tidak",null);
        builder.show();
    }
}
