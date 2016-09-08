package com.iosoft.hidayat.personalfinance.model;

/**
 * Created by hidayat on 07/09/16.
 */
public class Budget {

    private String desc, icon;
    private int id_budget;
    private int id_category;
    private int budget_amount;
    private int budget_used;

    public Budget(){ }

    public Budget(int id_budget, String desc, String icon,
                        int id_category, int budget_amount, int budget_used){

        this.id_budget = id_budget;
        this.desc = desc;
        this.icon = icon;
        this.id_category = id_category;
        this.budget_amount = budget_amount;
        this.budget_used = budget_used;
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
}
