package com.uniyapps.yadoctor.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uniyapps.yadoctor.Operations.Operatations;
import com.uniyapps.yadoctor.R;

import dmax.dialog.SpotsDialog;

public class FinalResultFragment extends DialogFragment {


    public static final String TAG = "FinalREsult";
    TextView msg;

    public FinalResultFragment() {
        // Required empty public constructor
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_result, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_done_black_24dp);
        toolbar.setTitle("نتيجة التشخيص");
        msg = view.findViewById(R.id.final_result);



        // detect diease
        // result if diease != lesions
        // send to server

        AlertDialog alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();

        String msg2 = getArguments().getString("msg","").trim();
        if (msg2.equals("diease")){
            alertDialog.setTitle("اتتظر حتي يتم التعرف علي المرض");
            Bitmap bitmap = getArguments().getParcelable("img");

        }else
        {
            msg.setText("غير مصاب بمرض جلدي");
        }

        alertDialog.dismiss();
    }
}
