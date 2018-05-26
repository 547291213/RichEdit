package com.example.xkfeng.richedit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import java.math.RoundingMode;
import java.security.KeyStore;

/**
 * Created by initializing on 2018/5/14.
 */

public class WaveView extends View {
    private Paint paint ;
    private Paint paint1;
    private Path path ;
    private Path path1 ;
    private int waveLength ;
    private int waveHeight ;
    private int originY ;
    private int waveView_boatBitmap;
    private boolean waveView_rise ;
    private int duration ;
    private int width ;
    private int height ;
    private Bitmap mBitmap ;
    private int dx ;
    private int dy ;
    private ValueAnimator valueAnimator ;
    private Region region ;
    private float degrees ;
    private static final int BITMAP_HEIGHT = 300 ;
    private static final int BITMAP_WIDTH = 300 ;


    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context ,attrs) ;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs , R.styleable.WaveView) ;

//        waveView_boatBitmap = array.getResourceId(R.styleable.WaveView_boatBitmap , 0) ;
        waveView_rise = array.getBoolean(R.styleable.WaveView_rise , false) ;
        duration = (int)array.getDimension(R.styleable.WaveView_duration , 2000) ;
        originY = (int)array.getDimension(R.styleable.WaveView_originY , 500) ;
        waveHeight = (int) array.getDimension(R.styleable.WaveView_waveHeight , 200) ;
        waveLength = (int) array.getDimension(R.styleable.WaveView_waveleLength , 3000) ;
        System.out.println("wavelength is "+ waveLength);
        array.recycle();

        BitmapFactory.Options options = new BitmapFactory.Options() ;
        options.inSampleSize = 1 ;
//        if (waveView_boatBitmap > 0)
//        {
//            mBitmap = BitmapFactory.decodeResource(getResources() , waveView_boatBitmap , options) ;
//            mBitmap = getmBitmap(mBitmap);
//        }else {
//            mBitmap = BitmapFactory.decodeResource(getResources() , R.mipmap.point , options) ;
//        }

        mBitmap = BitmapFactory.decodeResource(getResources() , R.drawable.app_pic) ;
        mBitmap = resizeImage(mBitmap , BITMAP_WIDTH , BITMAP_HEIGHT) ;

        paint = new Paint() ;
        paint.setColor(getResources().getColor(R.color.waveColor));
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        path = new Path() ;
        path1 = new Path() ;

        paint1 = new Paint() ;

        paint1.setColor(getResources().getColor(R.color.waveColor1));
        paint1.setAlpha(50);
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.FILL_AND_STROKE);
    }

//    public Bitmap getmBitmap(Bitmap bitmap)
//    {
//        if (bitmap == null)
//        {
//            return null ;
//        }
//        try {
//            Bitmap circleBitmap =  Bitmap.createBitmap(bitmap.getWidth() , bitmap.getHeight() ,Bitmap.Config.ARGB_8888) ;
//            Canvas canvas = new Canvas(circleBitmap) ;
//            final Paint paint = new Paint() ;
//            final Rect rect = new Rect(0,0,bitmap.getWidth() , bitmap.getHeight()) ;
//            final RectF rectF = new RectF(new Rect(0,0,BITMAP_WIDTH ,bitmap.getHeight())) ;
//            float rountPx = 0.0f ;
//            //以短邊為標準
//            if (bitmap.getWidth() > bitmap.getHeight())
//            {
//                rountPx = bitmap.getHeight() / 2.0f ;
//            }else
//            {
//                rountPx = bitmap.getWidth() / 2.0f ;
//            }
//            paint.setAntiAlias(true);
//            canvas.drawARGB(0,0,0,0);
//            paint.setColor(Color.WHITE);
//            canvas.drawRoundRect(rectF , rountPx ,rountPx , paint);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)) ;
//            final Rect src = new Rect(0 , 0 , bitmap.getWidth() ,bitmap.getHeight()) ;
//            canvas.drawBitmap(bitmap ,src , rect ,paint);
//            return circleBitmap ;
//
//        }catch ( Exception e)
//        {
//            return bitmap ;
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec) ;
        height = MeasureSpec.getSize(heightMeasureSpec) ;
        if (originY == 0)
        {
            originY = height ;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPathData() ;
        setPathData1() ;
        canvas.drawPath(path ,paint);
        canvas.drawPath(path1 ,paint1);


        Rect bounds = region.getBounds() ;
       // Log.i("WAVEVIEW" , "THE BOUNDS RIGHT IS : "+mBitmap.getWidth() + "  " + mBitmap.getHeight())  ;
        Paint paint2 = new Paint() ;
        paint2.setAntiAlias(true);


        Matrix matrix = new Matrix();
        // 根据画布的中心位置旋转
        matrix.setRotate(degrees, canvas.getWidth() / 2,
                canvas.getHeight() / 2);

        canvas.setMatrix(matrix);
       // Log.i("WAVEVIEW" , "CANVAS IS :" + canvas.getWidth() + " " + width + " " + canvas.getHeight() + " " + height)  ;
        if (bounds.top>0||bounds.right>0)
        {
            if (bounds.top < originY)
            {
                canvas.drawBitmap(mBitmap,bounds.right - BITMAP_WIDTH/2, bounds.top-BITMAP_HEIGHT  ,paint2);

            }else {


                canvas.drawBitmap(mBitmap,bounds.right - BITMAP_WIDTH/2, bounds.bottom-BITMAP_HEIGHT  ,paint2);

            }
        }else {
            canvas.drawBitmap(mBitmap , width/2 - BITMAP_WIDTH/2 , originY-BITMAP_HEIGHT  , paint2);

        }


    }
    private void setPathData1() {
        path1.reset();
        int halfWave = waveLength/2 ;
        path1.moveTo(-waveLength - waveLength/3 + dx , originY + dy);
        for (int i = -waveLength ; i < width+waveLength ; i+=waveLength)
        {
            path1.rQuadTo(halfWave/2 , waveHeight/6  ,halfWave,0); //相對坐標
            path1.rQuadTo(halfWave/2 , -waveHeight/6 , halfWave,0);  //相對坐標

        }

        path1.lineTo(width , height);
        path1.lineTo(0,height);
        path1.close();

    }
    private void setPathData() {
        path.reset();
        int halfWave = waveLength/2 ;
        path.moveTo(-waveLength + dx , originY + dy);
        for (int i = -waveLength ; i < width+waveLength ; i+=waveLength)
        {
           path.rQuadTo(halfWave/2 , waveHeight/6  ,halfWave,0); //相對坐標
           path.rQuadTo(halfWave/2 , -waveHeight/6 , halfWave,0);  //相對坐標

        }

        region = new Region() ;
        float temp_width = width/2 ;
        Region clip = new Region( (int)(temp_width-0.1),0,(int)temp_width,height*2) ;
        region.setPath(path , clip) ;

        path.lineTo(width , height);
        path.lineTo(0,height);
        path.close();

    }
    public void startAnimation()
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0,1) ;
        animator.setDuration(5000) ;
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               float time = (float)animation.getAnimatedValue();
                dx = (int) ( waveLength*time);



                postInvalidate();
            }
        });
        animator.start();

    }

    public void startImageRotate()
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0,1) ;
        animator.setDuration(8000) ;
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                degrees = 90* (float) animation.getAnimatedValue() %360;
                postInvalidate();

            }
        });
        animator.start();

    }
    public  Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }
    public  Bitmap resizeImage(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;

    }
}
