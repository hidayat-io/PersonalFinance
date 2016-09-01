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

    public DBHelper(Context context){

        openDatabase(context);
    }

    public void openDatabase(Context context){

        myDB = context.openOrCreateDatabase("pfDB.db", Context.MODE_PRIVATE, null);

        //create table transaksi
        myDB.execSQL("CREATE TABLE IF NOT EXISTS transaksi " +
                "( id_trans INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " tgl TEXT, " +
                " tipe TEXT, " +
                " id_kat INT, " +
                " desk TEXT,  " +
                " nominal INT, " +
                " id_simp INT); ");

        //create table kategori
        myDB.execSQL("CREATE TABLE IF NOT EXISTS kategori " +
                "( id_kat INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nama_kat TEXT, " +
                " tipe_trans TEXT, " +
                " icon_kat TEXT ); ");

        //sample data
        //-- sample transaksi
//        myDB.execSQL("DELETE FROM transaksi");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','id_simp','tgl') values('o',10,'Simpanan Pembaruan SIM',10000,1,'2016/08/01')");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','tgl') values('o',1,'Dinner',20000,'2016/08/01')");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','tgl') values('o',2,'Tagihan Firstmedia Juli',500000,'2016/08/01')");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','tgl') values('o',3,'Kondangan Si Fulan',90000,'2016/08/02')");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','tgl') values('o',1,'Makan Siang',10000,'2016/08/02')");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','tgl') values('o',2,'Internet Indosat',75000,'2016/08/03')");
//        myDB.execSQL("insert into transaksi('tipe','id_kat','desk','nominal','tgl') values('o',3,'Kado for Si Fulanah',20000,'2016/08/03')");

        //-- sample kategori
        myDB.execSQL("DELETE FROM kategori");
        myDB.execSQL("INSERT INTO kategori values('1','Makanan & Minuman','o','ic_cat_food')");
        myDB.execSQL("INSERT INTO kategori values('2','Tagihan & Utilitas','o','ic_cat_bill')");
        myDB.execSQL("INSERT INTO kategori values('3','Teman & Kekasih','o','ic_cat_friend')");
        myDB.execSQL("INSERT INTO kategori values('4','Gaji','i','ic_cat_salary')");
        myDB.execSQL("INSERT INTO kategori values('10','Simpanan','o','ic_saving')");

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
}
