package com.iosoft.hidayat.personalfinance;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by hidayat on 27/08/16.
 */
public class SavingNew extends AppCompatActivity {

    private DBHelper myDB;
    EditText txtNominal, txtIdSaving, txtSavingDesc;
    String strData=null;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        myDB = new DBHelper(this);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_new_saving);

        txtNominal = (EditText) findViewById(R.id.txtNominal);
        txtIdSaving = (EditText) findViewById(R.id.txtIdSaving);
        txtSavingDesc = (EditText) findViewById(R.id.txtSavingDesc);

        txtNominal.addTextChangedListener(onNominalChangeListener(txtNominal));

        Intent i = getIntent();
        strData = i.getStringExtra("strDataSaving");

        if(strData==null){ //new transaction

            this.getSupportActionBar().setTitle(R.string.title_simpanan_new);

            RelativeLayout layoutSaving = (RelativeLayout) findViewById(R.id.layoutSavingAction);
            layoutSaving.setVisibility(View.GONE);
        }
        else{ //edit transaction with paramater iData

            this.getSupportActionBar().setTitle(R.string.title_update_simpanan);

            String[] splitSaving = strData.split("#");

            txtIdSaving.setText(splitSaving[0]);
            txtSavingDesc.setText(splitSaving[1]);
            txtNominal.setText(splitSaving[2]);

            //load list transaction to recyclerview
            String param = " id_simp="+Integer.parseInt(splitSaving[0]);

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

        Intent i = new Intent(SavingNew.this, BudgetCategory.class);
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

    private TextWatcher onNominalChangeListener(final EditText iEdit){

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                iEdit.removeTextChangedListener(this);

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

                    iEdit.setText(formattedString);
                    iEdit.setSelection(iEdit.getText().length());

                }catch (NumberFormatException nfe){

                    nfe.printStackTrace();
                }

                iEdit.addTextChangedListener(this);
            }
        };
    }

    private void saveData(){

        String iDesc, iAmountText, idSaving;
        int iAmount;

        iDesc = txtSavingDesc.getText().toString();
        iAmountText = txtNominal.getText().toString();
        iAmountText = iAmountText.replace(",","");
        idSaving = txtIdSaving.getText().toString();

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
            builder.setMessage("Opps, deskripsi simpanan harus diisi.");
            builder.setPositiveButton("OK",null);
            builder.show();
            return;
        }


        if(idSaving.matches("")){ //new transaction

            myDB.saveSavingPlan(iDesc,iAmount);
        }
        else{ //update transaction

            myDB.updateSavingPlan(iDesc,iAmount,Integer.parseInt(idSaving));
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void depositSaving(View v){

        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

        final EditText amountSaving = new EditText(SavingNew.this);
        amountSaving.setInputType(InputType.TYPE_CLASS_NUMBER);
        amountSaving.addTextChangedListener(onNominalChangeListener(amountSaving));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        amountSaving.setLayoutParams(lp);

        dialog.setIcon(R.drawable.ic_dollar);
        dialog.setTitle("Jumlah Dana");
        dialog.setView(amountSaving);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int iAmount;
                String iAmountText;

                iAmountText = amountSaving.getText().toString();
                iAmountText = iAmountText.replace(",","");

                if(iAmountText.matches("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(SavingNew.this);
                    builder.setMessage("Opps, Nominal tidak boleh kosong.\nTransaksi gagal.");
                    builder.setPositiveButton("OK",null);
                    builder.show();
                    return;
                }
                else{

                    iAmount = Integer.parseInt(iAmountText);

                    if(iAmount<=0){

                        AlertDialog.Builder builder = new AlertDialog.Builder(SavingNew.this);
                        builder.setMessage("Opps, Nominal harus lebih dari 0 (Nol).\nTransaksi gagal.");
                        builder.setPositiveButton("OK",null);
                        builder.show();
                        return;
                    }
                }

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatSave = new SimpleDateFormat("yyyy/MM/dd");
                String dateForSave = formatSave.format(cal.getTime());
                String savingDesc = txtSavingDesc.getText().toString();
                int savingId = Integer.parseInt(txtIdSaving.getText().toString());

                myDB.saveTransaksi(dateForSave, "o", 20, "Simpan dana ke Simpanan "+savingDesc, iAmount, savingId);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void withdrawSaving(View v){

        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());

        final EditText amountSaving = new EditText(SavingNew.this);
        amountSaving.setInputType(InputType.TYPE_CLASS_NUMBER);
        amountSaving.addTextChangedListener(onNominalChangeListener(amountSaving));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        amountSaving.setLayoutParams(lp);

        dialog.setIcon(R.drawable.ic_dollar);
        dialog.setTitle("Jumlah Dana");
        dialog.setView(amountSaving);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int iAmount;
                String iAmountText;

                iAmountText = amountSaving.getText().toString();
                iAmountText = iAmountText.replace(",","");

                if(iAmountText.matches("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(SavingNew.this);
                    builder.setMessage("Opps, Nominal tidak boleh kosong.\nTransaksi gagal.");
                    builder.setPositiveButton("OK",null);
                    builder.show();
                    return;
                }
                else{

                    iAmount = Integer.parseInt(iAmountText);

                    if(iAmount<=0){

                        AlertDialog.Builder builder = new AlertDialog.Builder(SavingNew.this);
                        builder.setMessage("Opps, Nominal harus lebih dari 0 (Nol).\nTransaksi gagal.");
                        builder.setPositiveButton("OK",null);
                        builder.show();
                        return;
                    }
                }

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatSave = new SimpleDateFormat("yyyy/MM/dd");
                String dateForSave = formatSave.format(cal.getTime());
                String savingDesc = txtSavingDesc.getText().toString();
                int savingId = Integer.parseInt(txtIdSaving.getText().toString());

                myDB.saveTransaksi(dateForSave, "i", 9, "Ambil dana dari Simpanan "+savingDesc, iAmount, savingId);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void deleteSaving(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin akan menghapus Target Simpanan ini ?");
        builder.setIcon(R.drawable.ic_remove_circle);
        builder.setTitle("Konfirmasi");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                myDB.deleteSavingPlan(Integer.parseInt(txtIdSaving.getText().toString()));
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
