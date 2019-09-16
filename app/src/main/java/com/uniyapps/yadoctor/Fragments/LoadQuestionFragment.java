package com.uniyapps.yadoctor.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.uniyapps.yadoctor.Controllers.QuestionsController;
import com.uniyapps.yadoctor.Interfaces.IResultQuestion;
import com.uniyapps.yadoctor.Model.Question;
import com.uniyapps.yadoctor.R;
import com.uniyapps.yadoctor.Utils.Utils;

import java.util.ArrayList;

import okhttp3.internal.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadQuestionFragment extends DialogFragment {

    public static String TAG = "FullScreenDialog";

    int totalQuetions = 0;
    int index = 0;

    TextView fadeTextView , indextxt;
    TextView totaltxt;

    ArrayList<String> questions = new ArrayList<>();
    Button yes, no;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        questions.add("هل بدات تظهر هذه الافة من فترة قريبة؟");
        questions.add("هل الافة تنزف او ترشح سوائل ؟");
        questions.add("هل الافة مؤلمة ؟");
        questions.add("هل يوجد باالافة هرش او حرقان ؟");
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fadeTextView = view.findViewById(R.id.title_question_);
        indextxt = view.findViewById(R.id.index);
        totaltxt = view.findViewById(R.id.total);
        yes = view.findViewById(R.id.yes);
        no  = view.findViewById(R.id.no);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle(  "اسئلة عن الافة الجلدية");
        totaltxt.setText(""+totalQuetions);
        loadQuestion();

        yes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                view.startAnimation(animation1);
                return false;
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestion();
                switch (index){
                    case 0:
                        Utils.AGE = 1;
                        break;
                    case  1 :
                        Utils.BLOOD =1;
                        break;
                    case 2 :
                        Utils.PAIN = 1;
                    case 3 :
                        Utils.H  = 1;
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestion();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    private void loadQuestion(){
        if (index < totalQuetions)
        {
            ShowQuestion(questions.get(index));
            ++index;
        }else
           dismiss();
    }
    private void ShowQuestion(String title){
        // make animation  to textview after change text
        // make animation to Button After change Questions
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(1100);
        fadeTextView.startAnimation(animation1);
        fadeTextView.setText(title);
        int a = index + 1;
        indextxt.setText(""+ a);
        indextxt.startAnimation(animation1);
    }

}
