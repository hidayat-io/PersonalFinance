package com.iosoft.hidayat.personalfinance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iosoft.hidayat.personalfinance.adapter.AdapterListCategory;
import com.iosoft.hidayat.personalfinance.model.Category;
import com.iosoft.hidayat.personalfinance.sqlite.DBHelper;
import com.iosoft.hidayat.personalfinance.ui.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hidayat on 19/08/16.
 */
public class FragmentCategoryIn extends Fragment{

    private List<Category> categoryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterListCategory mAdapter;
    private ArrayList<HashMap<String, String>> dataCategory;
    private DBHelper myDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view1 = (ViewGroup) inflater.inflate(R.layout.fragment_cat_pemasukan, null);

        recyclerView = (RecyclerView) view1.findViewById(R.id.recycler_view);

        myDb = new DBHelper(getActivity());

        mAdapter = new AdapterListCategory(categoryList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int posisiton) {
                                Category category = categoryList.get(posisiton);

                Bundle conData = new Bundle();
                conData.putString("result", category.getDescription()+"#"+
                        category.getId()+"#"+
                        category.getIcon()+"#"+
                        category.getType());

                Intent i = new Intent();
                i.putExtras(conData);
                getActivity().setResult(getActivity().RESULT_OK, i);
                getActivity().finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareCategory();

        return view1;
    }

    private void prepareCategory(){

        Category category;
        String param;

        param = " tipe_trans='i' AND id_kat != '10' "; //10 is simpanan

        dataCategory = myDb.getCategoryByParam(param);

        for (int i=0;i<dataCategory.size();i++){

            category = new Category(dataCategory.get(i).get("id"),
                    dataCategory.get(i).get("cat_ico"),
                    dataCategory.get(i).get("desk"),
                    dataCategory.get(i).get("type"));

            categoryList.add(category);
        }

        mAdapter.notifyDataSetChanged();
    }

    public interface ClickListener{

        void onClick(View view, int posisiton);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

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
