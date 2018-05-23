package com.example.xkfeng.richedit.SqlHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by initializing on 2018/5/18.
 */

public class SqlClass extends SQLiteOpenHelper {

    private Context context ;
    public static final String TABLE_NAME = "notes" ;
    public static final String CONTENT = "content"  ;
    public static final String ID = "_id" ;
    public static String CREATE_TIME = "create_time";
    public static String UPDATE_TIME = "update_time";
    public static String USER_ID = "user_id" ;

    public SqlClass(Context context, String name) {
        super(context, name, null, (int) 1.0);
        this.context = context ;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL("CREATE TABLE " + TABLE_NAME + "("
           + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
           + CONTENT + " VERCHAR(255) ,"
           + CREATE_TIME + " TEXT ,"
           + UPDATE_TIME + " TEXT ,"
           + USER_ID + " INTEGER "
                   + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Toast.makeText(context , "数据库版本更新" ,Toast.LENGTH_SHORT).show();
    }
}
