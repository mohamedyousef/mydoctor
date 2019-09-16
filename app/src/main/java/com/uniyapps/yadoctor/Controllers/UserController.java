package com.uniyapps.yadoctor.Controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.uniyapps.yadoctor.Interfaces.IUserName;
import com.uniyapps.yadoctor.Interfaces.IUserResult;

import java.util.HashMap;

public class UserController {
    private static  UserController instance = null;

    public static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }

    public  void addUser(String id,String username, final IUserResult iUserResult){

        HashMap<String,String> user = new HashMap<>();
        user.put("name",username);

        FirebaseFirestore.getInstance().collection("Users").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iUserResult.on_result(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iUserResult.on_result(false);
            }
        });
    }
    public void  checkUserId(String uid, final IUserResult iUserResult){
        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot .exists())
                    iUserResult.on_result(true);
                else
                    iUserResult.on_result(false);
            }
        });
    }
    public void  getUserName(String uid,final IUserName iUserName){
        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name =  documentSnapshot.getString("name")+"";
                iUserName.on_result(name);
            }
        });
    }

}
