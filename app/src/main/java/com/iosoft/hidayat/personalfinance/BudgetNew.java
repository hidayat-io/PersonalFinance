package com.iosoft.hidayat.personalfinance;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.iosoft.hidayat.personalfinance.model.Budget;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by hidayat on 27/08/16.
 */
public class BudgetNew extends AppCompatActivity {

    private DBHelper myDB;
    EditText txtNominal, txtKategori,
            txtIdKategori, txtTypeKat,
            txtIdBudget;
    Button btnDelete;
    ImageView imgCateg;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        myDB = new DBHelper(this);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_new_budget);

        btnDelete = (Button) findViewById(R.id.btnDelete);

        txtNominal = (EditText) findViewById(R.id.txtNominal);
        txtKategori = (EditText) findViewById(R.id.txtKat);
        txtIdKategori = (EditText) findViewById(R.id.txtIdKat);
        txtTypeKat = (EditText) findViewById(R.id.txtTypeKat);
        txtIdBudget = (EditText) findViewById(R.id.txtIdBudget);
        imgCateg = (ImageView)findViewById(R.id.imgCateg);

        txtNominal.addTextChangedListener(onNominalChangeListener());

        Intent i = getIntent();
        Budget iData = i.getParcelableExtra("budget");

        if(iData==null){ //new transaction

            this.getSupportActionBar().setTitle(R.string.title_new_budget);
            btnDelete.setVisibility(View.GONE);
        }
        else{ //edit transaction with paramater iData

            this.getSupportActionBar().setTitle(R.string.title_update_budget);

            //hidden field
            txtIdBudget.setText(String.valueOf(iData.getIdBudget()));
            txtIdKategori.setText(String.valueOf(iData.getIdCategory()));
            txtKategori.setText(iData.getDesc());
            txtNominal.setText(String.valueOf(iData.getBudgetAmount()));

            //set image kategori
            int imageResource = this.getResources().getIdentifier(iData.getIcon(),"drawable",
                    this.getPackageName());
            Drawable imgKat = this.getResources().getDrawable(imageResource);

            imgCateg.setImageDrawable(imgKat);
        }

    }

    public void showCategoryChoice(View v){

        Intent i = new Intent(BudgetNew.this, BudgetCategory.class);
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

        String iCat, iAmountText, idBudget;
        int iAmount, intCat;

        iCat = txtIdKategori.getText().toString();
        iAmountText = txtNominal.getText().toString();
        iAmountText = iAmountText.replace(",","");
        idBudget = txtIdBudget.getText().toString();

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


        if(iCat.matches("")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Silahkan pilih kategori.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }

        Calendar today = Calendar.getInstance();
        SimpleDateFormat formatSave = new SimpleDateFormat("yyMM");
        String monthYear = formatSave.format(today.getTime());

        intCat = Integer.parseInt(iCat);

        if(idBudget.matches("")){ //new transaction

            //check is category and month already in DB or not
            Boolean isValid = myDB.validBudget(monthYear,intCat);

            if(isValid==false){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Opps, kategori tersebut sudah ada di database.");
                builder.setPositiveButton("OK",null);
                builder.show();
                return;
            }

            myDB.saveBudget(monthYear,iAmount,intCat);
        }
        else{ //update transaction

            //myDB.updateTransaksi(dateForSave, iType, intCat, iDesc, iAmount, Integer.parseInt(idTrans));
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

                myDB.deleteTransaksi(Integer.parseInt(txtIdBudget.getText().toString()));
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
