package com.uniyapps.yadoctor.Nearby;

import com.uniyapps.yadoctor.Model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGOOGLEAPICLIENT {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDcRTh1a8x49io8JxK-8jsGctC2lbtYPIw")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
