package com.example.xkfeng.richedit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.xkfeng.richedit.Fragment.ViewPaperFragments.ViewFargment_3;
import com.example.xkfeng.richedit.Fragment.ViewPaperFragments.ViewFragment_1;
import com.example.xkfeng.richedit.Fragment.ViewPaperFragments.ViewFragment_2;

/**
 * Created by initializing on 2018/5/24.
 */

public class ViewPeperActivity extends AppCompatActivity {

    private ViewPager viewPager ;
    private Fragment[] fragments = new Fragment[]{ new ViewFragment_1() , new ViewFragment_2() , new ViewFargment_3()};
    private FragmentManager fragmentManager ;
    private LinearLayout lineaerLayoutId ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_paper_layout);

        viewPager = (ViewPager)findViewById(R.id.view_paper) ;
        lineaerLayoutId = (LinearLayout)findViewById(R.id.lineaerLayoutId) ;


        for (int i=0;i<lineaerLayoutId.getChildCount();i++)
        {
            lineaerLayoutId.getChildAt(i).setTag(i);
            lineaerLayoutId.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem((Integer) v.getTag());
                    setImage((Integer)v.getTag());
                    Toast.makeText(ViewPeperActivity.this , "点击了第" + 1 +"项",Toast.LENGTH_SHORT).show();
                }
            });
        }

        fragmentManager = getSupportFragmentManager() ;
        viewPager.setAdapter(new FragmentAdapter(fragmentManager));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setImage(position)  ;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setImage(int index)
    {
        int count = lineaerLayoutId.getChildCount() ;
        for (int i = 0 ; i<count ;i++)
        {
            lineaerLayoutId.getChildAt(i).setEnabled(true);
        }
        lineaerLayoutId.getChildAt(index).setEnabled(false);

    }
    private class  FragmentAdapter extends FragmentPagerAdapter
    {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position] ;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
