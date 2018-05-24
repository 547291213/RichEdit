package com.example.xkfeng.richedit.Fragment.ViewPaperFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.xkfeng.richedit.MainActivity;
import com.example.xkfeng.richedit.R;

/**
 * Created by initializing on 2018/5/24.
 */

public class ViewFargment_3 extends Fragment {
    private Button button ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_fragment3 , container ,false) ;
        button = (Button)view.findViewById(R.id.login_Btn) ;
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , MainActivity.class) ;
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view  ;
    }
}
