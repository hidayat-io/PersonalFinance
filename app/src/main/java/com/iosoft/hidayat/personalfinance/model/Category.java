package com.iosoft.hidayat.personalfinance.model;

/**
 * Created by hidayat on 30/08/16.
 */
public class Category {

    private String id_cat, icon, description, type;

    public Category(){

    }

    public Category(String id_cat, String icon, String description, String type){

        this.id_cat = id_cat;
        this.icon = icon;
        this.description = description;
        this.type = type;
    }

    public String getId(){  return id_cat;  }

    public void setId(String id){ this.id_cat = id;  }

    public String getIcon(){ return icon; }

    public void setIcon(String ic_name){ this.icon = ic_name; }

    public String getDescription(){ return description; }

    public void setDescription(String desc){  this.description = desc;}

    public String getType(){ return type; }

    public void setType(String itype){ this.type = itype; }
}
