package com.iosoft.hidayat.personalfinance;


import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListCategory;
import com.iosoft.hidayat.personalfinance.model.Category;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentCategory extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container = (ViewGroup) inflater.inflate(R.layout.fragment_category,null);

        getActivity().setTitle("Kategori");
        return container;
    }

}
