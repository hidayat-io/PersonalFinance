package com.iosoft.hidayat.personalfinance.model;

/**
 * Created by hidayat on 19/09/16.
 */
public class Transaction {

    private String trans_date, trans_desc, categ_type, categ_desc, categ_icon;
    private int trans_id, categ_id, amount;

    public Transaction(int trans_id, String trans_date, String trans_desc,
                        int categ_id, String categ_type, String categ_desc,
                        String categ_icon, int amount){

        this.trans_id = trans_id;
        this.trans_date = trans_date;
        this.trans_desc = trans_desc;
        this.categ_id = categ_id;
        this.categ_type = categ_type;
        this.categ_desc = categ_desc;
        this.categ_icon = categ_icon;
        this.amount = amount;
    }

    public int getTrans_id(){

        return trans_id;
    }

    public String getTrans_date(){

        return trans_date;
    }

    public String getTrans_desc(){

        return trans_desc;
    }

    public int getCateg_id(){

        return categ_id;
    }

    public String getCateg_type(){

        return categ_type;
    }

    public String getCateg_desc(){

        return categ_desc;
    }

    public String getCateg_icon(){

        return categ_icon;
    }

    public int getAmount(){

        return amount;
    }
}
