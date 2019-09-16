package com.uniyapps.yadoctor.Operations;

 import android.graphics.Bitmap;
 import android.support.v7.app.AppCompatActivity;
 import android.widget.Toast;
 import com.uniyapps.yadoctor.Fragments.LoadQuestionFragment;
 import com.uniyapps.yadoctor.Interfaces.IResultImage;
 import com.uniyapps.yadoctor.Interfaces.InterFaceService;
 import com.uniyapps.yadoctor.TF.ImageClassifier;
 import com.uniyapps.yadoctor.TF.ImageClassifierGeneral;
 import com.uniyapps.yadoctor.TF.MobileNetSecondVERSION;
 import com.uniyapps.yadoctor.TF.SecondStageModel;
 import com.uniyapps.yadoctor.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Operatations {

    private static final int INPUT_SIZE = 224;
    private Executor executor = Executors.newSingleThreadExecutor();
    ImageClassifier img;
    private static Operatations instance  = null;
    AppCompatActivity activity;
    Retrofit retrofit;
    ImageClassifierGeneral imageClassifierGeneral;

    private Operatations(final AppCompatActivity activity){
        this.activity = activity;
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.50:3000").build();
    }

    public static Operatations getInstance(AppCompatActivity activity) {
        if (instance == null)
            instance = new Operatations(activity);
        return instance;
    }

    public void checkSkinOrNoSkin2( Bitmap bitmap, final IResultImage iResultImage){
         imageClassifierGeneral = new MobileNetSecondVERSION(activity);
        iResultImage.on_result(imageClassifierGeneral.classifyFrame(bitmap));
    }
    public void checkifskinHaveDieaseOrNo(Bitmap bitmap,  IResultImage iResultImage){
        imageClassifierGeneral = new SecondStageModel(activity);
        iResultImage.on_result(imageClassifierGeneral.classifyFrame(bitmap));
    }
    public void classifiySkinDieases(final String MODEL_PATH, final Bitmap bitmap, final IResultImage iResultImage){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                   // ImageClassifier.MODEL_PATH  = MODEL_PATH;
                    ImageClassifier.typestage  = 1;
                    img = ImageClassifier.getInstance(activity);
                    String res = predict(bitmap) ;

                    iResultImage.on_result(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // get first or check first
    }
    public void sendData(Bitmap bitmap,int blood,int pain , int h, int age, final IResultImage iResultImage) throws IOException {
        final File f = new File(activity.getCacheDir(), "photo.jpg");
        f.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        InterFaceService interFaceService = retrofit.create(InterFaceService.class);
        RequestBody filePart = RequestBody.create(MediaType.parse("image/*"),f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", f.getName(), filePart);

        Call<ResponseBody> responseBodyCall = interFaceService.getResut(body,age,blood,pain,h);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                f.delete();
                try {
                    iResultImage.on_result(response.body().string());
                    Utils.zero();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void showFragmentQuestions(){
        LoadQuestionFragment fragment = new  LoadQuestionFragment();
        fragment.show(activity.getSupportFragmentManager(),LoadQuestionFragment.TAG);
    }
    String predict(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        //bitmap = Bitmap.createBitmap(bitmap);
        String res = img.classifyFrame(bitmap);
        return res;
    }

    public void close(){
        imageClassifierGeneral.close();
            /*
        try{

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (img.tflite !=null)
                    img.close();
            }
        }); }
        catch (Exception e){
            e.printStackTrace();
        }
            */

    }
}
