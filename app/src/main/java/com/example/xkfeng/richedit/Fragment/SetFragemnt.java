package com.example.xkfeng.richedit.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.example.xkfeng.richedit.MainActivity;
import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.RoundImage.RoundImage;
import com.example.xkfeng.richedit.ServicePackage.UpdateService;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * 设置界面
 *
 */

public class SetFragemnt extends Fragment {

    private final static String TAG = "SetFragment" ;
    private ImageView imageView ;
    private Animation animation ;
    private  static boolean ANIMA_CHANGE = false;
    private RoundImage roundImage ;

    private SimpleAdapter simpleAdapter ;
    private List<Map<String , Object>> mapList ;
    private final String[] array_string = new String[]{"拍照","从相册选取"};
    private final int[] image_id = new int[]{R.drawable.camera , R.drawable.photo_album} ;
    private static final int TAKE_PHOTO = 1 ;
    public Uri imageUri ;
    private static final int CHOOSE_PHOTE = 2 ;
    private static final int REQUEST_CODE_WRITE = 1 ;
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor ;
    private LocationBroadcasr locationBroadcasr ;

    private TextView cityText ;
    private ImageView cityImage ;

    private NavigationView navigationView ;
    private MenuItem modeItem ;
    private UpdateUiModeBroadcast updateUiModeBroadcast ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editor = getContext().getSharedPreferences("isFirst",Context.MODE_PRIVATE).edit() ;

        sharedPreferences = getContext().getSharedPreferences("isFirst" , Context.MODE_PRIVATE) ;

        /*
        注册广播监听
         */
        updateUiModeBroadcast = new UpdateUiModeBroadcast() ;
        IntentFilter intentFilter = new IntentFilter("com.example.richedit.changemodecast") ;
        intentFilter.addCategory("updateUimodeByService");
        getContext().registerReceiver(updateUiModeBroadcast,intentFilter) ;

        locationBroadcasr = new LocationBroadcasr() ;
        IntentFilter intentFilter1 = new IntentFilter("com.example.xkfeng.locationbroadcast");
        getContext().registerReceiver(locationBroadcasr , intentFilter1) ;
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_layout , container , false) ;

        /*
        ListView列表项
         */
        mapList = new ArrayList<Map<String, Object>>() ;
        for (int i = 0 ; i< 2 ; i++)
        {
            Map<String, Object> map = new HashMap<>() ;
            map.put("Image" , image_id[i]) ;
            map.put("Text" , array_string[i]) ;
            mapList.add(map) ;
        }

        simpleAdapter = new SimpleAdapter(getContext(),mapList , R.layout.header_image_list_item ,
                new String[]{"Image" , "Text"} ,
                new int[]{R.id.headerItemImage ,R.id.headerItemText});

        /*
           用AlertDialog来设置图片
           1 相机，拍照
           2 相册，选取
         */
        roundImage = (RoundImage) view.findViewById(R.id.round_image) ;
        roundImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG , "RouunImage is clicked") ;
                /*
                当点击了图片的时候，设置MainActivity需要调用监听事件来启动RelativeLayout的Layout方法，重新布局
                 */

                MainActivity.RELATIVELAYOUT_STATE = 1 ;
                //AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(getContext())
                        .setCancelable(true)
                        .setTitle("选择照片来源")
                        .setIcon(R.drawable.app_pic)
                        .setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity() , "点击了第"+which+"项",Toast.LENGTH_SHORT).show();
                                if (which == 0)
                                {
                                    imageUri = getImageUri() ;
                                    //启动程序
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE") ;
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT , imageUri) ;
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivityForResult(intent , TAKE_PHOTO);
                                }
                                else
                                {
                                    //获取照相机权限
                                    int check = getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ;
                                    if (check!= PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[]{"android.Manifest.permission.WRITE_EXTERNAL_STORAGE"} ,REQUEST_CODE_WRITE );
                                    }
                                    else {
                                       openAlbum();
                                    }
                                }
                            }
                        })

                        .setNegativeButton("取消" , null)
                        .create()
                        .show();
            }
        });

        imageView = (ImageView) view.findViewById(R.id.backImageView) ;

