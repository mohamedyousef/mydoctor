package com.uniyapps.yadoctor.Fragments;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
import com.uniyapps.yadoctor.AdivceActivity;
import com.uniyapps.yadoctor.Controllers.QuestionsController;
import com.uniyapps.yadoctor.MainActivity;
import com.uniyapps.yadoctor.Model.Question;
import com.uniyapps.yadoctor.R;

import java.util.ArrayList;

public class QuestionsFragment extends DialogFragment {

    public static String TAG = "FullScreenDialog";

    int score = 0;
    int totalQuetions = 0;
    int index = 0;

    TextView fadeTextView , indextxt;
    TextView totaltxt;
    boolean state;

    // btn and animation text and button
    // increase x =  abs(100 / totalquetions );
    ArrayList<String>questions = new ArrayList<>();
    Question question;
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
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // INITLI        state = getArguments().getBoolean("state",false);ZATION

        question = QuestionsController.question;
        fadeTextView = view.findViewById(R.id.title_question_);
        indextxt = view.findViewById(R.id.index);
        totaltxt = view.findViewById(R.id.total);
        yes = view.findViewById(R.id.yes);
        no  = view.findViewById(R.id.no);

         this.questions = question.getQuizes();
         Toolbar toolbar = view.findViewById(R.id.toolbar);
         toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
         toolbar.setTitle(question.getTitle()+"");

         totalQuetions =  question.getQuizes().size();
         final int x = 100 / totalQuetions;  // = get score range
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
                 score += x;
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
            showResult(QuestionsController.getResult(score),state);
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

    private void showResult(String title,boolean is_advice_activity){
        View v = getActivity().getLayoutInflater().inflate(R.layout.the_result,null);
        final AlertDialog alert = new AlertDialog.Builder(getActivity()).setView(v).setCancelable(false).create();

        TextView txt = v.findViewById(R.id.title_questions_id);
        txt.setText(title);

        Button home = v.findViewById(R.id.btn_home_page) , again = v.findViewById(R.id.btn_test_again) ,
                goto_advice = v.findViewById(R.id.btn_goto_advice);

        if (!is_advice_activity) {
            goto_advice.setVisibility(View.VISIBLE);
            goto_advice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    Intent intent = new Intent(getActivity(), AdivceActivity.class);
                    dismiss();
                    intent.putExtra("title", question.getTitle());
                    startActivity(intent);
                }
            });

            goto_advice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(600);
                    v.startAnimation(animation1);
                    return false;
                }
            });
        }
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                getActivity().finish();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        home.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        again.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                alert.dismiss();
                Reload();
            }
        });
        alert.show();
    }
    private void Reload(){
        Fragment currentFragment = this;
      //  getFragmentManager().findFragmentById(R>id)
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();

        index = 0;
        score = 0;
    }

}
