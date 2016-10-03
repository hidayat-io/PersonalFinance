package com.iosoft.hidayat.personalfinance;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListBudgetTransaction;
import com.iosoft.hidayat.personalfinance.model.Transaction;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by hidayat on 27/08/16.
 */
public class CategoryNew extends AppCompatActivity {

    private DBHelper myDB;
    private ImageView imgCategory;
    private EditText txtCategoryName, txtIdCategory;
    private RadioGroup rdoGroupType;
    private RadioButton rdoType;

    String strData=null;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        myDB = new DBHelper(this);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setContentView(R.layout.activity_new_category);

        rdoGroupType = (RadioGroup) findViewById(R.id.radioCategType);
        txtIdCategory = (EditText) findViewById(R.id.txtIdCategory);
        txtCategoryName = (EditText) findViewById(R.id.txtCategoryDesc);
        imgCategory = (ImageView) findViewById(R.id.imgCategoryIco);

        Intent i = getIntent();
        strData = i.getStringExtra("strDataCategory");

        if(strData==null){

            this.getSupportActionBar().setTitle("Tambah Kategori");

            txtCategoryName.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(txtCategoryName, InputMethodManager.SHOW_IMPLICIT);
        }
        else{

            RadioButton rdoIn = (RadioButton) findViewById(R.id.radioIncome);
            RadioButton rdoOut = (RadioButton) findViewById(R.id.radioOutcome);

            this.getSupportActionBar().setTitle("Update Kategori");

            String[] splitCategory = strData.split("#");

            if(splitCategory[3].matches("i")){

                rdoIn.setChecked(true);
            }
            else{

                rdoOut.setChecked(true);
            }

            txtIdCategory.setText(splitCategory[1]);
            txtCategoryName.setText(splitCategory[0]);

            int imageResource = this.getResources().getIdentifier(splitCategory[2],"drawable",
                    this.getPackageName());
            Drawable imgKat = this.getResources().getDrawable(imageResource);

            imgCategory.setImageDrawable(imgKat);
        }
    }

    public void showCategoryChoice(View v){

        Intent i = new Intent(CategoryNew.this, BudgetCategory.class);
        startActivityForResult(i, 1);
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

            case R.id.btrDel:
                deleteSaving();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(strData!=null){

            menu.findItem(R.id.btrDel).setVisible(true);
        }
        return true;
    }


    private void saveData(){

        String categName, selectedRadio, idCategory;
        int selectedRadioId = rdoGroupType.getCheckedRadioButtonId();

        rdoType = (RadioButton) findViewById(selectedRadioId);
        selectedRadio = rdoType.getText().toString();
        selectedRadio = selectedRadio.matches("Pengeluaran")?"o":"i";
        categName = txtCategoryName.getText().toString().trim();
        idCategory = txtIdCategory.getText().toString();


        if(categName.matches("")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Opps, Nama kategori harus diisi.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }

        if(idCategory.matches("")){ //new

            myDB.SaveNewCategory(categName,selectedRadio,"ic_cat_netral");
        }
        else{ //update

            myDB.updateCategory(categName,selectedRadio,Integer.parseInt(idCategory));
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void deleteSaving(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin akan menghapus Target Simpanan ini ?");
        builder.setIcon(R.drawable.ic_remove_circle);
        builder.setTitle("Konfirmasi");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int idCategory = Integer.parseInt(txtIdCategory.getText().toString());

                if(idCategory<=20){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoryNew.this);
                    builder.setMessage("Anda tidak dapat menghapus kategori default sistem.");
                    builder.setTitle("Gagal Hapus Data");
                    builder.setPositiveButton("OK",null);
                    builder.show();
                    return;
                }

                myDB.deleteCategory(idCategory);
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
