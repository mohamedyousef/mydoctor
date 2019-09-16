package com.uniyapps.yadoctor.Fragments;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.uniyapps.yadoctor.Controllers.UserController;
import com.uniyapps.yadoctor.Interfaces.IUserName;
import com.uniyapps.yadoctor.R;

public class ProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView gotochat  = view.findViewById(R.id.gotochat),gotoreview = view.findViewById(R.id.goto_review);
        final TextView username = view.findViewById(R.id.user_name_profile);

        UserController.getInstance().getUserName(FirebaseAuth.getInstance().getUid(),new IUserName() {
            @Override
            public void on_result(String name) {
                if (name!=null && !name.isEmpty())
                username.setText(name+"");
                else
                    username.setText(" ");
            }
        });

        gotochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.show(getChildFragmentManager(),chatFragment.TAG);
            }
        });

        gotoreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoFragment videoFragment = new VideoFragment();
                videoFragment.show(getChildFragmentManager(),VideoFragment.TAG);
            }
        });

        gotochat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        gotoreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });
    }
}
