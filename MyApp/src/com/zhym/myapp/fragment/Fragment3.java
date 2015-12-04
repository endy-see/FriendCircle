package com.zhym.myapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhym.friendcircule.FriendCircleActivity;
import com.zhym.friendcircule.TestPicActivity;
import com.zhym.myapp.R;

/**
 * Created by lenovo on 2015/9/4.
 */
public class Fragment3 extends Fragment implements View.OnClickListener{
    public static final int REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment3, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.rl_friendcircule).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_friendcircule:
//                startActivityForResult(new Intent(getActivity(), FriendCircleActivity.class), REQUEST);
                startActivity(new Intent(getActivity(), FriendCircleActivity.class));
                break;
            default:
                break;

        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST && resultCode == -1) {
//            Toast.makeText(getActivity(), data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
//        }
//    }
}
