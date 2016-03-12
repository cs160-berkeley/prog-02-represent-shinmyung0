package things.shiny.represent.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import things.shiny.represent.activity.PhoneMainActivity;
import things.shiny.represent.R;
import things.shiny.represent.data.RepDataParcel;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.data.RepresentDataStore;
import things.shiny.represent.service.PhoneToWatchService;
import things.shiny.represent.util.ApiRequestTask;
import things.shiny.represent.util.AsyncListener;
import things.shiny.represent.util.GeoLocationHandler;


/**
 * Created by shinmyung0 on 3/2/16.
 */
public class LocationViewFragment extends Fragment implements AsyncListener {

    private Context currContext;
    private PhoneMainActivity mainActivity;
    private boolean usingZipcode = true;
    public final static int LOCATION_REQUEST_CODE = 1000;

    // Main location address field
    private EditText locationField;
    private ProgressDialog loadingDialog;


    // Information for zip validity checking
    private final static String zipcodeURL = "https://www.zipcodeapi.com/rest/";
    private final String ZIP_API_KEY = "qUckVp4hGL3k9aJEZ3cWQnunr5dKO0ofUAAQ0E8Gqc0zmJemNqiDOM0fRd0GepW8";
    private final String format = "json";
    private final String units = "degrees";

    // fields for checking that we got all data requests
    private boolean gotReps;
    private boolean gotCounty;
    private boolean gotAllTweets;

    private boolean wantCurrLocation;

    // Geographic bounds
    private final double minLat = 30.00f;
    private final double maxLat = 42.00f;
    private final double minLon = -120.00f;
    private final double maxLon = -80.00f;


    public static LocationViewFragment newInstance() {
        LocationViewFragment f = new LocationViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragment
        final View v = inflater.inflate(R.layout.fragment_phone_location, container, false);
        currContext = getContext();
        mainActivity = (PhoneMainActivity) getActivity();

        // Bind locationField View
        locationField = (EditText) v.findViewById(R.id.editText);
        gotReps = false;
        gotCounty = false;
        wantCurrLocation = false;
        gotAllTweets = false;


        // check if there has been a shake
        // randomly generate lon/lat get Location within bounds
        if (mainActivity.gotShake()) {
            double ranLat = Math.random() * 100;
            double ranLon = Math.random() * 100;
            double newLat = Math.min(minLat + ranLat, maxLat);
            double newLon = Math.min(minLon + ranLon, maxLon);

            Location ranLoc = new Location("");
            ranLoc.setLatitude(newLat);
            ranLoc.setLongitude(newLon);

            mainActivity.setShake(false);
            checkLocationPermission();
            loadRepresentativesFromLocation(ranLoc);

        }



        // search button and onclick listener
        final Button locationButton = (Button) v.findViewById(R.id.locationBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the EditText value
                String txt = locationField.getText().toString();

                // Check the validity of the input Zipcode
                if (!txt.isEmpty() && isValidZipStr(txt)) {
                    // If we are using current location, send a different request
                    if (!usingZipcode && mainActivity.hasValidLocation()) {
                        // Do actual data loading
                        loadRepresentativesFromLocation(mainActivity.getCurrentLocation());
                    } else {
                        // Check if it is a valid zip code
                        // After execution will call loadRepresentatives()
                        String urlStr = String.format("%s%s/info.%s/%s/%s", zipcodeURL, ZIP_API_KEY, format, txt, units);
                        new CheckAddressTask().execute(urlStr);
                    }
                } else {
                    Toast toast = Toast.makeText(currContext, "Please input a proper zip code!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        // current location button and handler
        ImageButton currLocationBtn = (ImageButton) v.findViewById(R.id.currLocationBtn);
        currLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usingZipcode) {
                    checkLocationPermission();
                    wantCurrLocation = true;
                    disableLocationField();


                } else {
                    wantCurrLocation = false;
                    usingZipcode = true;
                    enableLocationField();
                }
            }
        });

