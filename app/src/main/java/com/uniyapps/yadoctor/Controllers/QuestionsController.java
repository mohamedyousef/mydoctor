package com.uniyapps.yadoctor.Controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniyapps.yadoctor.Interfaces.IAdvice;
import com.uniyapps.yadoctor.Interfaces.IAdvices;
import com.uniyapps.yadoctor.Interfaces.IResultQuestion;
import com.uniyapps.yadoctor.Interfaces.IResultQuestions;
import com.uniyapps.yadoctor.Model.Question;

import java.util.ArrayList;

public class QuestionsController {

    private static QuestionsController instance  =null ;
    private QuestionsController(){}

    public static Question question = null;

    public static QuestionsController getInstance() {
        if (instance == null)
            instance = new QuestionsController();
        return instance;
    }
    public void loadQuestions(final IResultQuestions iResultQuestions){
        FirebaseFirestore.getInstance().collection("Quize").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Question> quizename = new ArrayList<Question>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments())
                    quizename.add(doc.toObject(Question.class));
                iResultQuestions.on_result(quizename);
            }
        });
    }
    public static String getResult(int score){
         if (score > 50)
        return "احتمالية اصابتك بالمرض كبيره";
         else if (score <= 50)
            return  "احتمالية اصابتك بالمرض ضعيفة" ;
         else
             return  "";
    }
    public void getAdvices(String title, final IAdvice result){
        FirebaseFirestore.getInstance().collection("Advice").document(title).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("advice");
                result.on_result(name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                result.on_result("تاكد من اتصالاك باالانترنت");
            }
        });
    }
    public void getQuestion(String  title, final IResultQuestions iResultQuestions){
        FirebaseFirestore.getInstance().collection("Quize")
                .orderBy(title).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Question> quizename = new ArrayList<Question>();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments())
                    quizename.add(doc.toObject(Question.class));
                iResultQuestions.on_result(quizename);
            }
        });
    }
    public void loadAdvices(final IAdvices Result){
        FirebaseFirestore.getInstance().collection("Advice").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String>advices = new ArrayList<>();
                for (DocumentSnapshot title : queryDocumentSnapshots.getDocuments()){
                    advices.add(title.getId());
                }
                Result.on_result(advices);
            }
        });
    }

    public void getDieaseQuestion(final IResultQuestion iResultQuestions) {
         FirebaseFirestore.getInstance().collection("Melanoma").document("quiz")
         .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
             @Override
             public void onSuccess(DocumentSnapshot documentSnapshot) {
                 iResultQuestions.on_result(documentSnapshot.toObject(Question.class));
             }
         });
    }
}
