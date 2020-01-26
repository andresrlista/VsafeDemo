package com.anlisoft.vsafe.utils;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.anlisoft.vsafe.models.data.Global.gUserProfileData;

public class UserLocation {


    public static void updateUserCurrentLocation(Context context, FusedLocationProviderClient mFusedLocationClient) {

        mFusedLocationClient = LocationServices
                .getFusedLocationProviderClient(context);
        mFusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            gUserProfileData.setUserCurrtentLocation(location);
                        }
                    }
                });
    }
}
