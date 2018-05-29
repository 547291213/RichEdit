package com.example.xkfeng.richedit.RecyclerViewPackage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;

import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.JavaBean.EditSql;
import com.example.xkfeng.richedit.MainActivity;
import com.example.xkfeng.richedit.R;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by initializing on 2018/5/18.
 */

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    private  List<EditSql> editDataList;//对象列表
    private Context context ;//context
    public  RecyclerAdapter(List<EditSql> editSql)
    {
        editDataList = editSql ;
    }


    public static class  MyHolder extends RecyclerView.ViewHolder
    {
        TextView listItemTime ;
        TextView listItemImage ;
        TextView listItemTitle ;
        public MyHolder(View itemView) {
            super(itemView);
            listItemImage = itemView.findViewById(R.id.listItemImage)  ;
            listItemTime = itemView.findViewById(R.id.listItemTime) ;
            listItemTitle = itemView.findViewById(R.id.listItemTitle) ;
        }

    }


    @Override
    public  MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent , false) ;
        final MyHolder myHolder = new MyHolder(view) ;

        myHolder.listItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.CURRENT_PAGE == 1 || MainActivity.CURRENT_PAGE == 2)
                {
                    int position = myHolder.getAdapterPosition() ;
                    EditSql editSql = editDataList.get(position) ;
                    Log.i("RecyclerAdapter" , "THE position is " + position + " isCollected" + editSql.getIsCollected())  ;
                    if (!editSql.getIsCollected())
                    {
                        myHolder.listItemImage.setText("已收藏");
                        editSql.setCollected(true);
                        editSql.update(editSql.getId()) ;
                        editSql.save() ;
                    }
                    else if (editSql.getIsCollected()){
                        myHolder.listItemImage.setText("收藏");
                        editSql.setCollected(false);
                        editSql.update(editSql.getId()) ;
                        editSql.save() ;
                    }
                    SendBroadCast();

                }else{

                    Toast.makeText(context , "只有在首页中可以更改收藏属性",Toast.LENGTH_SHORT).show();
                }


            }
        }) ;


        class ItemClick implements View.OnClickListener
        {
            @Override
            public void onClick(View v) {
                int position = myHolder.getAdapterPosition() ;
                Toast.makeText(context , "点击了第" + position +"项" ,Toast.LENGTH_SHORT).show();
            }
        }
        ItemClick itemClick = new ItemClick() ;
        myHolder.listItemTitle.setOnClickListener(itemClick);
        myHolder.listItemTime.setOnClickListener(itemClick);

        class ItemLongClick implements View.OnLongClickListener {
            @Override
            public boolean onLongClick(View v) {
                int position = myHolder.getAdapterPosition() ;
                showPopMenu(v,position) ;
                return  true ;
            }
        }

        ItemLongClick itemLongClick = new ItemLongClick() ;
        myHolder.listItemImage.setOnLongClickListener(itemLongClick);
        myHolder.listItemTitle.setOnLongClickListener(itemLongClick);
        myHolder.listItemTime.setOnLongClickListener(itemLongClick);



        context = parent.getContext() ;
        return myHolder;
    }

    private void SendBroadCast()
    {
        if (MainActivity.CURRENT_PAGE == 1)
        {
            Intent intent = new Intent("com.example.xkfeng.richedit.mainbroadcast") ;
            intent.putExtra("action","homeFragment") ;
            context.sendBroadcast(intent) ;
        }else if (MainActivity.CURRENT_PAGE == 2)
        {
            Intent intent = new Intent("com.example.xkfeng.richedit.mainbroadcast") ;
            intent.putExtra("action","collectionFragment") ;
            context.sendBroadcast(intent) ;
        }
    }

    @SuppressLint("RestrictedApi")
    public void showPopMenu(View view , final int pos)
    {
        final PopupMenu popupMenu = new PopupMenu(context , view) ;
        Log.i("RecyclerAdapter" , "长按了Item") ;
        popupMenu.getMenuInflater().inflate(R.menu.recycler_item , popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //移除操作
                EditSql editSql = editDataList.get(pos) ;
                DataSupport.delete(EditSql.class ,editSql.getId()) ;
                notifyItemRemoved(pos);
                editDataList.remove(pos) ;
                //发送广播
                SendBroadCast();
                return true;
            }
        });
        //使用反射，强制显示菜单图标
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
            mHelper.setForceShowIcon(true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //popupMenu.setGravity(Gravity.CENTER_HORIZONTAL);
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(context , "关闭了菜单" , Toast.LENGTH_SHORT).show();
//            }
//        });
        popupMenu.show();
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        EditSql  editSql = editDataList.get(position) ;
        boolean isCollect = editSql.getIsCollected() ;
        Log.i("TAG" , "isCollect is " + isCollect)  ;
        String collect ;
        if (!isCollect)
        {
            collect="收藏" ;
        }else {
            collect="已收藏" ;
        }
        holder.listItemTime.setText(editSql.getCreate_time());
        holder.listItemImage.setText(collect);
        holder.listItemTitle.setText(editSql.getTitle());


    }

    @Override
    public int getItemCount() {
        //  Log.i("RecyclerAdapter" , "SIZE IS " + editDataList.size())  ;
        return editDataList.size();
    }



}
