package com.uniyapps.yadoctor.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.uniyapps.yadoctor.Interfaces.IResultImage;
import com.uniyapps.yadoctor.Operations.Operatations;
import com.uniyapps.yadoctor.R;
import com.uniyapps.yadoctor.SkinDieaseDetectionActivity;
import com.uniyapps.yadoctor.Utils.App;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class CropViewFragment extends DialogFragment {


    public static final String TAG = "cropa";
    AlertDialog alertDialog ;

    //  goto second Stage check if skin has diease or no diease

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
        alertDialog = new SpotsDialog(getActivity());
        return inflater.inflate(R.layout.fragment_crop_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_done_black_24dp);
        toolbar.setTitle("قم بتحديد المنطقة التي تريد التعرف عليها");

        final CropImageView mCropView =  view.findViewById(R.id.cropImageView);
        mCropView.setBackgroundColor(0xFFFFFFFB);
        mCropView.setOverlayColor(0xAA1C1C1C);

        Bitmap bmp = getArguments().getParcelable("image");

        mCropView.setImageBitmap(bmp);
        mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
        //mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
               //mCropView.getCroppedBitmap();
               mCropView.cropAsync(new CropCallback() {
                   @Override
                   public void onSuccess(final Bitmap cropped) {
                      // Toast.makeText(getActivity(), ""+cropped.getHeight(), Toast.LENGTH_SHORT).show();
                       //mCropView.setVisibility(View.GONE);
                       // Intent intent = new Intent(getActivity(),SkinDieaseDetectionActivity.class);
                       //intent.putExtra("img",cropped);
                       //startActivity(intent);

                       Operatations.getInstance((AppCompatActivity) getActivity()).checkifskinHaveDieaseOrNo(cropped, new IResultImage() {
                           @Override
                           public void on_result(String name) {
                               alertDialog.dismiss();
                               //Toast.makeText(getActivity(), "Result :"+name, Toast.LENGTH_SHORT).show();
                               Bundle bundle = new Bundle();
                               if (name.equals("diease"))
                               {
                                   bundle.putString("msg","diease");
                                   bundle.putParcelable("img",cropped);
                               }else{
                                   bundle.putString("msg","nodiease");
                               }
                               FinalResultFragment finalResultFragment = new FinalResultFragment();
                               finalResultFragment.show(getChildFragmentManager(),FinalResultFragment.TAG);
                               finalResultFragment.setArguments(bundle);

                               dismiss();
                               close();
                           }
                       });
                   }

                   @Override
                   public void onError(Throwable e) {

                   }
               });
            }
        });
    }

    void close(){
    dismiss();
    Operatations.getInstance((AppCompatActivity) getActivity()).close();
}

}
