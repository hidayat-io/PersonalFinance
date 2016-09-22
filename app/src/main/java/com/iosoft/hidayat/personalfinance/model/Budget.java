package com.iosoft.hidayat.personalfinance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hidayat on 07/09/16.
 */
public class Budget implements Parcelable{

    private String desc, icon, budget_month;
    private int id_budget;
    private int id_category;
    private int budget_amount;
    private int budget_used;

    public Budget(Parcel source){

        id_budget = source.readInt();
        desc = source.readString();
        icon = source.readString();
        id_category = source.readInt();
        budget_amount = source.readInt();
        budget_used = source.readInt();
        budget_month = source.readString();
    }

    public Budget(int id_budget, String desc, String icon,
                        int id_category, int budget_amount,
                        int budget_used, String budget_month){

        this.id_budget = id_budget;
        this.desc = desc;
        this.icon = icon;
        this.id_category = id_category;
        this.budget_amount = budget_amount;
        this.budget_used = budget_used;
        this.budget_month = budget_month;
    }

    public int getIdBudget(){

        return id_budget;
    }

    public void setIdBudget(int id_budget){

        this.id_budget = id_budget;
    }

    public String getDesc(){

        return desc;
    }

    public void setDesc(String desc){

        this.desc = desc;
    }

    public String getIcon(){

        return icon;
    }

    public void setIcon(String icon){

        this.icon = icon;
    }

    public int getIdCategory(){

        return id_category;
    }

    public void setIdCategory(int id_category){

        this.id_category = id_category;
    }

    public int getBudgetAmount(){

        return budget_amount;
    }

    public void setBudgetAmount(int budget_amount){

        this.budget_amount = budget_amount;
    }

    public int getBudgetUsed(){

        return budget_used;
    }

    public void setBudgetUsed(int budget_used){

        this.budget_used = budget_used;
    }

    public String getBudget_month(){

        return budget_month;
    }

    @Override
    public int describeContents() {

        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id_budget);
        dest.writeString(desc);
        dest.writeString(icon);
        dest.writeInt(id_category);
        dest.writeInt(budget_amount);
        dest.writeInt(budget_used);
        dest.writeString(budget_month);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public Budget createFromParcel(Parcel in){

            return new Budget(in);
        }

        public Budget[] newArray(int size){

            return new Budget[size];
        }
    };
}
