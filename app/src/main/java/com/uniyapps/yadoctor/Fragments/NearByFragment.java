package com.uniyapps.yadoctor.Fragments;;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.uniyapps.yadoctor.BuildConfig;
import com.uniyapps.yadoctor.Model.Example;
import com.uniyapps.yadoctor.Nearby.DoUpdateUI;
import com.uniyapps.yadoctor.Nearby.IGOOGLEAPICLIENT;
import com.uniyapps.yadoctor.Nearby.MapOperations;
import com.uniyapps.yadoctor.R;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByFragment extends Fragment {

    MapView mapView ;
    private GoogleMap googleMap;
    MapOperations mapOperations;
    Retrofit retrofit ;
    String baseurl = "https://maps.googleapis.com/maps/";
    IGOOGLEAPICLIENT igoogleapiclient ;
    private Polyline polyline;
    ArrayList<String> markers = new ArrayList<>();
    Marker marker;
    float zoomlevel =  15;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit  = new Retrofit.Builder().baseUrl(baseurl).addConverterFactory(GsonConverterFactory.create()).build();
        igoogleapiclient = retrofit.create(IGOOGLEAPICLIENT.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        mapOperations = MapOperations.getInstance(getContext());
        return inflater.inflate(R.layout.activity_maps,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                final MyInfoWindowAdapter myInfoWindowAdapter = new MyInfoWindowAdapter();
                googleMap.setInfoWindowAdapter(myInfoWindowAdapter);

                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                googleMap.setMyLocationEnabled(true);
                                mapOperations.initLocation(new DoUpdateUI() {
                                    @Override
                                    public void UpdateUi(final Location mCurrentLocation) {
                                        updateUI(mCurrentLocation);
                                        DrawIconsHospitals(mCurrentLocation,10000);

                                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(final Marker marker) {

                                                if (!marker.getTitle().equals("your")) {

                                                    if (marker.isInfoWindowShown()) {
                                                        marker.hideInfoWindow();
                                                    }

                                                    if (polyline != null)
                                                        polyline.remove();

                                                    GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                                                            .from(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                                                            .to(marker.getPosition())
                                                            .execute(new DirectionCallback() {
                                                                @Override
                                                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                                                    if(direction.isOK()) {
                                                                        Leg leg = direction.getRouteList().get(0).getLegList().get(0);
                                                                        Info distanceInfo = leg.getDistance();
                                                                        Info durationInfo = leg.getDuration();
                                                                        myInfoWindowAdapter.distance = distanceInfo.getText();
                                                                        myInfoWindowAdapter.time = durationInfo.getText();
                                                                        ArrayList<LatLng> latLngs = leg.getDirectionPoint();
                                                                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(), latLngs, 5, Color.BLUE);
                                                                        polyline = mMap.addPolyline(polylineOptions);
                                                                        marker.showInfoWindow();
                                                                    }

                                                                }
                                                                @Override
                                                                public void onDirectionFailure(Throwable t) {
                                                                }
                                                            });


                                                }
                                                return true;
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    // open device settings when the permission is
                                    // denied permanently
                                    openSettings();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                // For showing a move to my location button


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (!marker.getTitle().equals("your"))
                            marker.hideInfoWindow();
                        if (polyline != null)
                            polyline.remove();
                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        zoomlevel =  googleMap.getCameraPosition().zoom;
                    }
                });

                googleMap.setMaxZoomPreference(18);
                googleMap.setMinZoomPreference(14);

            }
        });

    }


    private void updateUI(Location mCurrentLocation1) {
        if (mCurrentLocation1 != null) {
            if (marker !=null)
                marker.remove();
                LatLng latLng = new LatLng(mCurrentLocation1.getLatitude(), mCurrentLocation1.getLongitude());
                marker = googleMap.addMarker(new MarkerOptions().title("your").position(latLng).flat(true));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomlevel));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapOperations.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        mapOperations.destroy();
}

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapOperations.destroy();
        mapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void DrawIconsHospitals(Location location,int radius){
        Call<Example> call = igoogleapiclient.getNearbyPlaces("hospital", location.getLatitude() + "," + location.getLongitude(), radius);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                try {
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        String placeName = response.body().getResults().get(i).getName();
                        if (!markers.contains(placeName)) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                            markerOptions.position(latLng);
                            // Adding Title to the Marker
                            markerOptions.title(placeName + " : " + vicinity);
                            // Adding Marker to the Camera.
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_hospital));
                            googleMap.addMarker(markerOptions);
                            markers.add(placeName);
                        }
                    }
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }


        });
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        public  String  distance = " " , time=" ";

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info, null);
        }

        @Override
        public View getInfoContents(final Marker marker) {
            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.numtitle));
            tvTitle.setText(marker.getTitle());

            TextView distanceof = myContentsView.findViewById(R.id.distanceof);
            distanceof.setText(distance);

            TextView dtime = myContentsView.findViewById(R.id.timeof);
            dtime.setText(time);

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }

}
