package com.uniyapps.yadoctor.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.uniyapps.yadoctor.AdvicesActivity;
import com.uniyapps.yadoctor.CameraDetectionActivity;
import com.uniyapps.yadoctor.QuestionsActivity;
import com.uniyapps.yadoctor.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         if (((AppCompatActivity)getActivity()).getSupportActionBar().isShowing()==false){
             ((AppCompatActivity)getActivity()).getSupportActionBar().show();
         }
         setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout gotoQuiz = view.findViewById(R.id.btn_go_quiz),
                gotocamera= view.findViewById(R.id.btn_a) ,
                goto_adivce = view.findViewById(R.id.btn_go_advices);

        goto_adivce.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(550);
                v.startAnimation(animation1);
                return false;
            }
        });


        gotoQuiz.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(550);
                v.startAnimation(animation1);
                return false;
            }
        });

        gotocamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(550);
                v.startAnimation(animation1);
                return false;
            }
        });

        gotoQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuestionsActivity.class));
            }
        });

        gotocamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), CameraDetectionActivity.class));
            }
        });




        goto_adivce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdvicesActivity.class));
            }
        });
    }

}
