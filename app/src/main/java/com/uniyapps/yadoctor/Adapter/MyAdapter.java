package com.uniyapps.yadoctor.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.uniyapps.yadoctor.Controllers.QuestionsController;
import com.uniyapps.yadoctor.Fragments.QuestionsFragment;
import com.uniyapps.yadoctor.Model.Question;
import com.uniyapps.yadoctor.R;

import java.util.ArrayList;


public class MyAdapter extends  RecyclerView.Adapter<myViewHolder>  {

    Context context ;
    public ArrayList<Question>questions ;
    boolean state = false;


    FragmentManager fragmentManager;
    public MyAdapter(ArrayList<Question>questions, FragmentManager manager,boolean st){
        this.questions = questions;
        this.fragmentManager = manager;
        this.state = st ;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            context   =  parent.getContext();
            View row = LayoutInflater.from(context).inflate(R.layout.row_quetions,null);
            row.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(600);
                    v.startAnimation(animation1);
                    return false;
                }
            });

        return new myViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int pos) {
        final Question question = questions.get(pos);
        myViewHolder.txt.setText(question.getTitle()+"");
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load(question);
            }
        });

        myViewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load(question);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    private void Load(Question question){
        Bundle b = new Bundle();
        b.putParcelable("id", question);
        b.putBoolean("state",state);
        QuestionsFragment questionsFragment = new QuestionsFragment();
        questionsFragment.setArguments(b);
        questionsFragment.show(fragmentManager,QuestionsFragment.TAG);

        QuestionsController.question = question;
    }
}

class myViewHolder extends RecyclerView.ViewHolder{
    public TextView txt;
    public Button btn;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        txt = itemView.findViewById(R.id.title_quetions);
        btn = itemView.findViewById(R.id.btn_questions);
    }
}