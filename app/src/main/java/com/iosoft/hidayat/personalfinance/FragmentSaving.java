package com.iosoft.hidayat.personalfinance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListSaving;
import com.iosoft.hidayat.personalfinance.model.Saving;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static java.lang.Integer.parseInt;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentSaving extends Fragment {

    private List<Saving> savingList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterListSaving mAdapter;
    private DBHelper myDB;
    private ArrayList<HashMap<String, String>> dataSaving;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container = (ViewGroup) inflater.inflate(R.layout.fragment_saving,null);

        getActivity().setTitle("Dana Simpanan");
        recyclerView = (RecyclerView) container.findViewById(R.id.rv_saving);
        myDB = new DBHelper(getContext());
        mAdapter = new AdapterListSaving(savingList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();

        FloatingActionButton fab = (FloatingActionButton) container.findViewById(R.id.fabAddSaving);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SavingNew.class);
                startActivity(intent);
            }
        });

        return container;
    }

    private void prepareData(){

        dataSaving = myDB.getListSaving();

        Saving saving;
        int idSaving,savedAmount;

        for (int i=0;i<dataSaving.size();i++){

            idSaving = parseInt(dataSaving.get(i).get("saving_id"));
            savedAmount = myDB.getSavingBalance(idSaving);

            saving = new Saving(parseInt(dataSaving.get(i).get("saving_id")),
                    dataSaving.get(i).get("saving_desc"),
                    parseInt(dataSaving.get(i).get("saving_target")),
                    dataSaving.get(i).get("end_date"),
                    parseInt(dataSaving.get(i).get("is_done")),
                    dataSaving.get(i).get("saving_icon"),
                    savedAmount);

            savingList.add(saving);
        }

        mAdapter.notifyDataSetChanged();
    }
}
