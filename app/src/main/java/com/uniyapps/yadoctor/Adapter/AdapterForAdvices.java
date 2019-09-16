package com.uniyapps.yadoctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import com.uniyapps.yadoctor.AdivceActivity;
import com.uniyapps.yadoctor.R;

import java.util.ArrayList;

public class AdapterForAdvices extends  RecyclerView.Adapter<AdapterForAdvices.ViewAdviceholder>{

    ArrayList<String>advices ;
    Context context;

    public AdapterForAdvices(ArrayList<String>advices){
        this.advices =advices;
    }

    @NonNull
    @Override
    public ViewAdviceholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context   =  viewGroup.getContext();
        View row = LayoutInflater.from(context).inflate(R.layout.row_advices,null);
        row.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });
        return new ViewAdviceholder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdviceholder myViewHolder, int pos) {
        final String question = advices.get(pos);
        myViewHolder.txt.setText(question+"");
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

    void Load(String title){
        Intent intent = new Intent(context,AdivceActivity.class);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return advices.size();
    }


    class ViewAdviceholder extends RecyclerView.ViewHolder {

        TextView txt ;
        Button btn;

        public ViewAdviceholder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.title_advice);
            btn = itemView.findViewById(R.id.btn_advices);
        }
    }
}
