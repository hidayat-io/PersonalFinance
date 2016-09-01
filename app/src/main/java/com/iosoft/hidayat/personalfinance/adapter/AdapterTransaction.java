package com.iosoft.hidayat.personalfinance.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iosoft.hidayat.personalfinance.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by hidayat on 25/08/16.
 */
public class AdapterTransaction extends BaseExpandableListAdapter{

    private Context mContext;
    private List<String> mGroups;
    private HashMap<String, List<String>> mChilds;

    public AdapterTransaction(Context context, List<String> mGroup, HashMap<String, List<String>> mChild){

        mContext = context;
        mGroups = mGroup;
        mChilds = mChild;
    }

    @Override
    public int getGroupCount() {

        return this.mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return this.mChilds.get(mGroups.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return mChilds.get(mGroups.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                                View convertView, ViewGroup parent) {

        String groupText = (String) getGroup(groupPosition);

        if(convertView==null){

            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trans_header, null);
        }

        Locale id = new Locale("in","ID");
        Date iDate = null;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formatHari = new SimpleDateFormat("EEEE", id);
        SimpleDateFormat formatTgl = new SimpleDateFormat("dd", id);
        SimpleDateFormat formatBulan = new SimpleDateFormat("MMMM yyyy", id);

        try {

            iDate = formater.parse(groupText);


        } catch (ParseException e) {

            e.printStackTrace();
        }


        TextView txtTgl = (TextView) convertView.findViewById(R.id.txvDate);
        TextView txtHari = (TextView) convertView.findViewById(R.id.txvDay);
        TextView txtBulan = (TextView) convertView.findViewById(R.id.txvMonth);


        txtTgl.setText(formatTgl.format(iDate));
        txtHari.setText(formatHari.format(iDate));
        txtBulan.setText(formatBulan.format(iDate));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                View convertView, ViewGroup parent) {

        String childText = (String) getChild(groupPosition, childPosition);
        String detailText[] = childText.split("##");

        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trans_detail, null);
        }

        TextView txtDeskripsi = (TextView) convertView.findViewById(R.id.txvDesc);
        TextView txtKategori = (TextView) convertView.findViewById(R.id.txvCateg);
        TextView txtNominal = (TextView) convertView.findViewById(R.id.txvAmount);
        ImageView imgKategori = (ImageView) convertView.findViewById(R.id.imgCateg);

        DecimalFormat myFormat = new DecimalFormat("###,###");

        int nominal = Integer.parseInt(detailText[5]);

        int imageResource = mContext.getResources().getIdentifier(detailText[7],"drawable", mContext.getPackageName());
        Drawable imgKat = mContext.getResources().getDrawable(imageResource);

        txtDeskripsi.setText(detailText[4]);
        txtKategori.setText(detailText[6]);
        txtNominal.setText(myFormat.format(nominal));
        imgKategori.setImageDrawable(imgKat);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }
}
