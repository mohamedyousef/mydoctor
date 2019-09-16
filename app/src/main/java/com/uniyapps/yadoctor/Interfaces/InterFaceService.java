package com.uniyapps.yadoctor.Interfaces;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterFaceService {
    @Multipart
    @POST("/")
    Call<ResponseBody>getResut(@Part MultipartBody.Part photo,@Part("age")int num,@Part("blood")int blood,@Part("pain")int pain,@Part("h")int h);

}
