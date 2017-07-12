package aia.com.wheely_map.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import aia.com.wheely_map.activities.OpenMarkerActivity;
import aia.com.wheely_map.map.Ramp;
import aia.com.wheely_map.map.RampManager;
import aia.com.wheely_map.user.User;
import aia.com.wheely_map.user.UserManager;

import static aia.com.wheely_map.utils.ActivityUtils.openActivity;

public class MapsFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = MapsFragment.class.getSimpleName();

    private static MapsFragment instance;
    private static List<Ramp> rampsOnMap;

    public GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rampsOnMap = new ArrayList<>();
        getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "onMapReady GoogleMap googleMap:" + googleMap);
        UserManager.enableTestUser();
        User user = new User();

        Ramp demo1 = new Ramp(user, "This ramp has a high angle of attack", null, 37.7749, -122.4194);
        Ramp demo2 = new Ramp(user, "This ramp has no grip", null, 47.6205, -122.3493);
        Ramp demo3 = new Ramp(user, "This ramp has a fat hole right in the middle of it", null, 47.576063, -122.292813);

        RampManager.addRampToRegisteredRamps(demo1);
        RampManager.addRampToRegisteredRamps(demo2);
        RampManager.addRampToRegisteredRamps(demo3);

        setMarker(demo1);
        setMarker(demo2);
        setMarker(demo3);

        mMap.setOnMarkerClickListener(this);

        setUpMap();
    }

    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(47.6062, -122)));
    }

    public boolean setMarker(Ramp ramp) {
        if (mMap == null) {
            Log.d(TAG, "setMarker mMap:" + mMap);
            return false;
        } else if (rampsOnMap.contains(ramp)) {
            Log.i(TAG, "setMarker: ramp is already marked");
            return false;
        }
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(ramp.getLatitude(), ramp.getLongitude())));
        rampsOnMap.add(ramp);
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng position = marker.getPosition();
        Ramp clickedMarker = RampManager.findRamp(position.latitude, position.longitude);
        if (clickedMarker != null) {
            Intent intent = new Intent(getContext(), OpenMarkerActivity.class);
            intent.putExtra("description", clickedMarker.getDescription());
            startActivity(intent);
        }
        return true;
    }

    public static MapsFragment getInstance() {
        Bundle args = new Bundle();

        if (instance == null) {
            instance = new MapsFragment();
        }

        instance.setArguments(args);
        return instance;
    }

    public GoogleMap getmMap() {
        return mMap;
    }
}
