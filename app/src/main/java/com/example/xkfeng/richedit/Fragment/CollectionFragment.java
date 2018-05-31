package com.example.xkfeng.richedit.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
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
import com.example.xkfeng.richedit.StaticElement.StateElement;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by initializing on 2018/5/10.
 */

public class CollectionFragment extends Fragment {
    private RecyclerView recyclerview ;
    private List<EditSql> editSql ;
    private AdapterData adapterData ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.home_fragment , null) ;
        recyclerview = (RecyclerView) view.findViewById(R.id.homeListView) ;
        init() ;
        return  view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
    /*
    初始化列表项。因为存在多次调用，所以单独列出为方法
    1 首先将所有置顶的列表项找出，按照修改时间倒序排列
    2 找出非置顶的列表项，按照创建时间顺序排列，并且添加到1中得到的序列中
    3 用列表项对象去初始化Adapater，用recyclerview指定垂直布局，并且指定adapter

     */
    public void init()
    {
        //默认排序：
        // 1 置顶列表项（按修改时间倒序排序）
        // 2 普通列表项（按创建时间顺序排序）
        if(StateElement.SORT_STATE == 1)
        {
            editSql = new ArrayList<>() ;
            editSql = DataSupport.where("istop = ? and isCollected = ? " , "1"  ,"1")
                    .order("update_time desc")
                    .find(EditSql.class) ;
            List<EditSql> editSqlList = new ArrayList<>() ;
            editSqlList = DataSupport.where("istop = ? and isCollected = ? " , "0" , "1")
                    .order("create_time")
                    .find(EditSql.class);

            for (EditSql ee : editSqlList)
            {
                editSql.add(ee) ;
            }
        }
        //按照内容字母顺序
        // 1 置顶列表项（按照内容字母顺序）
        // 2 普通列表项（按照内容字母顺序）
        else if (StateElement.SORT_STATE == 2)
        {
            editSql = new ArrayList<>() ;
            /*

              用于支持中文排序 collate localized  asc
             */
            editSql = DataSupport.where("istop = ? and isCollected = ? " , "1"  ,"1")
                    .order("title collate localized  asc ")
                    .find(EditSql.class) ;
            List<EditSql> editSqlList = new ArrayList<>() ;
            editSqlList = DataSupport.where("istop = ? and isCollected = ? " , "0" , "1")
                    .order("title collate localized  asc ")
                    .find(EditSql.class);

            for (EditSql ee : editSqlList)
            {
                editSql.add(ee) ;
            }
        }


        adapterData = new AdapterData(editSql) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) ;
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
        //new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL)
        recyclerview.addItemDecoration(divider);

        recyclerview.setAdapter(adapterData);

    }
    public class AdapterData extends RecyclerAdapter {
        public AdapterData(List<EditSql> editSql) {
            super(editSql);
        }
    }
}
