package com.iosoft.hidayat.personalfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentSaving extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view1 = (ViewGroup) inflater.inflate(R.layout.fragment_saving, null);
        return view1;
    }
}
