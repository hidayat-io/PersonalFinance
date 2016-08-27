package com.iosoft.hidayat.personalfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentBudget extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup iView = (ViewGroup) inflater.inflate(R.layout.fragment_budget,null);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabAdd);
        fab.hide();
        return iView;
    }
}
