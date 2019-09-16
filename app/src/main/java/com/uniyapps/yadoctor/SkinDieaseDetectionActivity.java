package com.uniyapps.yadoctor;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uniyapps.yadoctor.Operations.Operatations;

import dmax.dialog.SpotsDialog;


public class SkinDieaseDetectionActivity extends AppCompatActivity {

    // check image  if skin cancer calc diameter and show some questions

    Bitmap bitmap;
    AlertDialog alertDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_diease_detection);
        alertDialog =  new SpotsDialog(this);
        bitmap =   getIntent().getParcelableExtra("img");
        //  CHECK DIEASES  OR NO DIEASES

    }
    void ShowResult(){}


}
