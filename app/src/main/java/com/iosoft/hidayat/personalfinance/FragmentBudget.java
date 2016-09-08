package com.iosoft.hidayat.personalfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListBudget;
import com.iosoft.hidayat.personalfinance.model.Budget;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentBudget extends Fragment {

    private List<Budget> budgetList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterListBudget mAdapter;
    private DBHelper myDB;
    private ArrayList<HashMap<String, String>> dataBudget;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup iView = (ViewGroup) inflater.inflate(R.layout.fragment_budget,null);

        getActivity().setTitle("Anggaran");
        recyclerView = (RecyclerView) iView.findViewById(R.id.rv_budget);
        myDB = new DBHelper(getContext());
        mAdapter = new AdapterListBudget(budgetList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();

        return iView;
    }

    private void prepareData(){

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyMM", Locale.getDefault());
        SimpleDateFormat formaterTwo = new SimpleDateFormat("yyyy/MM/", Locale.getDefault());
        String paramMonth = formater.format(today);

        Budget budget;
        String param = " bulan='"+paramMonth+"' ";

        int idCategory;
        String startDate, endDate;
        int budgetUsed;

        int intEndDate = Calendar.getInstance().getActualMaximum(Calendar.DATE);
        startDate = formaterTwo.format(today)+"01";
        endDate = formaterTwo.format(today)+intEndDate;

        dataBudget = myDB.getBudgetByParam(param);

        for (int i=0;i<dataBudget.size();i++){

            idCategory = parseInt(dataBudget.get(i).get("id_category"));
            budgetUsed = myDB.getTotalAmountTransaction(idCategory, startDate, endDate);

            budget = new Budget(parseInt(dataBudget.get(i).get("id_budget")),
                        dataBudget.get(i).get("desc"),
                        dataBudget.get(i).get("icon"),
                        idCategory,
                        parseInt(dataBudget.get(i).get("budget_amount")),
                        budgetUsed);

            budgetList.add(budget);
        }

        mAdapter.notifyDataSetChanged();
    }
}
