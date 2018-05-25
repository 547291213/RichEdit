package com.example.xkfeng.richedit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.SqlHelper.SqlClass;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by initializing on 2018/5/18.
 */

public class EditActivity extends AppCompatActivity{
    private SQLiteDatabase db ;
    private SqlClass sqlClass ;
    private static final int USER_ID_DATA = 1 ;
    private static final String TAG = "EditActivity" ;
    private TextView editingText , backText , finishText ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_layout);
        /*
        隐藏顶部标题栏
         */
        ActionBar actionBar = getSupportActionBar() ;
        if (actionBar != null)
        {
            actionBar.hide();
        }

        sqlClass = new SqlClass(this , SqlClass.TABLE_NAME+".db") ;
        db = sqlClass.getWritableDatabase() ;

        editingText = (TextView)findViewById(R.id.editingText) ;
        backText = (TextView)findViewById(R.id.backText) ;
        finishText = (TextView)findViewById(R.id.finishText);

        MyClick myClick = new MyClick();
        editingText.setOnClickListener(myClick);
        backText.setOnClickListener(myClick);
        finishText.setOnClickListener(myClick);


    }

    public class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.finishText) {
              /*
              获取输入的内容载入数据库
               */
                ContentValues cv = new ContentValues();
                cv.put(SqlClass.CONTENT, "xkfeng");
                cv.put(SqlClass.USER_ID, USER_ID_DATA);
                cv.put(SqlClass.CREATE_TIME, getTime());
                cv.put(SqlClass.UPDATE_TIME, getTime());
                db.insert(SqlClass.TABLE_NAME, null, cv);
            }

            else if (v.getId() == R.id.backText)
            {
                Log.i(TAG , "点击了back按钮") ;
                EditActivity.this.finish();
              
            }
            else if (v.getId() == R.id.editingText)
            {
                Cursor cursor = db.query(SqlClass.TABLE_NAME ,null , null ,null,
                        null,null ,null);
                while (cursor.moveToNext())
                {
                    Log.i(TAG ,"THE ID IS " + cursor.getInt(0) +
                     cursor.getString(1) + cursor.getString(2) +
                            cursor.getString(3)) ;
                }
                cursor.close();
            }
            Toast.makeText(EditActivity.this , "OnClick" , Toast.LENGTH_SHORT).show();
        }
    }

    private String getTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        Date date = new Date() ;
        String str = sdf.format(date) ;
        return str ;
    }
}
