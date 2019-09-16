package com.uniyapps.yadoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import com.uniyapps.yadoctor.Controllers.QuestionsController;
import com.uniyapps.yadoctor.Interfaces.IAdvice;

public class AdivceActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_advice);
        final String title = getIntent().getStringExtra("title");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn =  findViewById(R.id.btn_got_question);
        final TextView adviceTxt = findViewById(R.id.suggesstions_txt);
        QuestionsController.getInstance().getAdvices(title, new IAdvice() {
            @Override
            public void on_result(String result) {
                adviceTxt.setText(result);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =  new Intent(AdivceActivity.this,QuestionsActivity.class);
                intent.putExtra("state",true);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
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
