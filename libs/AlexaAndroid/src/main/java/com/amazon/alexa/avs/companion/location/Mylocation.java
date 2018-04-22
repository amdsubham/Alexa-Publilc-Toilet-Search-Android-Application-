package com.amazon.alexa.avs.companion.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class Mylocation implements LocationListener {
    Double lati,longi;

    @Override
    public void onLocationChanged(Location location) {
        lati= location.getLatitude();
        longi=location.getLongitude();

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
}
