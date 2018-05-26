package com.example.xkfeng.richedit.RecyclerViewPackage;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.JavaBean.EditData;
import com.example.xkfeng.richedit.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by initializing on 2018/5/18.
 */

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> implements View.OnClickListener{
    private  List<EditData> editDataList;

    private Context context ;
    public  RecyclerAdapter(List<EditData> editData)
    {
        editDataList = editData ;
        System.out.println("ZHIXINg ");
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
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent , false) ;
        MyHolder myHolder = new MyHolder(view) ;
        myHolder.listItemImage.setOnClickListener(this);
        myHolder.listItemTitle.setOnClickListener(this);
        myHolder.listItemTime.setOnClickListener(this);
        context = parent.getContext() ;
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        EditData editData = editDataList.get(position) ;
        holder.listItemTime.setText(editData.getTime());
        holder.listItemImage.setText(editData.getIsCollected());
        holder.listItemTitle.setText(editData.getData());
    }

    @Override
    public int getItemCount() {
      //  Log.i("RecyclerAdapter" , "SIZE IS " + editDataList.size())  ;
        return editDataList.size();
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.listItemImage)
        {

            Toast.makeText(context , "收藏" , Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.listItemTime)
        {

            Toast.makeText(context , "时间" , Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.listItemTitle)
        {
            Toast.makeText(context , "标题" ,Toast.LENGTH_SHORT).show();
        }
    }


}
