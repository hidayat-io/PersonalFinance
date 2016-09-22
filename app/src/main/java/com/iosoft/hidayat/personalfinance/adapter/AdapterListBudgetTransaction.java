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
import com.iosoft.hidayat.personalfinance.model.Transaction;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by hidayat on 19/09/16.
 */
public class AdapterListBudgetTransaction extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> items;
    private final int HEADER = 0, DETAIL = 1;

    public AdapterListBudgetTransaction(List<Object> items){

        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){

            case HEADER:
                View vHeader = inflater.inflate(R.layout.budget_header_transaction,parent,false);
                viewHolder = new ViewHolderHeader(vHeader);
                break;
            case DETAIL:
                View vDetail = inflater.inflate(R.layout.budget_detail_transaction,parent,false);
                viewHolder = new ViewHolderDetail(vDetail);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){

            case HEADER:

                ViewHolderHeader vHeader = (ViewHolderHeader) holder;
                vHeader.getTxvDate().setText((CharSequence) items.get(position));
                break;

            case DETAIL:

                Transaction transaction = (Transaction) items.get(position);

                int transAmount = transaction.getAmount();
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
                formatter.applyPattern("#,###,###");
                String strAmount = formatter.format(transAmount);

                ViewHolderDetail vDetail = (ViewHolderDetail) holder;
                vDetail.getTxvCateg().setText(transaction.getCateg_desc());
                vDetail.getTxvDesc().setText(transaction.getTrans_desc());
                vDetail.getTxvAmout().setText(strAmount);

                ImageView imgCateg = vDetail.getImgCateg();
                Context mContext = imgCateg.getContext();

                int imageResource = mContext.
                        getResources().getIdentifier(transaction.getCateg_icon(),"drawable",
                        mContext.getPackageName());
                Drawable imgKat = mContext.getResources().getDrawable(imageResource);

                imgCateg.setImageDrawable(imgKat);
        }
    }

    @Override
    public int getItemCount() {

        return this.items.size();
    }

    @Override
    public int getItemViewType(int position){

        if(items.get(position) instanceof Transaction){

            return DETAIL;
        }
        else if(items.get(position) instanceof String){

            return HEADER;
        }
        return -1;
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder{

        private TextView txvDate;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            txvDate = (TextView) itemView.findViewById(R.id.txvDate);
        }

        public TextView getTxvDate(){

            return txvDate;
        }

        public void setTxvDate(TextView txvDate){

            this.txvDate = txvDate;
        }
    }

    public class ViewHolderDetail extends RecyclerView.ViewHolder{

        private ImageView imgCateg;
        private TextView txvCateg, txvDesc, txvAmout;

        public ViewHolderDetail(View itemView) {
            super(itemView);
            imgCateg = (ImageView) itemView.findViewById(R.id.imgCateg);
            txvCateg = (TextView) itemView.findViewById(R.id.txvCateg);
            txvDesc = (TextView) itemView.findViewById(R.id.txvDesc);
            txvAmout = (TextView) itemView.findViewById(R.id.txvAmount);
        }

        public ImageView getImgCateg(){

            return imgCateg;
        }

        public TextView getTxvCateg(){

            return txvCateg;
        }

        public TextView getTxvDesc(){

            return txvDesc;
        }

        public TextView getTxvAmout(){

            return txvAmout;
        }
    }
}
