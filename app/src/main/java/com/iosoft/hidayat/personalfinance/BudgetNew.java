package com.iosoft.hidayat.personalfinance;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListBudgetTransaction;
import com.iosoft.hidayat.personalfinance.model.Budget;
import com.iosoft.hidayat.personalfinance.model.Transaction;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;


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

            //load list transaction to recyclerview
            String strMonth, startDate, endDate, iYear, iMonth;
            SimpleDateFormat formaterMonth = new SimpleDateFormat("yyyy/MM/");

            strMonth = iData.getBudget_month();
            iYear = "20"+strMonth.substring(0,2);
            iMonth = strMonth.substring(2);
            Calendar calendar = new GregorianCalendar(Integer.parseInt(iYear),
                                                        Integer.parseInt(iMonth)-1,1);

            startDate = formaterMonth.format(calendar.getTime())+"01";
            endDate = formaterMonth.format(calendar.getTime())+calendar.getActualMaximum(calendar.DATE);

            String param = " transaksi.id_kat ='" + iData.getIdCategory() + "' "+
                                "AND (tgl BETWEEN '"+startDate+"' AND '"+endDate+"')";

            ArrayList<HashMap<String, String>> transData = myDB.getTransListByParam(param);
            ArrayList<Object> items = new ArrayList<>();
            String strTransDate = "";

            if(transData.size()>0){

                for (int x=0;x<transData.size();x++){

                    //on different date, add new header Date
                    if(!strTransDate.matches(transData.get(x).get("tgl"))){

                        String strDate = transData.get(x).get("tgl");
                        Date iDate = null;
                        Locale id = new Locale("in","ID");
                        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");

                        try {

                            iDate = formater.parse(strDate);

                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat formatTransDate = new SimpleDateFormat("dd MMMM yyyy", id);

                        items.add(formatTransDate.format(iDate));
                    }


                    Transaction itemTrans = new Transaction(Integer.parseInt(transData.get(x).get("id_trans")),
                            transData.get(x).get("tgl"),
                            transData.get(x).get("desk"),
                            Integer.parseInt(transData.get(x).get("id_kat")),
                            transData.get(x).get("tipe"),
                            transData.get(x).get("kat"),
                            transData.get(x).get("ic_kat"),
                            Integer.parseInt(transData.get(x).get("nominal"))
                    );
                    items.add(itemTrans);

                    strTransDate = transData.get(x).get("tgl");
                }
            }

            AdapterListBudgetTransaction mAdapter = new AdapterListBudgetTransaction(items);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvListTransaction);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL,false);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
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

            myDB.updateBudget(Integer.parseInt(idBudget),iAmount,intCat);
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void deleteBudget(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin akan menghapus anggaran ini ?");
        builder.setIcon(R.drawable.ic_remove_circle);
        builder.setTitle("Konfirmasi");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                myDB.deleteBudget(Integer.parseInt(txtIdBudget.getText().toString()));
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
