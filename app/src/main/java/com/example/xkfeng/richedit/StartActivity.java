package com.example.xkfeng.richedit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import org.litepal.LitePal;

/**
 * Created by initializing on 2018/5/24.
 */

public class StartActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("isFirst" , MODE_PRIVATE) ;
        String data = preferences.getString("data" , "null") ;
        //如果是第一次启动程序 则需要启动动画
        if (data.equals("null"))
        {
            LitePal.getDatabase();

            editor = (SharedPreferences.Editor) getSharedPreferences("isFirst" , MODE_PRIVATE).edit();
            editor.putString("data" , "xkfeng") ;
            editor.apply();
            Intent intent = new Intent(StartActivity.this , ViewPeperActivity.class) ;
            startActivity(intent);
        }
        //如果不是第一次启动程序，则直接启动主界面
        else
        {
            Intent intent = new Intent(StartActivity.this , MainActivity.class) ;
            startActivity(intent);

        }
        finish();

    }
}
