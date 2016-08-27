package com.iosoft.hidayat.personalfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;
import com.iosoft.hidayat.personalfinance.adapter.AdapterTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentTrans extends Fragment {

    private DBHelper myDb;
    private List<String> mDate;
    private ArrayList<HashMap<String, String>> mTrans;
    private ExpandableListView mExpandableListView;
    private AdapterTransaction mAdapter;
    private List<String> mGroups;
    private HashMap<String, List<String>> mChilds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabAdd);
        fab.show();

        ViewGroup vroot = (ViewGroup) inflater.inflate(R.layout.fragment_trans, container, false);

        //call the database
        myDb = new DBHelper(getActivity());

        prepareData();

        mAdapter = new AdapterTransaction(getActivity(), mGroups, mChilds);
        mExpandableListView = (ExpandableListView) vroot.findViewById(R.id.listTrans);

        mExpandableListView.setAdapter(mAdapter);

        for(int i=0;i<mGroups.size();i++){

            mExpandableListView.expandGroup(i);
        }

        return vroot;
    }

    private void prepareData() {

        String iDate;
        mDate = myDb.getTransDate();


        mGroups = new ArrayList<String>();
        mChilds = new HashMap<String, List<String>>();

        for(int i=0;i<mDate.size();i++){

            iDate = mDate.get(i);
            mGroups.add(iDate);

            //get detail transaction by date
            mTrans = myDb.getTransListByDate(iDate);

            List<String> transDetail = new ArrayList<String>();

            for (int x=0;x<mTrans.size();x++){

                String strDetail = mTrans.get(x).get("id_trans")+"##";
                    strDetail+=mTrans.get(x).get("tgl")+"##";
                    strDetail+=mTrans.get(x).get("tipe")+"##";
                    strDetail+=mTrans.get(x).get("id_kat")+"##";
                    strDetail+=mTrans.get(x).get("desk")+"##";
                    strDetail+=mTrans.get(x).get("nominal")+"##";
                    strDetail+=mTrans.get(x).get("kat")+"##";
                    strDetail+=mTrans.get(x).get("ic_kat");

                transDetail.add(strDetail);
            }

            mChilds.put(iDate, transDetail);


        }
    }
}
