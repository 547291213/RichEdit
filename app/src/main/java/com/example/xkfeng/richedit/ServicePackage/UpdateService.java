package com.example.xkfeng.richedit.ServicePackage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by initializing on 2018/6/8.
   后台定时服务
   功能：用于获取当前时间，并且根据当前时间自动切换为夜间或者日间模式

 */

public class UpdateService extends Service {

    private static final String TAG = "UpdateService" ;

    /*
    必须的方法。
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("HH");
        Date curDate =  new Date(System.currentTimeMillis());
        int mHour = Integer.parseInt(formatter.format(curDate)) ;
        Log.i(TAG , "CURRENT TIME IS " + mHour) ;
        if (mHour >= 7 && mHour<=19)
        {

            Intent intent1 = new Intent("com.example.richedit.changemodecast");
            intent1.putExtra("DATE" , "MORN") ;
            intent1.addCategory("updateUimodeByService");

            sendBroadcast(intent1);
        }else {
            Intent intent1 = new Intent("com.example.richedit.changemodecast");
            intent1.putExtra("DATE" , "NIGHT") ;
            intent1.addCategory("updateUimodeByService");

            sendBroadcast(intent1);
        }

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE) ;
        int updateTime = 60 * 60 * 1000 ; //一个小时
        long triggerAtTime = SystemClock.elapsedRealtime() + updateTime ;
        Intent intent1 = new Intent(this , UpdateService.class) ;
        PendingIntent pendingIntent = PendingIntent.getService(this , 0 , intent1 , 0) ;
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime ,pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }


}
