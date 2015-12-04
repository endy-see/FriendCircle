package com.zhym.myapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhym.myapp.R;
import com.zhym.myapp.function.NewTecActivity;
import com.zhym.myapp.function.SettingInfo;
import com.zhym.myapp.function.UserInfoActivity;

/**
 * Created by lenovo on 2015/9/4.
 */
public class Fragment4 extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment4, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.rl_userInfo).setOnClickListener(this);
        getView().findViewById(R.id.rl_set).setOnClickListener(this);
        getView().findViewById(R.id.rl_growKnowledge).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.rl_userInfo:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.rl_set:
                startActivity(new Intent(getActivity(), SettingInfo.class));
                break;
            case R.id.rl_growKnowledge:
                startActivity(new Intent(getActivity(), NewTecActivity.class));
            default:
                break;
        }
    }
}
