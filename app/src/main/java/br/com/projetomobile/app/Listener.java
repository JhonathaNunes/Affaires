package br.com.projetomobile.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by 16254855 on 27/09/2017.
 */

public class Listener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
