package com.example.xkfeng.richedit.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.SqlHelper.MyImageView;

/**
 * Created by initializing on 2018/5/23.
 */

public class SetFragemnt extends Fragment {

    private MyImageView imageView ;
    private Animation animation ;
    private  static boolean ANIMA_CHANGE = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_layout , container , false) ;
        imageView = (MyImageView) view.findViewById(R.id.backImageView) ;
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.image_anima) ;
      //  animation.setRepeatCount(Animation.INFINITE);
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
                ani.setRepeatMode(Animation.REVERSE);
                imageView.startAnimation(ani);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        imageView.setAnimation(animation);

        return view;

    }

}
