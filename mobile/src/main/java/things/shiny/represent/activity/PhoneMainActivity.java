package things.shiny.represent.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;


import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import things.shiny.represent.R;
import things.shiny.represent.fragment.FeedViewFragment;
import things.shiny.represent.fragment.LocationViewFragment;
import things.shiny.represent.fragment.RepresentativeViewFragment;
import things.shiny.represent.util.GeoLocationHandler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;

import java.util.List;


public class PhoneMainActivity extends AppCompatActivity
                               implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "TfV6IXFpx3KHkn5X15w4V6nmB";
    private static final String TWITTER_SECRET = "orSFj8BKMttZyxXJQZYdigS3d78rUHcjr57VguJc8n2FQqFn4S";


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private GoogleApiClient mGoogleApiClient;

    private static Location currentLocation;

    public final static int LOCATION_VIEW = 0;
    public final static int REP_VIEW = 1;
    public final static int FEED_VIEW = 2;
    public static Context mContext;


    // Saved references to fragments
    private LocationViewFragment fragment0;

    private boolean gotShake = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_main);

        connectGoogleApi();
        mContext = this;
        gotShake = false;

        // TODO: Twitter authentication and logic
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        // Check for any intents
        Intent intent = getIntent();
        if (watchShaken(intent)) {
            // generate a random location
            gotShake = true;
        }

        // Create the fragments with current location
        createPagerAdapter();


    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        Log.d("activity result", "called");

        if (req == LocationViewFragment.LOCATION_REQUEST_CODE) {
            if (res == Activity.RESULT_OK) {
                Log.d("activity result", "okay");
                fragment0.getCurrentLocation();
            }
        }
    }


    /* ############## Helper methods ###################### */


    // Create a connection to the GoogleApiClient
    protected void connectGoogleApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    // Check if there was a shake message from the watch
    private boolean watchShaken(Intent intent) {
        String msg = intent.getStringExtra("shake");
        if (msg != null) {
            return true;
        }
        return false;
    }

    public boolean gotShake() {
        return gotShake;
    }
    public void setShake(boolean val) {
        gotShake = val;
    }

    // Initialize the fragments
    protected void createPagerAdapter() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    // Set a new location object
    public void setNewLocation(Location location) {
        currentLocation = location;
    }

    // Return the current location object
    public Location getCurrentLocation() {
        if (currentLocation == null) {
            return null;
        }
        return currentLocation;
    }

    public boolean hasValidLocation() {
        return currentLocation != null;
    }

    public GoogleApiClient getApiConnection() {
        return mGoogleApiClient;
    }

    // Generate a random location
    private Location generateRandomLocation() {
        return new GeoLocationHandler(getBaseContext()).generateRandomLocation();
    }

    public void moveToTab(int position) {
        mViewPager.setCurrentItem(position);
    }


    public static Context getContext() {
        return mContext;
    }



    /**
     * Adapter for the main view fragments
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case LOCATION_VIEW:
                    fragment0 = LocationViewFragment.newInstance();
                    return fragment0;
                case REP_VIEW:
                    return RepresentativeViewFragment.newInstance();
                case FEED_VIEW:
                    return FeedViewFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
