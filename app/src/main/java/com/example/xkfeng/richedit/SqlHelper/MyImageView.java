package com.example.xkfeng.richedit.SqlHelper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xkfeng.richedit.R;

/**
 * Created by initializing on 2018/5/25.
 */

public class MyImageView extends View {
    private Paint paint ;
    private Bitmap bitmap;

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint()  ;
        paint.setAntiAlias(true);

        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.backimage2) ;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap ==null)
        {
            Log.e("MyImageView" , "Bitmap is null") ;
            return ;
        };
        
        canvas.drawBitmap(bitmap, 0,0 ,paint);
    }
}
