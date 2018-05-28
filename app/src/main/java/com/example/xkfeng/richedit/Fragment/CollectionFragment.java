package com.example.xkfeng.richedit.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.xkfeng.richedit.JavaBean.EditSql;
import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.RecyclerViewPackage.RecyclerAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by initializing on 2018/5/10.
 */

public class CollectionFragment extends Fragment {
    private RecyclerView listView ;
    private List<EditSql> editSql ;
    private AdapterData adapterData ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.home_fragment , null) ;
        listView = (RecyclerView) view.findViewById(R.id.homeListView) ;
        init() ;
        return  view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init()
    {

        editSql = new ArrayList<>() ;
        editSql = DataSupport.where("isCollected = ?" , "true")
                .find(EditSql.class) ;

        adapterData = new AdapterData(editSql) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) ;
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        listView.setAdapter(adapterData);
        //listView.setAdapter(adapter);
    }
    public class AdapterData extends RecyclerAdapter {
        public AdapterData(List<EditSql> editSql) {
            super(editSql);
        }
    }
}
