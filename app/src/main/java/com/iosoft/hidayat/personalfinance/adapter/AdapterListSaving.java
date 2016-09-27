package com.iosoft.hidayat.personalfinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iosoft.hidayat.personalfinance.R;
import com.iosoft.hidayat.personalfinance.SavingNew;
import com.iosoft.hidayat.personalfinance.model.Saving;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by hidayat on 07/09/16.
 */
public class AdapterListSaving extends RecyclerView.Adapter<AdapterListSaving.MyViewHolder> {

    private List<Saving> savingList;


    public AdapterListSaving(List<Saving> savingList){

        this.savingList = savingList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ProgressBar pb_saving;
        public ImageView ic_saving;
        public TextView txtSavingDesc, txtSavingSaved, txtSavingTarget;
        public String strDataSaving;

        public MyViewHolder(View itemView) {
            super(itemView);
            ic_saving = (ImageView) itemView.findViewById(R.id.imgIconSaving);
            txtSavingDesc = (TextView) itemView.findViewById(R.id.txtSavingDesc);
            txtSavingSaved = (TextView) itemView.findViewById(R.id.txtSavedAmount);
            txtSavingTarget = (TextView) itemView.findViewById(R.id.txtSavingAmount);
            pb_saving = (ProgressBar) itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(v.getContext(),SavingNew.class);
                    i.putExtra("strDataSaving",strDataSaving);
                    v.getContext().startActivity(i);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saving_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Context mContext;

        Saving saving = savingList.get(position);

        mContext = holder.ic_saving.getContext();

        DecimalFormat myFormat = new DecimalFormat("###,###");
        int savingTarget = saving.getSaving_target();
        int savingSaved = saving.getSaved_amount();
        int percentSaved;

        if(savingSaved>=savingTarget){

            percentSaved=100;

            //set progress bar color to RED
            holder.pb_saving.getProgressDrawable().setColorFilter(
                    Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else{

            percentSaved=(savingSaved*100)/savingTarget;

            //set progress bar color to BLUE
            holder.pb_saving.getProgressDrawable().setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        }

        holder.txtSavingDesc.setText(saving.getSaving_desc());
        holder.txtSavingTarget.setText(myFormat.format(savingTarget));
        holder.txtSavingSaved.setText(myFormat.format(savingSaved));
        holder.pb_saving.setProgress(percentSaved);

        holder.strDataSaving=saving.getSaving_id()+"#"+saving.getSaving_desc()+"#"+saving.getSaving_target();



        int imageResource = mContext.getResources().getIdentifier(saving.getSaving_icon(), "drawable", mContext.getPackageName());
        Drawable imgIcon = mContext.getResources().getDrawable(imageResource);

        holder.ic_saving.setImageDrawable(imgIcon);
    }

    @Override
    public int getItemCount() {
        return savingList.size();
    }
}