        return v;
    }

    private void disableLocationField() {
        locationField.setEnabled(false);
        locationField.setClickable(false);
        locationField.setBackgroundColor(Color.GRAY);
        locationField.setTextColor(Color.WHITE);
    }

    private void enableLocationField() {
        locationField.setText("");
        locationField.setEnabled(true);
        locationField.setClickable(true);
        locationField.setBackgroundColor(Color.WHITE);
        locationField.setTextColor(Color.BLACK);
        locationField.requestFocus();
    }


    // Check current location permissions and prompt user if not set
    // This will also call getCurrentLocation() on success
    private void checkLocationPermission() {
        LocationRequest req = new LocationRequest();
        LocationSettingsRequest.Builder b = new LocationSettingsRequest.Builder();
        b.addLocationRequest(req);
        b.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result;
        result = LocationServices.SettingsApi.checkLocationSettings(mainActivity.getApiConnection(), b.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    // Everything is good, getCurrentLocation()
                    case LocationSettingsStatusCodes.SUCCESS:

                        if (wantCurrLocation) {
                            getCurrentLocation();
                        }

                        break;
                    // Not good but can be fixed with dialog
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {

                            status.startResolutionForResult(
                                    mainActivity,
                                    LOCATION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // ignore
                        }
                        break;
                    // No good, but nothing we can do
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    public void getCurrentLocation() {
        PhoneMainActivity activity = (PhoneMainActivity) getActivity();
        locationField = (EditText) getView().findViewById(R.id.editText);

        GeoLocationHandler handler = new GeoLocationHandler(getContext());
        Location currLocation = handler.getCurrentLocation(activity.getApiConnection());

        locationField.setText(GeoLocationHandler.getAddressString(currLocation, getContext()));
        usingZipcode = false;
        mainActivity.setNewLocation(currLocation);

    }

    // Main method to fetch congressional data and county data
    private void loadRepresentativesFromZip(String zipcode) {
        Log.d("ReceivedZip!", zipcode);
        showLoadingDialog("Loading Data", "Fetching all necessary data...");
        // Congressional data
        RepresentDataStore dataStore = new RepresentDataStore();
        dataStore.requestDataForLocation(this, zipcode);

        // county data
        GeoLocationHandler.GeoPoint p = GeoLocationHandler.getPointFromZip(zipcode, currContext);
        dataStore.requestCounty(this, p.longitude, p.latitude);

    }

    private void loadRepresentativesFromLocation(Location location) {
        showLoadingDialog("Loading Data", "Fetching all necessary data...");
        Log.d("Received Location", String.valueOf(location.getLongitude()));

        // set up RepresentDataStore necessary fields
        // Get representative data for long, lat using Sunlight
        RepresentDataStore dataStore = new RepresentDataStore();
        dataStore.requestDataForLocation(this, location);
        dataStore.requestCounty(this, location.getLongitude(), location.getLatitude());

    }

    // Assumes Representative data is loaded
    private void loadTweetsForReps() {
        // start counter
        ArrayList<RepInfoObject> allReps = RepresentDataStore.getRepresentatives();



        RepresentDataStore dataStore = new RepresentDataStore();
        dataStore.requestTweets(this, allReps);

    }



    // This is called from any AsyncTasks that this fragment has bound itself to
    @Override
    public void notifyListener(int reqCode) {

        switch (reqCode) {
            // got the representative information
            case RepresentDataStore.REP_LIST_REQUEST:
                // send the same information to the watch
                // Simulate a swipe
                gotReps = true;
                break;
            case RepresentDataStore.COUNTY_REQUEST:
                gotCounty = true;
                break;
            case RepresentDataStore.TWEET_REQUEST:
                // everytime success will decrement until exactly 0

                break;
            case RepresentDataStore.FAILED_REQUEST_CODE:
                dismissLoadingDialog();
                showAlertDialog("Error Fetching Data", "Something went wrong! Please try again.");
                gotReps = false;
                gotCounty = false;
                break;
        }

        // If we got all first info then
        if (gotCounty && gotReps) {
            loadTweetsForReps();
        }


        if (gotCounty && gotReps) {
            dismissLoadingDialog();
            sendDataToWatch();
            mainActivity.moveToTab(PhoneMainActivity.REP_VIEW);
        }



    }

    // Attempt to send information to a pair wearable
    private void sendDataToWatch() {
        // Create intent and pack it with parcelable RepInfoObj
        Intent watchIntent = new Intent(mainActivity, PhoneToWatchService.class);

        // Put parcelable into extra
        watchIntent.putExtra("dataUpdate", RepresentDataStore.toParcel());
        mainActivity.startService(watchIntent);
    }


    // Checks length and whether the string is all numbers
    private boolean isValidZipStr(String str) {
        
        if (!usingZipcode) {
            return true;
        }
        
        if (str.length() != 5) {
            return false;
        }
        String regex = "\\d+";
        if (!str.matches(regex)) {
            return false;
        }
        return true;
    }

    private void showLoadingDialog(String title, String body) {
        loadingDialog = ProgressDialog.show(getActivity(), title, body, true);
    }

    private void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }

    private void showAlertDialog(String title, String body) {
        AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
        alert.setTitle(title);
        alert.setMessage(body);
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }



    // AsyncTask that checks the address
    private class CheckAddressTask extends ApiRequestTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Display loading dialog
            showLoadingDialog("Checking Address...", "Please wait...");
        }


        @Override
        protected void onPostExecute(JSONObject response) {

            if (response == null || response.has("error_code")) {
                dismissLoadingDialog();
                showAlertDialog("Zip Not Found", "Invalid Zip! Please input another.");

                return;
            } else {
                dismissLoadingDialog();
                try {
                    loadRepresentativesFromZip(response.getString("zip_code"));
                } catch (Exception e) {
                    Log.d("onPostExecute::", e.getMessage());
                }
            }
        }


    }


}
