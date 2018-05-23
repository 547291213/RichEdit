package com.example.xkfeng.richedit;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.xkfeng.richedit.Fragment.CollectionFragment;
import com.example.xkfeng.richedit.Fragment.HomeFragment;
import com.example.xkfeng.richedit.Fragment.TipFragment;
import com.example.xkfeng.richedit.SqlHelper.SqlClass;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView setText;
    private TextView addText;
    private TextView homeText;
    private TextView collectionText;
    private TextView tipText;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private TipFragment tipFragment ;
    private CollectionFragment collectionFragment;
    private FragmentTransaction fragmentTransaction;

    private SQLiteDatabase db ;
    private SqlClass sqlClass ;
    private static final String TAG = "MainActivity"  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去掉标题栏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//         //设置全屏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setText = (TextView) findViewById(R.id.setText);
        setText.setOnClickListener(this);
        addText = (TextView) findViewById(R.id.addText);
        addText.setOnClickListener(this);
        homeText = (TextView) findViewById(R.id.homeText);
        homeText.setOnClickListener(this);
        collectionText = (TextView) findViewById(R.id.collectionText);
        collectionText.setOnClickListener(this);
        tipText = (TextView) findViewById(R.id.tipText);
        tipText.setOnClickListener(this);

        sqlClass = new SqlClass(this ,SqlClass.TABLE_NAME ) ;
        db = sqlClass.getWritableDatabase() ;

        frameLayout = (FrameLayout) findViewById(R.id.layout_content);

        homeFragment = new HomeFragment();
        collectionFragment = new CollectionFragment();
        tipFragment = new TipFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
 //       fragmentTransaction.add(R.id.layout_content, collectionFragment);
//        fragmentTransaction.hide(collectionFragment) ;
        fragmentTransaction.add(R.id.layout_content, homeFragment);
        fragmentTransaction.commit();
    }

    /*
    初始化代码
     */
    private void init() {

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.setText)
        {

        }
        else if (v.getId() == R.id.addText)
        {

            Log.e(TAG, "click addText") ;
            Intent intent = new Intent(MainActivity.this , EditActivity.class) ;
            startActivity(intent);
        }
        else if (v.getId() == R.id.homeText)
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_content , homeFragment) ;
            fragmentTransaction.commit();
        }
        else if (v.getId() == R.id.collectionText)
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_content , collectionFragment) ;
            fragmentTransaction.commit();
        }
        else if (v.getId() == R.id.tipText)
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_content , tipFragment) ;
            fragmentTransaction.commit();
        }
    }
}
