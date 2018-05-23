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

import com.example.xkfeng.richedit.JavaBean.EditData;
import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.RecyclerViewPackage.RecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by initializing on 2018/5/10.
 */

public class CollectionFragment extends Fragment {
    private RecyclerView listView ;
    private List<EditData> editData ;
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

        editData = new ArrayList<>() ;
        for (int i = 0 ; i< 20 ; i++)
        {
            EditData edit = new EditData() ;
            edit.setData("Recyclerview");
            edit.setIsCollected("收藏");
            edit.setTime("2018-05-20 17:45:65");
            editData.add(edit) ;
        }

        adapterData = new AdapterData(editData) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) ;
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        listView.setAdapter(adapterData);
        //listView.setAdapter(adapter);
    }
    public class AdapterData extends RecyclerAdapter {
        public AdapterData(List<EditData> editData) {
            super(editData);
        }
    }
}
