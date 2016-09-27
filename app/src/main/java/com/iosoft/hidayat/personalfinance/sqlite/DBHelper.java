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
    private static String tbTrans, tbKategori, tbAnggaran, tbNotif, tbSimpanan;

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

        tbSimpanan = "CREATE TABLE IF NOT EXISTS simpanan " +
                    "( id_simpanan INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " deskripsi TEXT," +
                    " nominal_target INTEGER, "+
                    " tgl_akhir TEXT," +
                    " selesai INTEGER, " +
                    " icon_simpanan TEXT)";


        tbNotif = "CREATE TABLE IF NOT EXISTS notif "+
                    "(id_notif INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        "waktu_notif TEXT, "+
                        "pesan TEXT)";

        //exec create table
        myDB.execSQL(tbTrans);
        myDB.execSQL(tbKategori);
        myDB.execSQL(tbAnggaran);
        myDB.execSQL(tbSimpanan);
        myDB.execSQL(tbNotif);


        //-- data default kategori
        myDB.execSQL("DELETE FROM kategori");
        myDB.execSQL("INSERT INTO kategori values('1','Makanan & Minuman','o','ic_cat_food')");
        myDB.execSQL("INSERT INTO kategori values('2','Tagihan & Utilitas','o','ic_cat_bill')");
        myDB.execSQL("INSERT INTO kategori values('3','Teman & Kekasih','o','ic_cat_friend')");
        myDB.execSQL("INSERT INTO kategori values('4','Gaji','i','ic_cat_salary')");
        myDB.execSQL("INSERT INTO kategori values('5','Award','i','ic_cat_award')");
        myDB.execSQL("INSERT INTO kategori values('6','Hadiah','i','ic_cat_gift')");
        myDB.execSQL("INSERT INTO kategori values('7','Penjualan','i','ic_cat_sell')");
        myDB.execSQL("INSERT INTO kategori values('8','Pinjaman','i','ic_cat_debt')");
        myDB.execSQL("INSERT INTO kategori values('9','Pemasukan Lain-lain','i','ic_cat_other_in')");
        myDB.execSQL("INSERT INTO kategori values('10','Simpanan','o','ic_saving')");
        myDB.execSQL("INSERT INTO kategori values('11','Hiburan','o','ic_cat_entertainment')");
        myDB.execSQL("INSERT INTO kategori values('12','Pendidikan','o','ic_cat_education')");
        myDB.execSQL("INSERT INTO kategori values('13','Keluarga','o','ic_cat_family')");
        myDB.execSQL("INSERT INTO kategori values('14','Rumah Tangga','o','ic_cat_home')");
        myDB.execSQL("INSERT INTO kategori values('15','Investasi','o','ic_cat_invest')");
        myDB.execSQL("INSERT INTO kategori values('17','Kesehatan','o','ic_cat_medical')");
        myDB.execSQL("INSERT INTO kategori values('18','Belanja','o','ic_cat_shopping')");
        myDB.execSQL("INSERT INTO kategori values('19','Transportasi','o','ic_cat_transport')");
        myDB.execSQL("INSERT INTO kategori values('20','Travel','o','ic_cat_travel')");
        myDB.execSQL("INSERT INTO kategori values('21','Dipinjamkan','o','ic_cat_loan')");
        myDB.execSQL("INSERT INTO kategori values('22','Pengeluaran Lain-lain','o','ic_cat_other_out')");


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

    public ArrayList<HashMap<String, String>> getTransListByParam(String param){

        ArrayList<HashMap<String, String>> arrayTrans = new ArrayList<HashMap<String, String>>();

        String sql = "SELECT * FROM transaksi INNER JOIN kategori ON transaksi.id_kat = kategori.id_kat";
                sql+=" WHERE "+param+" ORDER BY tgl DESC, id_trans DESC;";

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
                              String keterangan, int nominal, int id_simpanan){

        String sql = "INSERT INTO transaksi('tipe','id_kat','desk','nominal','tgl','id_simp') " +
                "VALUES('"+tipe+"',"+id_kategori+","+"'"+keterangan+"',"+nominal+","+"'"+tgl+"',"+id_simpanan+")";

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

        String sql = "select (select sum(nominal) from transaksi where tipe='i') as pemasukan, " +
                " (select sum(nominal) from transaksi where tipe='o') as pengeluaran";

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
                hashMapBudget.put("budget_month", cursor.getString(cursor.getColumnIndex("bulan")));

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

    public void saveBudget(String bulan, int jml_angg, int kat_angg){

        String sql = "INSERT INTO anggaran(bulan,jml_angg,kat_angg)" +
                " VALUES('"+bulan+"',"+jml_angg+","+kat_angg+")";

        myDB.execSQL(sql);
    }

    public void updateBudget(int id_anggaran,  int jml_anggaran, int kat_anggaran){

        String sql = "UPDATE anggaran SET jml_angg="+jml_anggaran+", "+
                                            "kat_angg="+kat_anggaran+
                        " WHERE id_angg="+id_anggaran;

        myDB.execSQL(sql);
    }

    public void deleteBudget(int id_budget){

        String sql = "DELETE FROM anggaran WHERE id_angg='"+id_budget+"'";

        myDB.execSQL(sql);
    }

    public boolean validBudget(String bulan, int kat_angg){

        boolean validBudget;
        String sql = "SELECT * FROM anggaran WHERE bulan='"+bulan+"' AND kat_angg="+kat_angg;

        Cursor icur = myDB.rawQuery(sql,null);

        if(icur.getCount() > 0){

            validBudget = false;
        }
        else{

            validBudget = true;
        }

        return validBudget;
    }

    public int getBudgetAmount(String bulan, int kategori){

        int amount = 0;

        String sql = "SELECT jml_angg FROM anggaran WHERE bulan='"+bulan+"' AND kat_angg="+kategori;
        Cursor iCur = myDB.rawQuery(sql,null);

        if(iCur.getCount()>0){

            iCur.moveToFirst();
            amount = iCur.getInt(0);
        }

        return amount;
    }

    public void saveNotif(String notif_time, String notif_message){

        String sql = "INSERT INTO notif(waktu_notif,pesan) "+
                        "VALUES('"+notif_time+"','"+notif_message+"')";

        myDB.execSQL(sql);
    }

    public ArrayList<HashMap<String, String>> getListSaving(){

        ArrayList<HashMap<String, String>> arraySaving = new ArrayList<>();

        String sql = "SELECT * FROM simpanan ORDER BY selesai, id_simpanan DESC";

        Cursor cursor = myDB.rawQuery(sql, null);

        if(cursor.moveToFirst()){

            do{

                HashMap<String, String> hashMapSaving = new HashMap<>();

                hashMapSaving.put("saving_id", cursor.getString(cursor.getColumnIndex("id_simpanan")));
                hashMapSaving.put("saving_desc", cursor.getString(cursor.getColumnIndex("deskripsi")));
                hashMapSaving.put("saving_target", cursor.getString(cursor.getColumnIndex("nominal_target")));
                hashMapSaving.put("end_date", cursor.getString(cursor.getColumnIndex("tgl_akhir")));
                hashMapSaving.put("is_done", cursor.getString(cursor.getColumnIndex("selesai")));
                hashMapSaving.put("saving_icon", cursor.getString(cursor.getColumnIndex("icon_simpanan")));

                arraySaving.add(hashMapSaving);

            }while(cursor.moveToNext());
        }

        return arraySaving;
    }

    public int getSavingBalance(int id_saving){

        String sql  = "SELECT (SELECT IFNULL(SUM(nominal),0) FROM transaksi WHERE tipe='o' AND id_simp='"+id_saving+"')-" +
                "(SELECT IFNULL(SUM(nominal),0) FROM transaksi WHERE tipe='i' AND id_simp='"+id_saving+"') AS pengeluaran";

        Cursor iCur = myDB.rawQuery(sql,null);

        iCur.moveToFirst();

        int savingBalance = iCur.getInt(0);

        return savingBalance;
    }

    public void saveSavingPlan(String saving_description, int target_amount){

        String sql = "INSERT INTO simpanan(deskripsi,nominal_target,selesai,icon_simpanan) " +
                "VALUES ('"+saving_description+"',"+target_amount+",0,'ic_saving')";

        myDB.execSQL(sql);
    }

    public void updateSavingPlan(String saving_description, int target_amount, int id_saving){

        String sql  = "UPDATE simpanan SET deskripsi='"+saving_description+"', nominal_target="+target_amount+" WHERE id_simpanan='"+id_saving+"'";

        myDB.execSQL(sql);
    }
}