//        Glide.with(getContext()).load(R.drawable.backimage2).into(imageView) ;
        imageView.setImageResource(R.drawable.backimage2);
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.image_anima) ;
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation ani ;

                if (!ANIMA_CHANGE)
                {
                    ANIMA_CHANGE = true ;
                    ani = AnimationUtils.loadAnimation(getContext(), R.anim.image_anima1);
                }
                else {
                    ANIMA_CHANGE = false ;
                    ani = AnimationUtils.loadAnimation(getContext(), R.anim.image_anima);
                }
                ani.setAnimationListener(this);
                ani.setFillAfter(true);
                ani.setInterpolator(new LinearInterpolator());
                //ani.setRepeatMode(Animation.REVERSE);
                imageView.startAnimation(ani);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        imageView.setAnimation(animation);


        cityText = (TextView)view.findViewById(R.id.city) ;
        cityText.setText("未知城市");

        cityImage = (ImageView)view.findViewById(R.id.cityImage) ;



        navigationView = (NavigationView)view.findViewById(R.id.navigationView) ;
        /*
        实践发现：不能通过view.findViewById来获取menuitem
         */
        modeItem = (MenuItem) navigationView.getMenu().getItem(1) ;
        Log.i(TAG , "THE MODEITEM IS " + modeItem.getItemId()) ;

