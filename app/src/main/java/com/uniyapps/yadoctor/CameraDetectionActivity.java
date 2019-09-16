package com.uniyapps.yadoctor;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.uniyapps.yadoctor.Fragments.CropViewFragment;
import com.uniyapps.yadoctor.Fragments.MessageFragment;
import com.uniyapps.yadoctor.Interfaces.IResultImage;
import com.uniyapps.yadoctor.Interfaces.ImageResult;
import com.uniyapps.yadoctor.Operations.IResultImageList;
import com.uniyapps.yadoctor.Operations.Operatations;
import com.uniyapps.yadoctor.Utils.Utils;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import dmax.dialog.SpotsDialog;

public class CameraDetectionActivity extends AppCompatActivity {

    CameraView cameraView ;
    boolean state =false  , state_facing=false;
    ImageView flash , facing , capture;

    Operatations operatations;
    //String MODEL_PATH = "optimized_graph.lite";
  //  AlertDialog spots  ;
    int numPhoto = 0;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    //LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();

    // not skin - not skin - skin

    //   ImageClassifier img;
    //    Classifier img;
    //   private static final int INPUT_SIZE = 224;
    //  private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detection);
        cameraView =findViewById(R.id.camera);
        facing = findViewById(R.id.facingButton);
        flash = findViewById(R.id.flashButton);
        capture = findViewById(R.id.captureButton);
      //  spots = new SpotsDialog(this);

        operatations = Operatations.getInstance(this);

        getSupportActionBar().setTitle("اجراء فحص من خلال الكاميرا");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        capture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        facing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        flash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(600);
                v.startAnimation(animation1);
                return false;
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {}
            @Override
            public void onError(CameraKitError cameraKitError) {}

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                final Bitmap bitmap = cameraKitImage.getBitmap();
                /*
                if (numPhoto <  1) {
                    Toast.makeText(CameraDetectionActivity.this, "قم بالتقاط صورة اخري", Toast.LENGTH_SHORT).show();
                    bitmaps.add(bitmap);
                    numPhoto += 1;
                }
                else {
                    //   Map<String,Integer>sorted = Utils.sortMap(map);
                    operatations.checklist(bitmaps, new IResultImageList() {
                        @Override
                        public void onresult(Bitmap bitmap1, String name) {
                            showResult(bitmap1,name);
                            numPhoto = 0;
                        }
                    });

                }
                */

              //  predict(bitmap);
            //    new MyTask().execute(bitmap);

                operatations.checkSkinOrNoSkin2(bitmap, new IResultImage() {
                    @Override
                    public void on_result(String name) {
                        // ask zoom to diease or  not skin  or goto skin detection and show crop view
                        //Toast.makeText(CameraDetectionActivity.this, name, Toast.LENGTH_SHORT).show();
                        if (name.equals("skin"))
                            showcrop(bitmap);
                        else if (name.equals("skinfar"))
                            showFragment(bitmap,"يوجد جلد ولاكن بعيد عن الكاميرا يرجي التقاط صورة اخري ولاكن عن قرب");
                        else if (name.equals("notskin"))
                            showFragment(bitmap,"لايوجد جلد في الصورة");
                        }
                });
            }

            @Override
    public void onVideo(CameraKitVideo cameraKitVideo) {

    }
});


   // img = ImageClassifierTensorflow.create(getAssets(),MODEL_PATH,INPUT_SIZE);
        /*
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    img = ImageClassifier.getInstance(CameraDetectionActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
*/
    }

    /*
    private void showResult(Bitmap bitmap , String name) {
        if (name.equals("skin"))
                    showcrop(bitmap);
                else if (name.equals("skinfar"))
                    showFragment(bitmap,"يوجد جلد ولاكن بعيد عن الكاميرا يرجي التقاط صورة اخري ولاكن عن قرب");
                else if (name.equals("notskin"))
                   showFragment(bitmap,"لايوجد جلد في الصورة");
    }
*/
    private void showcrop(Bitmap bitmap) {
        CropViewFragment cropViewFragment = new CropViewFragment();
        Bundle bundle =new Bundle();
        bundle.putParcelable("image",bitmap);
        cropViewFragment.setArguments(bundle);
        cropViewFragment.show(getSupportFragmentManager(), CropViewFragment.TAG);
    }
    @Override
    protected void onStart() {
        super.onStart();
        cameraView.start();
    }
    void showFragment(Bitmap bitmap,String s){
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle =new Bundle();
        bundle.putParcelable("image",bitmap);
        bundle.putString("msg",s);
        messageFragment.setArguments(bundle);
        messageFragment.show(getSupportFragmentManager(),MessageFragment.TAG);
    }
    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        operatations.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    /*
    void predict(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        //bitmap = Bitmap.createBitmap();

        final Bundle bundle = new Bundle();
        String res = img.classifyFrame(bitmap);
        bundle.putString("res",res);
        BottomResult bottomResult = new BottomResult();
        bottomResult.setArguments(bundle);
        bottomResult.show(getSupportFragmentManager(),"bottomres");

    }*/

    public void bu_toggle_fac(View view) {
        state_facing = !state_facing;
        if (state_facing) {
            facing.setImageResource(R.drawable.ic_facing_back);
            cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
        }
        else {
            facing.setImageResource(R.drawable.ic_facing_front);
            cameraView.setFacing(CameraKit.Constants.FACING_BACK);
        }
    }
    public void Bu_Capture(View view) {
        cameraView.captureImage();
    }
    public void Bu_Flash(View view) {
        state = !state;
        if (state) {
            flash.setImageResource(R.drawable.ic_flash_off);
            cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
        } else {
            flash.setImageResource(R.drawable.ic_flash_on);
            cameraView.setFlash(CameraKit.Constants.FLASH_ON);
        }
    }

  /*
    class MyTask extends  AsyncTask <Bitmap,Void,String>{

        String result = "";
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show progress bar dialog;
            spots.show();
            cameraView.stop();
        }

        @Override
        protected String doInBackground(Bitmap ... voids) {
            bitmap = voids[0];
            operatations.checkSkinOrNoSkin(bitmap, new IResultImage() {
                @Override
                public void on_result(String name) {
                    // ask zoom to diease or  not skin  or goto skin detection and show crop view
                    result =name  ;
                }
            });
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            spots.dismiss();
          //  Toast.makeText(CameraDetectionActivity.this, s, Toast.LENGTH_SHORT).show();
           showFragment(bitmap,s);

            // show fragment crop view or show fragment message
            if (s.equals("not skin")){
                 showFragment(bitmap,"لا يوجد جلد");
            }else if(s.equals("skin far")){
                 showFragment(bitmap,"يوجد جلد ولاكن بعيد عن الكاميرا يرجي التقاط صورة اخري ولاكن عن قرب");
            }else if  (s.equals("skin")){
                CropViewFragment cropViewFragment = new CropViewFragment();
                Bundle bundle =new Bundle();
                bundle.putParcelable("image",bitmap);
                cropViewFragment.setArguments(bundle);
                cropViewFragment.show(getSupportFragmentManager(), CropViewFragment.TAG);
            }

         }

        void showFragment(Bitmap bitmap,String s){
            MessageFragment messageFragment = new MessageFragment();
            Bundle bundle =new Bundle();
            bundle.putParcelable("image",bitmap);
            bundle.putString("msg",s);
            messageFragment.setArguments(bundle);
            messageFragment.show(getSupportFragmentManager(),MessageFragment.TAG);
        }

    }
*/


}
