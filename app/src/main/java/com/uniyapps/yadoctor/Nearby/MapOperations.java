package com.uniyapps.yadoctor.Nearby;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class MapOperations {


    long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =500;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    short a = 0 ;

    public static Location mCurrentLocation = new Location("");


    private Context context;
    private static MapOperations instance = null;

    private MapOperations(Context context) {
        this.context = context;
    }

    public static MapOperations getInstance(Context context) {
        if (instance == null)
            return new MapOperations(context);
        return instance;
    }


    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private void buildLocationRequest() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
    }

    private void buildLocationCallback(final DoUpdateUI func){
        mLocationCallback  = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for ( Location location1 : locationResult.getLocations()) {
                    mCurrentLocation = location1;
                    func.UpdateUi(mCurrentLocation);
                    a = 1;
                }
            }
        };
    }

    public void initLocation(DoUpdateUI doUpdateUI){
        buildLocationRequest();
        buildLocationCallback(doUpdateUI);
        startLocationUpdates();
    }

    public void destroy(){
        if (a!=0)
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}
