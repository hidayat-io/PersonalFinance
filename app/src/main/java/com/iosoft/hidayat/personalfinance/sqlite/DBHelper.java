package com.iosoft.hidayat.personalfinance.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hidayat on 24/08/16.
 */
public class DBHelper{

    private static SQLiteDatabase myDB = null;
    private static String tbTrans, tbKategori, tbAnggaran;

    public DBHelper(Context context){

        openDatabase(context);
    }

    public void openDatabase(Context context){

        myDB = context.openOrCreateDatabase("pfDB.db", Context.MODE_PRIVATE, null);

        //string query create table
        tbTrans = "CREATE TABLE IF NOT EXISTS transaksi " +
                "( id_trans INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " tgl TEXT, " +
                " tipe TEXT, " +
                " id_kat INT, " +
                " desk TEXT,  " +
                " nominal INT, " +
                " id_simp INT)";

        tbKategori = "CREATE TABLE IF NOT EXISTS kategori " +
                "( id_kat INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nama_kat TEXT, " +
                " tipe_trans TEXT, " +
                " icon_kat TEXT )";

        tbAnggaran = "CREATE TABLE IF NOT EXISTS anggaran " +
                "( id_angg INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " bulan TEXT, " +
                " jml_angg INTEGER, " +
                " kat_angg INTEGER )";

        //exec create table
        myDB.execSQL(tbTrans);
        myDB.execSQL(tbKategori);
        myDB.execSQL(tbAnggaran);



        //-- sample kategori
//        myDB.execSQL("DELETE FROM kategori");
//        myDB.execSQL("INSERT INTO kategori values('1','Makanan & Minuman','o','ic_cat_food')");
//        myDB.execSQL("INSERT INTO kategori values('2','Tagihan & Utilitas','o','ic_cat_bill')");
//        myDB.execSQL("INSERT INTO kategori values('3','Teman & Kekasih','o','ic_cat_friend')");
//        myDB.execSQL("INSERT INTO kategori values('4','Gaji','i','ic_cat_salary')");
//        myDB.execSQL("INSERT INTO kategori values('10','Simpanan','o','ic_saving')");

        //-- sample data anggaran
        myDB.execSQL("DELETE FROM anggaran");

        String[] sqlAnggaran = new String[3];

        sqlAnggaran[0] = "INSERT INTO anggaran(bulan, jml_angg, kat_angg) " +
                "VALUES('1609', 200000, 1)";
        sqlAnggaran[1] = "INSERT INTO anggaran(bulan, jml_angg, kat_angg) " +
                "VALUES('1609', 200000, 2)";
        sqlAnggaran[2] = "INSERT INTO anggaran(bulan, jml_angg, kat_angg) " +
                "VALUES('1609', 150000, 3)";

        for (int i=0;i<sqlAnggaran.length;i++){

            myDB.execSQL(sqlAnggaran[i]);
        }

    }

    public ArrayList<String> getTransDate(){

        ArrayList<String> arrayDate = new ArrayList();

        Cursor cursor = myDB.rawQuery("SELECT DISTINCT tgl FROM transaksi ORDER BY tgl DESC",null);

        if(cursor.moveToFirst()){

            do{

                arrayDate.add(cursor.getString(0));

            }while(cursor.moveToNext());
        }

        return arrayDate;
    }

    public ArrayList<HashMap<String, String>> getTransListByDate(String iDate){

        ArrayList<HashMap<String, String>> arrayTrans = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT * FROM transaksi INNER JOIN kategori ON transaksi.id_kat = kategori.id_kat";
                sql+=" WHERE tgl='"+iDate+"' ORDER BY id_trans DESC;";

        Cursor cursor = myDB.rawQuery(sql,null);

        if(cursor.moveToFirst()){

            do {

                HashMap<String, String> hashMapTrans = new HashMap<String, String>();

                hashMapTrans.put("id_trans", cursor.getString(0));
                hashMapTrans.put("tgl", cursor.getString(1));
                hashMapTrans.put("tipe", cursor.getString(2));
                hashMapTrans.put("id_kat", cursor.getString(3));
                hashMapTrans.put("desk", cursor.getString(4));
                hashMapTrans.put("nominal", cursor.getString(5));
                hashMapTrans.put("kat", cursor.getString(8));
                hashMapTrans.put("ic_kat", cursor.getString(10));

                arrayTrans.add(hashMapTrans);

            }while (cursor.moveToNext());
        }

        return arrayTrans;
    }

