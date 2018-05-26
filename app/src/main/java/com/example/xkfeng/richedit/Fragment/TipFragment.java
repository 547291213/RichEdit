package com.example.xkfeng.richedit.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xkfeng.richedit.R;
import com.example.xkfeng.richedit.WaveView;

/**
 * Created by initializing on 2018/5/10.
 */

public class TipFragment extends Fragment {

    private WaveView waveView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View inflater1 = getLayoutInflater().inflate(R.layout.wave_view_layout , null) ;
        waveView = (WaveView)inflater1.findViewById(R.id.waveViewId) ;
        waveView.startAnimation();
        waveView.startImageRotate();
        return inflater1 ;
    }
}
