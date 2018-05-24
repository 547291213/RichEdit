package com.example.xkfeng.richedit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.Fragment.CollectionFragment;
import com.example.xkfeng.richedit.Fragment.HomeFragment;
import com.example.xkfeng.richedit.Fragment.TipFragment;
import com.example.xkfeng.richedit.SqlHelper.SqlClass;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1 ;
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
    private DrawerLayout mDrawerLayout;

    private SQLiteDatabase db ;
    private SqlClass sqlClass ;
    private static final String TAG = "MainActivity"  ;
    private RelativeLayout drawer_relayout ;
    private Fragment SetFragment ;


    @RequiresApi(api = Build.VERSION_CODES.M)
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

        SetFragment = getSupportFragmentManager().findFragmentById(R.id.id_right_menu) ;

        //获取照相机权限
        int check = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
        if (check!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.Manifest.permission.WRITE_EXTERNAL_STORAGE"} ,REQUEST_CODE );
        }
        else {
            Toast.makeText(MainActivity.this , "已经拥有权限" , Toast.LENGTH_SHORT).show();
        }
        init();
    }


    /*
    初始化代码
     */
    private void init() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer_relayout = (RelativeLayout)findViewById(R.id.drawer_relayout) ;

       // mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                drawer_relayout.layout(SetFragment.getView().getRight(), 0,  SetFragment.getView().getRight() + display.getWidth(), display.getHeight()+30);
            }
            @Override
            public void onDrawerOpened(View drawerView) {

            }
            @Override
            public void onDrawerClosed(View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.setText)
        {
            mDrawerLayout.openDrawer(Gravity.LEFT);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                    Gravity.LEFT);
            ;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MainActivity.this , "成功获取了权限" , Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this , "权限获取失败" ,Toast.LENGTH_LONG).show();
        }
    }
}
