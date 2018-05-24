package com.example.xkfeng.richedit.Fragment.ViewPaperFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xkfeng.richedit.R;

/**
 * Created by initializing on 2018/5/24.
 */

public class ViewFragment_1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_fragment1 , container , false) ;
        return view;

    }
}
