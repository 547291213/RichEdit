package com.example.xkfeng.richedit.RecyclerViewPackage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.EditActivity;
import com.example.xkfeng.richedit.JavaBean.EditSql;
import com.example.xkfeng.richedit.MainActivity;
import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.Utils.Utils;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by initializing on 2018/5/18.
 */

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> implements MyScrollView.IonSlidingButtonListener{
    private  List<EditSql> editDataList;//对象列表

    private Animation animation ;
    private Context context ;
    private MyScrollView mMenu = null;
    public  RecyclerAdapter(List<EditSql> editSql ,Context context)
    {
        editDataList = editSql ;
        this.context = context ;

    }


    public  class  MyHolder extends RecyclerView.ViewHolder
    {
        ImageView ImageAnimaId ;
        TextView listItemTime ;
        TextView listItemImage ;
        TextView listItemTitle ;
        MyScrollView hScrollView ;
        ViewGroup recyclerItemRelayout ;
        public TextView btn_Set;
        public TextView btn_Delete;

        @SuppressLint("WrongViewCast")
        @RequiresApi(api = Build.VERSION_CODES.M)
        public MyHolder(View itemView) {
            super(itemView);
            listItemImage = itemView.findViewById(R.id.listItemImage)  ;
            listItemTime = itemView.findViewById(R.id.listItemTime) ;
            listItemTitle = itemView.findViewById(R.id.listItemTitle) ;
            hScrollView = itemView.findViewById(R.id.hScrollView) ;
            recyclerItemRelayout = itemView.findViewById(R.id.recyclerItemRelayout) ;
            ImageAnimaId = itemView.findViewById(R.id.ImageAnimaId);
            btn_Set=itemView.findViewById(R.id.settingText) ;
            btn_Delete = itemView.findViewById(R.id.deleteText) ;
            ((MyScrollView) itemView).setSlidingButtonListener(RecyclerAdapter.this);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public  MyHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent , false) ;
        final MyHolder myHolder = new MyHolder(view) ;

        myHolder.hScrollView.setOnTouchListener(new MyScrollView.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("RECYCLERVIEW" , "RECYCLERVIEW ONTOUCH") ;
                EditSql editSql1 = editDataList.get(myHolder.getLayoutPosition()) ;
                if (editSql1.getIsTop())
                {

                    myHolder.btn_Set.setText("取消置顶");
                }else {

                    myHolder.btn_Set.setText("置顶");
                }
                return false;
            }
        });


        //左滑设置点击事件
        myHolder.btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = myHolder.getLayoutPosition();
                EditSql editSql = editDataList.get(myHolder.getLayoutPosition()) ;
                if (editSql.getIsTop())
                {
                    editSql.setTop(false);
                    editSql.setUpdate_time(EditActivity.getTime());
                    editSql.update(editSql.getId()) ;
                    editSql.save() ;

                }else{
                    editSql.setTop(true);
                    editSql.setUpdate_time(EditActivity.getTime());
                    editSql.update(editSql.getId()) ;
                    editSql.save() ;
                }
                notifyDataSetChanged();
                //发送广播
                SendBroadCast();
                // mIDeleteBtnClickListener.onSetBtnCilck(view , n);
            }
        });

        //左滑删除点击事件
        myHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = myHolder.getLayoutPosition();
                //移除操作
                EditSql editSql = editDataList.get(myHolder.getLayoutPosition()) ;
                DataSupport.delete(EditSql.class ,editSql.getId()) ;
                notifyItemRemoved(myHolder.getLayoutPosition());
                editDataList.remove(myHolder.getLayoutPosition()) ;
                //发送广播
            //    SendBroadCast();
          //      mIDeleteBtnClickListener.onDeleteBtnCilck(view, n);
            }
        });
        myHolder.listItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.CURRENT_PAGE == 1 || MainActivity.CURRENT_PAGE == 2)
                {
                    int position = myHolder.getAdapterPosition() ;
                    EditSql editSql = editDataList.get(position) ;
                  //  Log.i("RecyclerAdapter" , "THE position is " + position + " isCollected" + editSql.getIsCollected())  ;
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

                    Toast.makeText(context , "只有在首页和收藏页中可以更改收藏属性",Toast.LENGTH_SHORT).show();
                }


            }
        }) ;


        class ItemClick implements View.OnClickListener
        {
            @Override
            public void onClick(View v) {


                //设置为修改数据模式
                MainActivity.EDIT_STATE = 2 ;

                int position = myHolder.getAdapterPosition() ;
                EditSql editSql = editDataList.get(position) ;
                String data = editSql.getContent() ;
                Log.i("RecyclerAdapter" , "DATA IS " + data) ;
                int id = editSql.getId() ;
                Intent intent = new Intent(context , EditActivity.class) ;
                intent.putExtra("data" , data) ;
                intent.putExtra("id" , id) ;
                context.startActivity(intent);
               // Toast.makeText(context , "点击了第" + position +"项" ,Toast.LENGTH_SHORT).show();
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

        myHolder.recyclerItemRelayout.setOnLongClickListener(itemLongClick);

        context = parent.getContext() ;
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        EditSql  editSql = editDataList.get(position) ;
        boolean isCollect = editSql.getIsCollected() ;



        holder.recyclerItemRelayout.getLayoutParams().width = Utils.getScreenWidth(context) - 230;


        // Log.i("TAG" , "isCollect is " + isCollect)  ;
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
        /*
        获取点击的列表项是否置顶字段，如果置顶--则popmenu显示“取消置顶” 如果没有置顶---则popmenu显示“置顶”
         */
        EditSql editSql = editDataList.get(pos) ;
        if (editSql.getIsTop())
        {
            String isTop = context.getResources().getString(R.string.menu_nottop) ;
            popupMenu.getMenu().getItem(1).setTitle(isTop) ;
            popupMenu.getMenu().getItem(1).setIcon(R.drawable.icon_cancletop) ;
        }else{
            String isTop = context.getResources().getString(R.string.menu_top) ;
            popupMenu.getMenu().getItem(1).setTitle(isTop) ;
            popupMenu.getMenu().getItem(1).setIcon(R.drawable.icon_top) ;
        }
        /*
        设置点击事件
        如果：删除item点击，则删除具体的列表项
        如果：置顶或者取消置顶 item点击，则实现对应的置顶或者取消置顶操作
         */
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.removeItem)
                {
                    //移除操作
                    EditSql editSql = editDataList.get(pos) ;
                    DataSupport.delete(EditSql.class ,editSql.getId()) ;
                    notifyItemRemoved(pos);
                    editDataList.remove(pos) ;
                    //发送广播
                    //SendBroadCast();
                    return true;
                }
                else if (item.getItemId() == R.id.topItem)
                {
                    Toast.makeText(context , "置顶" ,Toast.LENGTH_SHORT).show();


                    EditSql editSql = editDataList.get(pos) ;
                    if (editSql.getIsTop())
                    {
                        editSql.setTop(false);
                        editSql.setUpdate_time(EditActivity.getTime());
                        editSql.update(editSql.getId()) ;
                        editSql.save() ;

                    }else{
                        editSql.setTop(true);
                        editSql.setUpdate_time(EditActivity.getTime());
                        editSql.update(editSql.getId()) ;
                        editSql.save() ;
                    }
                    notifyItemChanged(pos);
                    //发送广播
                    SendBroadCast();

                    return true ;
                }
                return false ;
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


    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (MyScrollView) view;


    }


    /**
     * 滑动或者点击了Item监听
     *
     * @param leftSlideView
     */
    @Override
    public void onDownOrMove(MyScrollView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断菜单是否打开
     *
     * @return
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

}
