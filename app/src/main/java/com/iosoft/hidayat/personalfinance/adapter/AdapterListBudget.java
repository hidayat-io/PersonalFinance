package com.iosoft.hidayat.personalfinance.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iosoft.hidayat.personalfinance.BudgetNew;
import com.iosoft.hidayat.personalfinance.R;
import com.iosoft.hidayat.personalfinance.TransactionNew;
import com.iosoft.hidayat.personalfinance.model.Budget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hidayat on 07/09/16.
 */
public class AdapterListBudget extends RecyclerView.Adapter<AdapterListBudget.MyViewHolder> {

    private List<Budget> budgetList;


    public AdapterListBudget(List<Budget> budgetList){

        this.budgetList = budgetList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ProgressBar pb_budget;
        public ImageView ic_budget;
        public TextView txtBudgetDesc, txtBudgetUsed, txtBudgetAmount;
        public Budget iBudget;

        public MyViewHolder(View itemView) {
            super(itemView);
            ic_budget = (ImageView) itemView.findViewById(R.id.imgIconBudgetCategory);
            txtBudgetDesc = (TextView) itemView.findViewById(R.id.txtBudgetDesc);
            txtBudgetUsed = (TextView) itemView.findViewById(R.id.txtBudgetUsed);
            txtBudgetAmount = (TextView) itemView.findViewById(R.id.txtBudgetAmount);
            pb_budget = (ProgressBar) itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ArrayList iBudgetList = new ArrayList();
//                    iBudgetList.add(iBudget);

                    Intent i = new Intent(v.getContext(), BudgetNew.class);
                    i.putExtra("budget", iBudget);
                    v.getContext().startActivity(i);
                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Context mContext;

        Budget budget = budgetList.get(position);

        mContext = holder.ic_budget.getContext();

        DecimalFormat myFormat = new DecimalFormat("###,###");
        int budgetAmount = budget.getBudgetAmount();
        int budgetUsed = budget.getBudgetUsed();
        int percentUsed;

        if(budgetUsed>=budgetAmount){

            percentUsed=100;

            //set progress bar color to RED
            holder.pb_budget.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else{

            percentUsed=(budgetUsed*100)/budgetAmount;

            //set progress bar color to BLUE
            holder.pb_budget.getProgressDrawable().setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        }

        holder.txtBudgetDesc.setText(budget.getDesc());
        holder.txtBudgetAmount.setText(myFormat.format(budgetAmount));
        holder.txtBudgetUsed.setText(myFormat.format(budgetUsed));
        holder.pb_budget.setProgress(percentUsed);
        holder.iBudget = budget;


        int imageResource = mContext.getResources().getIdentifier(budget.getIcon(), "drawable", mContext.getPackageName());
        Drawable imgIcon = mContext.getResources().getDrawable(imageResource);

        holder.ic_budget.setImageDrawable(imgIcon);
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }
}
