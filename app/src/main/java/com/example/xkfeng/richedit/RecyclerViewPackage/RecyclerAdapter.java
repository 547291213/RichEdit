package com.example.xkfeng.richedit.RecyclerViewPackage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.JavaBean.EditSql;
import com.example.xkfeng.richedit.MainActivity;
import com.example.xkfeng.richedit.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by initializing on 2018/5/18.
 */

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> implements View.OnClickListener{
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
                if (MainActivity.CURRENT_PAGE == 1)
                {
                    int position = myHolder.getAdapterPosition() ;
                    EditSql editSql = editDataList.get(position) ;
                    Log.i("RecyclerAdapter" , "THE position is " + position + " isCollected" + editSql.getIsCollected())  ;
                    if (!editSql.getIsCollected())
                    {
                        myHolder.listItemImage.setText("已收藏");
                        editSql.setCollected(true);
                        editSql.update(position+1) ;
                        editSql.save() ;
                    }
                    else if (editSql.getIsCollected()){
                        myHolder.listItemImage.setText("收藏");
                        editSql.setCollected(false);
                        editSql.update(position+1) ;
                        editSql.save() ;
                    }
                    Intent intent = new Intent("com.example.xkfeng.richedit.mainbroadcast") ;
                    intent.putExtra("action","homeFragment") ;
                    context.sendBroadcast(intent);
                }else{

                    Toast.makeText(context , "只有在首页中可以更改收藏属性",Toast.LENGTH_SHORT).show();
                }

            }
        }) ;
        myHolder.listItemTitle.setOnClickListener(this);
        myHolder.listItemTime.setOnClickListener(this);
        context = parent.getContext() ;
        return myHolder;
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

    @Override
    public void onClick(View view)
    {

        if (view.getId() == R.id.listItemTime)
        {

            Toast.makeText(context , "时间" , Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.listItemTitle)
        {
            Toast.makeText(context , "标题" ,Toast.LENGTH_SHORT).show();
        }
    }


}
