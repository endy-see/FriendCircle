package com.zhym.myapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhym.myapp.R;
import com.zhym.myapp.function.JhsActivity;


/**
 * Created by lenovo on 2015/9/4.
 */
public class Fragment1 extends Fragment {
    private Button jhs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        jhs = (Button) getView().findViewById(R.id.jhs);
        try {
            jhs.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), JhsActivity.class));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