    public ArrayList<HashMap<String, String>> getCategoryByParam(String param){

        ArrayList<HashMap<String, String>> arrayCategory = new ArrayList<>();

        String sql = "SELECT * FROM kategori WHERE " + param;
            sql += " ORDER BY id_kat";

        Cursor cursor = myDB.rawQuery(sql, null);

        if(cursor.moveToFirst()){

            do{

                HashMap<String, String> hashMapCategory = new HashMap<>();

                hashMapCategory.put("id", cursor.getString(0));
                hashMapCategory.put("desk", cursor.getString(1));
                hashMapCategory.put("type", cursor.getString(2));
                hashMapCategory.put("cat_ico", cursor.getString(3));

                arrayCategory.add(hashMapCategory);

            }while(cursor.moveToNext());
        }

        return arrayCategory;
    }

    public void saveTransaksi(String tgl,String  tipe, int id_kategori,
                              String keterangan, int nominal){

        String sql = "INSERT INTO transaksi('tipe','id_kat','desk','nominal','tgl') " +
                "VALUES('"+tipe+"',"+id_kategori+","+"'"+keterangan+"',"+nominal+","+"'"+tgl+"')";

        myDB.execSQL(sql);
    }

    public void updateTransaksi(String tgl,String  tipe, int id_kategori,
                                String keterangan, int nominal, int id_trans){

        String sql = "UPDATE transaksi SET " +
                        "tipe='"+tipe+"'," +
                        "id_kat="+id_kategori+"," +
                        "desk='"+keterangan+"'," +
                        "nominal="+nominal+"," +
                        "tgl='"+tgl+"' " +
                    "WHERE id_trans= '"+id_trans+"' ";

        myDB.execSQL(sql);
    }

    public void deleteTransaksi(int id_trans){

        String sql = "DELETE FROM transaksi WHERE id_trans='"+id_trans+"'";

        myDB.execSQL(sql);
    }

    public int[] getBalance(){

        int[] iAmount = new int[2];

        String sql = "select (select sum(nominal) from transaksi where tipe='i') as pemasukan, \n" +
                "\t(select sum(nominal) from transaksi where tipe='o') as pengeluaran";

        Cursor icur = myDB.rawQuery(sql,null);

        icur.moveToFirst();

        iAmount[0] = icur.getInt(0);
        iAmount[1] = icur.getInt(1);

        return iAmount;
    }

    public ArrayList<HashMap<String, String>> getBudgetByParam(String param){

        ArrayList<HashMap<String, String>> arrayBudget = new ArrayList<>();

        String sql = "SELECT angg.*,katg.nama_kat,katg.icon_kat FROM anggaran angg " +
                " INNER JOIN kategori katg ON angg.kat_angg = katg.id_kat " +
                " WHERE " + param +
                " ORDER BY id_angg DESC";

        Cursor cursor = myDB.rawQuery(sql, null);

        if(cursor.moveToFirst()){

            do{

                HashMap<String, String> hashMapBudget = new HashMap<>();

                hashMapBudget.put("id_budget", cursor.getString(cursor.getColumnIndex("id_angg")));
                hashMapBudget.put("desc", cursor.getString(cursor.getColumnIndex("nama_kat")));
                hashMapBudget.put("icon", cursor.getString(cursor.getColumnIndex("icon_kat")));
                hashMapBudget.put("id_category", cursor.getString(cursor.getColumnIndex("kat_angg")));
                hashMapBudget.put("budget_amount", cursor.getString(cursor.getColumnIndex("jml_angg")));

                arrayBudget.add(hashMapBudget);

            }while(cursor.moveToNext());
        }

        return arrayBudget;
    }

    public int getTotalAmountTransaction(int id_kategori, String startDate, String endDate){

        String sql = "SELECT sum(nominal) as jml FROM transaksi " +
                "WHERE id_kat="+id_kategori+" " +
                "   AND tgl BETWEEN '"+startDate+"' AND '"+endDate+"'";

        Cursor icur = myDB.rawQuery(sql,null);

        icur.moveToFirst();

        int totalAmount = icur.getInt(0);

        return totalAmount;
    }
}
