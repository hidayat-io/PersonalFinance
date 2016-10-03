package com.iosoft.hidayat.personalfinance;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListCategory;
import com.iosoft.hidayat.personalfinance.adapter.AdapterMenuListCategory;
import com.iosoft.hidayat.personalfinance.model.Category;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentCategory extends Fragment {

    private DBHelper myDB;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myDB = new DBHelper(container.getContext());

        container = (ViewGroup) inflater.inflate(R.layout.fragment_category,null);

        getActivity().setTitle("Kategori");

        final ArrayList<HashMap<String, String>> categData = myDB.getCategoryByParam(" is_del=0 ");
        ArrayList<Object> items = new ArrayList<>();
        String categType = "";
        int countFirstCategory=0;

        if(categData.size()>0){

            for(int i = 0;i<categData.size();i++){

                if(!categType.matches(categData.get(i).get("type"))){

                    if(categData.get(i).get("type").matches("i"))items.add("Pemasukan");
                    if(categData.get(i).get("type").matches("o"))items.add("Pengeluaran");
                }

                Category category = new Category(categData.get(i).get("id"),
                        categData.get(i).get("cat_ico"),
                        categData.get(i).get("desk"),
                        categData.get(i).get("type"));

                items.add(category);
                categoryList.add(category);

                categType = categData.get(i).get("type");

                if(categType.matches("o"))countFirstCategory++;
            }
        }

        AdapterMenuListCategory mAdapter = new AdapterMenuListCategory(items);
        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.rvListCategory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext(), OrientationHelper.VERTICAL,false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        final int finalCountFirstCategory = countFirstCategory+1;
        recyclerView.addOnItemTouchListener(new FragmentCategory.RecyclerTouchListener(getContext(), recyclerView, new FragmentCategory.ClickListener() {
            @Override
            public void onClick(View view, int posisiton) {

                if(posisiton!=0 && posisiton!= finalCountFirstCategory){

                    if(posisiton<finalCountFirstCategory){

                        posisiton--;
                    }
                    else{

                        posisiton-=2;
                    }

                    Category category = categoryList.get(posisiton);

                    Bundle conData = new Bundle();
                    conData.putString("strDataCategory", category.getDescription()+"#"+
                            category.getId()+"#"+
                            category.getIcon()+"#"+
                            category.getType());

                    Intent i = new Intent(getActivity(), CategoryNew.class);
                    i.putExtras(conData);
                    startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = (FloatingActionButton) container.findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CategoryNew.class);
                startActivity(intent);
            }
        });

        return container;
    }

    public interface ClickListener{

        void onClick(View view, int posisiton);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private FragmentCategory.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragmentCategory.ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e){

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e){

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(child != null && clickListener != null){

                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());

            if(child!=null && clickListener != null && gestureDetector.onTouchEvent(e)){

                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
