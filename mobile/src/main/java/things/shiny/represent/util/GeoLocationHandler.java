package things.shiny.represent.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by shinmyung0 on 3/10/16.
 */
public class GeoLocationHandler {

    private Context currentContext;


    public GeoLocationHandler(Context context) {
        currentContext = context;
    }



    public Location getCurrentLocation(GoogleApiClient connection) {
        Location result = LocationServices.FusedLocationApi.getLastLocation(connection);
        if (result != null) {
            return result;
        }
        return null;
    }


    public static GeoPoint getPointFromZip(String zip, Context c) {
        try {
            Address address = new Geocoder(c)
                    .getFromLocationName(zip, 1).get(0);

            return new GeoPoint(address.getLongitude(), address.getLatitude());
        } catch (IOException e) {
            // ignore
            Log.d("GeoLocationHandler::", "IOException on getAddressString()");
            return null;
        }
    }

    public static String getAddressString(Location location, Context context) {
        if (location == null) {
            return "";
        }
        try {
            Address address = new Geocoder(context)
                    .getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);


            String state = address.getAdminArea();
            String area = address.getLocality();
            if (area == null) {
                area = address.getSubAdminArea();
            }
            if (area == null) {
                area = address.getSubLocality();
            }

            return area + ", " + state;
        } catch (IOException e) {
            // ignore
            Log.d("GeoLocationHandler::", "IOException on getAddressString()");
            return "";
        }
    }

    public static String getAddressString(String zipcode, Context context) {
        if (zipcode == null) {
            return "";
        }
        try {
            Address address = new Geocoder(context)
                    .getFromLocationName(zipcode, 1).get(0);


            String state = address.getAdminArea();
            String area = address.getLocality();

            if (area == null) {
                area = address.getSubAdminArea();
            }
            if (area == null) {
                area = address.getSubLocality();
            }

            return area + ", " + state + ", " + zipcode;
        } catch (IOException e) {
            // ignore
            Log.d("GeozipcodeHandler::", "IOException on getAddressString()");
            return "";
        }
    }

    // TODO: Random location logic
    public Location generateRandomLocation() {
        return new Location("something");
    }


    public static class GeoPoint {

        public double longitude;
        public double latitude;

        public GeoPoint(double lon, double lat) {
            longitude = lon;
            latitude = lat;

        }
    }



}