//        navigationView.setClickable(true);
//        navigationView.setSelected(true);
//        navigationView.dispatchSetSelected(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /*
                     当点击了NavigationView  item控件的时候，设置MainActivity需要调用监听事件来启动RelativeLayout的Layout方法，重新布局
                 */
                MainActivity.RELATIVELAYOUT_STATE = 1 ;

                if (item.getItemId() == R.id.navGithub)
                {
                    Intent intent = new Intent() ;
                    intent.setAction("android.intent.action.VIEW") ;
                    Uri uri = Uri.parse("https://github.com/547291213/RichEdit") ;
                    intent.setData(uri) ;
                    startActivity(intent);
                    navigationView.setCheckedItem(R.id.navGithub);
                }
                else if (item.getItemId() == R.id.navNight)
                {

                    /*
                    对状态做出修改
                     */
                    if (MainActivity.MODE_STATE == 1)
                    {
                        MainActivity.MODE_STATE = 0 ;
                    }
                    else {
                        MainActivity.MODE_STATE = 1 ;
                    }
                     /*
                    将当前界面的背景颜色做出修改
                     */
                    UpdateUiMode() ;
                    /*
                    发送广播，去修改主界面中响应的背景颜色
                     */

                    Intent intent =  new Intent("com.example.richedit.changemodecast") ;
                    intent.addCategory("updateUimodeBySetFragment");

                    getContext().sendBroadcast(intent);
                }

                return false;
            }
        });

        return view;

    }
    /*
        功能：将当前界面的背景颜色做出修改，日间模式或者夜间模式的切换
        作用地方：
        1 用户点击了抽屉中日间/夜间模式列表项的时候调用
        2 系统后台服务根据当前时间自动切换模式的时候调用
    */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void UpdateUiMode()
    {
        navigationView.setCheckedItem(R.id.navNight);

        if (MainActivity.MODE_STATE == 0)
        {
            navigationView.setBackground(getContext().getResources().getDrawable(R.drawable.morn));
            //Glide.with(getContext()).load(R.drawable.backimage2).into(imageView) ;
            imageView.setImageResource(R.drawable.backimage2);

             modeItem.setTitle("夜间模式") ;
        }else {
            navigationView.setBackground(getContext().getResources().getDrawable(R.drawable.night));
            //Glide.with(getContext()).load(R.drawable.starrysky).into(imageView) ;
            imageView.setImageResource(R.drawable.starrysky);

            modeItem.setTitle("日间模式") ;
        }
    }

    /*
    定时更新为  日间模式或夜间模式的响应
     */
    private class UpdateUiModeBroadcast extends BroadcastReceiver
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BROADCAST" , "IN UPDATEUIMODEBROADCAST") ;
            UpdateUiMode();
        }
    }
    /*
    获取当前位置的响应
     */
    private class LocationBroadcasr extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            Glide.with(getContext()).load(R.drawable.city).into(cityImage) ;
            cityText.setText(intent.getStringExtra("city"));
        }
    }



    /*
    权限获取
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {

             openAlbum();
        }
    }

    /*
    选取照片后返回
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
//        Log.i(TAG , "返回到SetFragment"+ "requestCode is " + requestCode
//        +"  resultCode is "+resultCode);

        Log.i(TAG,"返回到SetFragment") ;
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK)
                {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri)) ;

                        editor.putString("image" , Environment.getExternalStorageDirectory()+File.separator+"header_image.jpg");
                        editor.apply();
                        roundImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e(TAG , "FILE NOT FOUND") ;
                        e.printStackTrace();
                    }

                }
                break ;
            case CHOOSE_PHOTE:
                if (resultCode == RESULT_OK)
                {
                    if (Build.VERSION.SDK_INT >= 19)
                    {
                        handleImageOnKitKat(data) ;
                    }else
                    {
                        Toast.makeText(getActivity() , "版本过老，已经不兼容" , Toast.LENGTH_SHORT).show();
                    }
                }
            default:
                break ;

        }
//        Intent intent = new Intent("com.example.xkfeng.richedit.layoutbroadcast") ;
//        getContext().sendBroadcast(intent);
    }
    /*
    获取图片Uri地址
     */
    public Uri getImageUri()
    {
        Uri imageUri ;
        File outputImage = new File(Environment.getExternalStorageDirectory() ,  "header_image.jpg") ;
        try{
            outputImage.getParentFile().mkdirs() ;
        //    Log.i(TAG, "目录创建成功") ;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try{
            if (outputImage.exists())
            {
                outputImage.delete();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24)
        {
            imageUri = FileProvider.getUriForFile(getContext(),"com.example.xkfeng.richedit.fileprovider",outputImage) ;
        }else {
            imageUri = Uri.fromFile(outputImage)  ;
        }

        return imageUri ;
    }

    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT") ;
        intent.setType("image/*") ;
        startActivityForResult(intent , CHOOSE_PHOTE);

    }

    /*
    解析从相册获取的图片
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
    {
        String imagePath = null ;
        Uri uri = data.getData() ;
        if (DocumentsContract.isDocumentUri(getContext() , uri)){
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri)  ;
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1] ; //解析出数字格式的Id
                String selection = MediaStore.Images.Media._ID + "=" + id ;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , selection) ;
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId)) ;
                imagePath = getImagePath(contentUri , null) ;
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri，则用普通的处理方式
            imagePath = getImagePath(uri , null) ;
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，直接获取图片即可
            imagePath = uri.getPath() ;
        }

        displayImage(imagePath);
    }

    private String getImagePath(Uri uri , String selection)
    {
        String path = null ;
        Cursor cursor = getContext().getContentResolver().query(uri , null , selection , null , null) ;
        if (cursor != null)
        {
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) ;
            }
            cursor.close();
        }

        return path ;
    }

    private void displayImage(String imagePath)
    {
        if (imagePath != null)
        {
            editor.putString("image" ,imagePath) ;
            editor.apply();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath) ;
            roundImage.setImageBitmap(bitmap);

        }else {
            Toast.makeText(getActivity(),"failed to get image" ,Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationBroadcasr!=null)
        {
            getContext().unregisterReceiver(locationBroadcasr);
            locationBroadcasr=null ;
        }
        if (updateUiModeBroadcast != null)
        {
            getContext().unregisterReceiver(updateUiModeBroadcast);
            updateUiModeBroadcast =null ;
        }

    }
}
