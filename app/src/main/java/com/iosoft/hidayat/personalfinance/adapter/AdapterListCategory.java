package com.iosoft.hidayat.personalfinance.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iosoft.hidayat.personalfinance.R;
import com.iosoft.hidayat.personalfinance.model.Category;

import java.util.List;

/**
 * Created by hidayat on 30/08/16.
 */
public class AdapterListCategory extends RecyclerView.Adapter<AdapterListCategory.MyViewHolder>{

    private List <Category> categoryList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView desk, id;
        public ImageView ic_cat;

        public MyViewHolder(View view){
            super(view);
            desk = (TextView) view.findViewById(R.id.txtKatDesk);
            id = (TextView) view.findViewById(R.id.txtKatId);
            ic_cat = (ImageView) view.findViewById(R.id.imgKatIcon);
        }
    }

    public AdapterListCategory(List<Category> categoryList){

        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cat_detail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Context mContext;

        Category category = categoryList.get(position);

        mContext = holder.ic_cat.getContext();

        holder.desk.setText(category.getDescription());
        holder.id.setText(category.getId());

        int imageResource = mContext.
                getResources().getIdentifier(category.getIcon(),"drawable",
                mContext.getPackageName());
        Drawable imgKat = mContext.getResources().getDrawable(imageResource);

        holder.ic_cat.setImageDrawable(imgKat);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

}
