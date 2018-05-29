package com.example.xkfeng.richedit.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by initializing on 2018/5/9.
 */

public class HomeFragment extends Fragment {
    private RecyclerView listView ;
    private List<EditSql> editSql ;
    private AdapterData adapterData ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.home_fragment , null) ;
        listView = (RecyclerView)view.findViewById(R.id.homeListView) ;
        init() ;

        return  view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void init()
    {
        editSql = new ArrayList<>() ;
        editSql = DataSupport.findAll(EditSql.class);

        adapterData = new AdapterData(editSql) ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) ;
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        listView.setAdapter(adapterData);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        从数据库获取数据
         */

    }
    public class AdapterData extends RecyclerAdapter {
        public AdapterData(List<EditSql> editSql) {
            super(editSql);
        }
    }
}
