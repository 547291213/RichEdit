package com.example.xkfeng.richedit.Fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.RippleDrawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.xkfeng.richedit.EditActivity;
import com.example.xkfeng.richedit.JavaBean.EditSql;
import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.RecyclerViewPackage.RecyclerAdapter;
import com.example.xkfeng.richedit.StaticElement.StateElement;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mthli.knife.KnifeText;

/**
 * Created by initializing on 2018/5/9.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment" ;
    private RecyclerView recyclerview ;
    private List<EditSql> editSql ;
    private AdapterData adapterData ;
    private KnifeText knifeText ;

    private HorizontalScrollView hScrollView ;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.home_fragment , null) ;
        recyclerview = (RecyclerView)view.findViewById(R.id.homeListView) ;



        init() ;

        return  view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*
    初始化列表项。因为存在多次调用，所以单独列出为方法
    1 首先将所有置顶的列表项找出，(由排序方式，动态调节)
    2 找出非置顶的列表项，按照创建时间顺序排列，并且添加到1中得到的序列中
    3 用列表项对象去初始化Adapater，用recyclerview指定垂直布局，并且指定adapter
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init()
    {
        //默认排序：
        // 1 置顶列表项（按修改时间倒序排序）
        // 2 普通列表项（按创建时间顺序排序）
        if (StateElement.SORT_STATE == 1)
        {
       //     Log.i("HOMEGRAGMENGT" , "INIT") ;
            editSql = new ArrayList<>() ;
            editSql = DataSupport.where("istop = ?" , "1" )
                    .order("update_time desc")
                    .find(EditSql.class) ;
            List<EditSql> editSqlList = new ArrayList<>() ;
            editSqlList = DataSupport.where("istop = ?" , "0")
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

        //    Log.i("HOMEGRAGMENGT" , "INIT") ;
            editSql = new ArrayList<>() ;
            /*
              用于支持中文排序 collate localized  asc
             */
            editSql = DataSupport.where("istop = ?" , "1" )
                    .order("title  collate localized  asc")
                    .find(EditSql.class) ;
            List<EditSql> editSqlList = new ArrayList<>() ;
            editSqlList = DataSupport.where("istop = ?" , "0")
                    .order("title collate localized  asc ")
                    .find(EditSql.class);

            for (EditSql ee : editSqlList)
            {
                editSql.add(ee) ;
            }
        }
        adapterData = new AdapterData(editSql , getContext()) ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) ;
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        //new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL)
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
        recyclerview.addItemDecoration(divider);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
//        recyclerview.setNestedScrollingEnabled(true);
//        recyclerview.setHasFixedSize(true);

        recyclerview.setAdapter(adapterData);
    }

    /*
       搜索内容查找
       注：一开始作者想的是：
           因为数据库存储的内容是转换为html格式的，那么就把传入的newText用KnifeText也转换为html，然后去数据库中进行模糊查找
           ，但是后来发现一个重要BUG，在富文本和中文规范下的存储类似：&#11111  .那么当我输入11111的时候，也会显示对对应的条目，但是我在存储的时候，并没有写入11111这段内容。
            knifeText = new KnifeText(getContext()) ;
            knifeText.setText(newText);
            newText = knifeText.toHtml() ;

           问题解决方式：
           在数据库中新加一个字段，origin_content,表示原生内容，即：不论有没有进行富文本操作，都把输入的字段直接转为String字段，存入数据库。
           那么在查询的时候newText也不需要转为html格式，直接进行模糊查询就可以了。


     */
    public void init(String newText)
    {


       // Log.i("HOMEGRAGMENGT" , "INIT(STRING)") ;
        List<EditSql> editSqlList = new ArrayList<>() ;
        editSqlList = DataSupport.where("origine_content like ?" ,"%"+ newText +"%")
                .find(EditSql.class) ;
        List<EditSql> editSqlList1 = new ArrayList<>() ;
        editSqlList1 = DataSupport.where("create_time like ? and origine_content not like ?" ,
                "%"+ newText +"%","%"+ newText +"%").find(EditSql.class) ;
        Log.i("HomeFragment" , "THE TEXT IS " + newText) ;

        for (EditSql ee : editSqlList1)
        {
            editSqlList.add(ee) ;
        }
        adapterData = new AdapterData(editSqlList , getContext()) ;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) ;
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        //new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL)
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.custom_divider));
        recyclerview.addItemDecoration(divider);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(adapterData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /*
    RecyclerAdapter
     */
    public class AdapterData extends RecyclerAdapter {
        public AdapterData(List<EditSql> editSql , Context mContext) {
            super(editSql , mContext);
        }
    }

    public RecyclerView getRecyclerView()
    {
        return recyclerview ;
    }
}
