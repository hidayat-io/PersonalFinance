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
 * Created by hidayat on 19/09/16.
 */
public class AdapterMenuListCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> items;
    private final int HEADER = 0, DETAIL = 1;

    public AdapterMenuListCategory(List<Object> items){

        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){

            case HEADER:
                View vHeader = inflater.inflate(R.layout.category_header_list,parent,false);
                viewHolder = new ViewHolderHeader(vHeader);
                break;
            case DETAIL:
                View vDetail = inflater.inflate(R.layout.category_detail_list,parent,false);
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
                vHeader.getTxvHeaderDesc().setText((CharSequence) items.get(position));
                break;

            case DETAIL:

                Category category = (Category) items.get(position);

                ViewHolderDetail vDetail = (ViewHolderDetail) holder;
                vDetail.getTxvCateg().setText(category.getDescription());

                ImageView imgCateg = vDetail.getImgCateg();
                Context mContext = imgCateg.getContext();

                int imageResource = mContext.
                        getResources().getIdentifier(category.getIcon(),"drawable",
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

        if(items.get(position) instanceof Category){

            return DETAIL;
        }
        else if(items.get(position) instanceof String){

            return HEADER;
        }
        return -1;
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder{

        private TextView txvHeaderDesc;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            txvHeaderDesc = (TextView) itemView.findViewById(R.id.txvCategHeader);
        }

        public TextView getTxvHeaderDesc(){

            return txvHeaderDesc;
        }

        public void setTxvHeaderDesc(TextView txvHeaderDesc){

            this.txvHeaderDesc = txvHeaderDesc;
        }
    }

    public class ViewHolderDetail extends RecyclerView.ViewHolder{

        private ImageView imgCateg;
        private TextView txvCateg;

        public ViewHolderDetail(View itemView) {
            super(itemView);
            imgCateg = (ImageView) itemView.findViewById(R.id.imgCateg);
            txvCateg = (TextView) itemView.findViewById(R.id.txvCategDesc);
        }

        public ImageView getImgCateg(){

            return imgCateg;
        }

        public TextView getTxvCateg(){

            return txvCateg;
        }
    }
}
