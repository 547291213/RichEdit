package com.example.xkfeng.richedit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xkfeng.richedit.Fragment.CollectionFragment;
import com.example.xkfeng.richedit.Fragment.HomeFragment;
import com.example.xkfeng.richedit.Fragment.SetFragemnt;
import com.example.xkfeng.richedit.Fragment.TipFragment;
import com.example.xkfeng.richedit.StaticElement.StateElement;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_WRITE = 1 ;
    private static final int REQUEST_CODE_CAMERA = 2 ;
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

    private static final String TAG = "MainActivity"  ;
    public RelativeLayout drawer_relayout ;
    private SetFragemnt setFragment ;

    private WindowManager manager ;
    private Display display ;

    private ImageView roundImage;
    private  File outputImage ;

    private UpdateDataBroadcast broadcast ;

    public static int CURRENT_PAGE = 1 ; //1首页 2收藏 3关于我们  三个页面的判断
    public static int EDIT_STATE = 1 ;  //1表示添加进入Edit界面，2表示点击列表项进入Edit界面
    private LinearLayout viewPageLineaerLayout ;

    private SearchView   searchView ;

    private FloatingActionButton floatButton ;

    private SharedPreferences preferences ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去掉标题栏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//         //设置全屏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);


        preferences = getSharedPreferences("isFirst" , MODE_PRIVATE) ;
        /*
        Careme权限申请
         */
        int check1 = checkSelfPermission(android.Manifest.permission.CAMERA) ;
        if (check1!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"android.Manifest.permission.CAMERA"} ,REQUEST_CODE_CAMERA );
        }
        else {
            // Toast.makeText(MainActivity.this , "已经拥有权限" , Toast.LENGTH_SHORT).show();
        }

        /*
        注册广播
         */
        broadcast = new UpdateDataBroadcast() ;
        IntentFilter intentFilter = new IntentFilter() ;
        intentFilter.addAction("com.example.xkfeng.richedit.mainbroadcast");
        registerReceiver(broadcast , intentFilter) ;


        /*
         FloatActionButton
         1 设置点击事件
         2 根据当前的排序方式，来动态调整Action中提示信息，以及根据不同页面调用方法
         */
        floatButton = (FloatingActionButton)findViewById(R.id.floatButton) ;
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Snackbar snackbar = Snackbar.make(v,getResources().getString(R.string.sort_way) ,Snackbar.LENGTH_LONG);

                if(StateElement.SORT_STATE == 2)
                {
                    snackbar.setAction(getResources().getString(R.string.sort_create_time) ,new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this , "按创建时间排序", Toast.LENGTH_SHORT).show();
                            StateElement.SORT_STATE = 1 ;

                            if (MainActivity.CURRENT_PAGE == 1)
                            {
                                homeFragment.init();

                            }else if (MainActivity.CURRENT_PAGE == 2)
                            {
                                collectionFragment.init();
                            }

                        }
                    });


                }else if (StateElement.SORT_STATE == 1)
                {
                    snackbar .setAction(getResources().getString(R.string.sort_letter), new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(MainActivity.this , "按标题字母排序", Toast.LENGTH_SHORT).show();
                            StateElement.SORT_STATE = 2 ;
                            if (MainActivity.CURRENT_PAGE == 1)
                            {
                                homeFragment.init();

                            }else if (MainActivity.CURRENT_PAGE == 2)
                            {
                                collectionFragment.init();
                            }

                        }
                    });


                }
                snackbar .show();
            }
        });

        /*
        SearchView
        1 注册监听器：当内容改变就做出响应
        2 响应内容：根据输入文本，动态调用homeFragment的初始化方法init(String),init()
         */
        searchView = (SearchView)findViewById(R.id.searchview) ;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //搜索框内部改变回调，newText就是搜索框里的内容
            @Override
            public boolean onQueryTextChange(String newText) {

                /*
                为空的时候调用默认的初始化方法
                 */
                if (newText.equals(""))
                {
                    homeFragment.init();
                }

                /*
                不为空的时候调用默认的查询列表显示方法
                 */
                else {
                    homeFragment.init(newText);

                }

                return true;
            }
        });


        /*
        布局初始化
         */
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //每次都初始为1
        EDIT_STATE = 1 ;

    }

    /*
        初始化代码
         */
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {


        manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE) ;
        display = manager.getDefaultDisplay() ;
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

        viewPageLineaerLayout = (LinearLayout)findViewById(R.id.viewPageLineaerLayout) ;



        frameLayout = (FrameLayout) findViewById(R.id.layout_content);

        homeFragment = new HomeFragment();
        collectionFragment = new CollectionFragment();
        tipFragment = new TipFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();


        fragmentTransaction.replace(R.id.layout_content, homeFragment);
        fragmentTransaction.commit();

        setFragment = (SetFragemnt) getSupportFragmentManager().findFragmentById(R.id.id_right_menu);
        outputImage = new File(Environment.getExternalStorageDirectory() , "header_image.jpg") ;

        roundImage = (ImageView)findViewById(R.id.round_image) ;
        if (!"null".equals(preferences.getString("image" ,"null")))
        {
            Uri imageUri = getImageUri() ;
            Bitmap bitmap = null;

            try {
                Log.i(TAG,imageUri.toString()) ;
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri)) ;

                bitmap = BitmapFactory.decodeFile(preferences.getString("image" ,"null")) ;
                roundImage.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            roundImage.setImageResource(R.mipmap.cat);
        }


        /*
         DrawerLayout
         1 注册监听器：响应抽屉拖动
         2 监听器功能：根据当前拖动的位置，动态调整主页面的显示布局 ，模拟手机版QQ 效果

         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer_relayout = (RelativeLayout)findViewById(R.id.drawer_relayout) ;

        // mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
        /*
          当在抽屉中点击图片，并且选取图片后返回时，会返回setFragment和MainActivity
          此时：drawer_relayout会移动到初始位置，并且不响应Layout方法。
          这里：作者尝试了先close抽屉，再open抽屉的方式，仍然不响应Layout方法，即：主界面不发生偏移
          经过多次尝试：
              发现通过这种事件互调的方式，可以完美解决问题

         */
        drawer_relayout.addOnLayoutChangeListener(new RelativeLayout.OnLayoutChangeListener(){
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                drawer_relayout.layout(setFragment.getView().getRight(), 0,  setFragment.getView().getRight() + display.getWidth(), display.getHeight()+30);
            }
        });


        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取屏幕的宽高
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                drawer_relayout.layout(setFragment.getView().getRight(), 0,  setFragment.getView().getRight() + display.getWidth(), display.getHeight()+30);
                //Log.i(TAG , "WIDTH " + setFragment.getView().getRight() ) ;


            }
            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i(TAG , "on OPENED " + drawer_relayout.getLeft() + " " + display.getHeight() ) ;

            }
            @Override
            public void onDrawerClosed(View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    /*
    主页面切换：首页，收藏，关于我们。底部下划线的动态移动。给用户点击切换页面的效果
    只将当前页面对应的下划线显示，其它下划线隐藏。
     */
    private void onViewPageChange(int index)
    {
        int count = viewPageLineaerLayout.getChildCount() ;
        for (int i = 0 ; i <count ; i++)
        {
            viewPageLineaerLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        viewPageLineaerLayout.getChildAt(index).setVisibility(View.VISIBLE);
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
            CURRENT_PAGE = 1 ;
            onViewPageChange(0) ;
            searchView.setVisibility(View.VISIBLE);
            floatButton.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.collectionText)
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_content , collectionFragment) ;
            fragmentTransaction.commit();
            CURRENT_PAGE = 2 ;
            onViewPageChange(1) ;
            searchView.setVisibility(View.GONE);
            floatButton.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.tipText)
        {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_content , tipFragment) ;
            fragmentTransaction.commit();
            CURRENT_PAGE = 3 ;
            onViewPageChange(2) ;
            searchView.setVisibility(View.GONE);
            floatButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_WRITE)
        {
            setFragment.onRequestPermissionsResult(requestCode , permissions , grantResults);
            Toast.makeText(MainActivity.this , "成功获取了权限" , Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == REQUEST_CODE_CAMERA && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MainActivity.this , "相机权限获取" ,Toast.LENGTH_LONG).show();
        }else
        {
            Log.i(TAG,"没有获取权限")  ;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  setFragment.onActivityResult(requestCode , resultCode ,data);
        Log.i(TAG,"返回到MainActivity") ;

     }

    public Uri getImageUri()
    {
        Uri imageUri ;
        if (Build.VERSION.SDK_INT >= 24)
        {
            imageUri = FileProvider.getUriForFile(MainActivity.this,"com.example.xkfeng.richedit.fileprovider",outputImage) ;
        }else {
            imageUri = Uri.fromFile(outputImage)  ;
        }

        return imageUri ;
    }

    /*
      BroadcastReceiver广播
      功能：用于页面的更新
      作用地方：当用户点击收藏或者取消收藏，置顶或者取消置顶，时候调用
     */
    public class UpdateDataBroadcast extends BroadcastReceiver
    {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新数据
            if ("homeFragment".equals(intent.getStringExtra("action")))
            {
                  homeFragment.init();
            }
            else if ("collectionFragment".equals(intent.getStringExtra("action")))
            {
                  collectionFragment.init();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        if (broadcast!=null)
        {
            unregisterReceiver(broadcast);
            broadcast=null;
        }

    }
}
