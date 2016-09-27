package com.iosoft.hidayat.personalfinance.model;

/**
 * Created by hidayat on 22/09/16.
 */
public class Saving {

    private int saving_id, saving_target, saved_amount, is_done;
    private String saving_desc, end_date,saving_icon;

    public Saving(int saving_id, String saving_desc, int saving_target, String end_date,
                    int is_done, String saving_icon, int saved_amount){

        this.saving_id = saving_id;
        this.saving_desc = saving_desc;
        this.saving_target = saving_target;
        this.end_date = end_date;
        this.is_done = is_done;
        this.saving_icon = saving_icon;
        this.saved_amount = saved_amount;
    }

    public int getSaving_id(){

        return saving_id;
    }

    public String getSaving_desc(){

        return saving_desc;
    }

    public int getSaving_target(){

        return saving_target;
    }

    public String getEnd_date(){

        return end_date;
    }

    public int getIs_done(){

        return is_done;
    }

    public String getSaving_icon(){

        return saving_icon;
    }

    public int getSaved_amount(){

        return saved_amount;
    }
}
