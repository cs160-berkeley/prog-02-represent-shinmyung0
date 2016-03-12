package things.shiny.represent.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import things.shiny.represent.activity.PhoneMainActivity;
import things.shiny.represent.util.ApiRequestTask;
import things.shiny.represent.util.AsyncListener;
import things.shiny.represent.util.GeoLocationHandler;


/**
 * Created by shinmyung0 on 3/10/16.
 */
public class RepresentDataStore {

    // Async Task Request Codes
    public final static int REP_LIST_REQUEST = 0;
    public final static int BILLS_REQUEST = 1;
    public final static int COMMITTEES_REQUEST = 2;
    public final static int COUNTY_REQUEST = 3;
    public final static int TWEET_REQUEST = 4;

    public final static int FAILED_REQUEST_CODE = 4534535;

    private static String currZip;
    private static Location currLocation;

    private static String currLocationStr;
    private static String currCountyStr;
    private static ArrayList<RepInfoObject> representatives = new ArrayList<>();

    private static List<TwitterCard> socialData = Collections.synchronizedList(new ArrayList<TwitterCard>());


    private static HashMap<String, Integer> bioguideIndex = new HashMap<>();

    private static final String SUNLIGHT_API_KEY = "d0baafc9418042298c51d4551f7d5e28";
    private final static String GOOGLE_API_KEY = "AIzaSyB9QKI-zVPGwuGMPh7CQnWkOSggqDBY0Dk";
    private static final String TWITTER_KEY = "TfV6IXFpx3KHkn5X15w4V6nmB";
    private static final String TWITTER_SECRET = "orSFj8BKMttZyxXJQZYdigS3d78rUHcjr57VguJc8n2FQqFn4S";


    public RepresentDataStore() {

    }

    // Given an ArrayList of twitter handles will make requests for twitter timelines
    public void requestTweets(final AsyncListener listener, final ArrayList<RepInfoObject> allReps) {

        socialData.clear();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(PhoneMainActivity.getContext(), new Twitter(authConfig));

        // Make a guest session, then loop through reps and add twitter
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {


                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                StatusesService service = twitterApiClient.getStatusesService();

                int NUM_TWEETS = 2;
                // for every representative
                for (int i = 0; i < allReps.size(); i++) {

                    final RepInfoObject currRep = allReps.get(i);

                    try {
                        // get timeline for each representative
                        service.userTimeline(null, currRep.getTwitter(), NUM_TWEETS, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                            @Override
                            public void success(Result<List<Tweet>> listResult) {
                                for (Tweet tweet : listResult.data) {
                                    socialData.add(new TwitterCard(tweet, currRep));
                                }
                                Log.d("requestTweet::", "Success for Rep : " + currRep.getIndex());
                            }

                            @Override
                            public void failure(TwitterException e) {
                                e.printStackTrace();
                                Log.d("requestTweet::", "Failed for Rep : " + currRep.getIndex());
                            }
                        });
                    } catch (Exception e) {
                        Log.d("requestTweets::", e.getMessage());
                    }
                }
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

    }


    public void requestCounty(AsyncListener listener, double lon, double lat) {
        String apiURL = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%.4f,%.4f&key=%s", lat, lon, GOOGLE_API_KEY);
        DataTask req = new DataTask(COUNTY_REQUEST, listener);
        req.setLngLat(lon, lat);
        req.execute(apiURL);
        
    }

    public void requestBillDataForRep(AsyncListener listener, String bioguide_id) {
        String apiUrl = String.format("http://congress.api.sunlightfoundation.com/bills?sponsor_id=%s&apikey=%s", bioguide_id, SUNLIGHT_API_KEY);
        DataTask request = new DataTask(BILLS_REQUEST, listener);
        request.setId(bioguide_id);
        request.execute(apiUrl);
    }

    public void requestCommitteeDataForRep(AsyncListener listener, String bioguide_id) {
        String apiUrl = String.format("http://congress.api.sunlightfoundation.com/committees?member_ids=%s&apikey=%s", bioguide_id, SUNLIGHT_API_KEY);
        DataTask request = new DataTask(COMMITTEES_REQUEST, listener);
        request.setId(bioguide_id);
        request.execute(apiUrl);

    }

    public void requestDataForLocation(AsyncListener listener, String zip) {
        // set current location
        currZip = zip;
        currLocation = null;
        // create http url
        String apiUrl = genRepListUrl(zip);
        // api request to Sunlight
        DataTask request = new DataTask(REP_LIST_REQUEST, listener);
        request.execute(apiUrl);
        currLocationStr = GeoLocationHandler.getAddressString(zip, PhoneMainActivity.getContext());

    }

    public void requestDataForLocation(AsyncListener listener, Location location) {
        // set current location
        currLocation = location;
        currZip = null;
        // create http url
        String apiUrl = genRepListUrl(location);
        // api request to Sunlight
        DataTask request = new DataTask(REP_LIST_REQUEST, listener);
        request.execute(apiUrl);

        currLocationStr = GeoLocationHandler.getAddressString(location, PhoneMainActivity.getContext());
    }


    private static String genRepListUrl(String zip) {
        return String.format("http://congress.api.sunlightfoundation.com/legislators/locate?zip=%s&apikey=%s", zip, SUNLIGHT_API_KEY);
    }

    private static String genRepListUrl(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        return String.format("http://congress.api.sunlightfoundation.com/legislators/locate?latitude=%.2f&longitude=%.2f&apikey=%s", latitude, longitude, SUNLIGHT_API_KEY);
    }

    public static boolean hasData() {
        return (representatives.size()) > 0 && !currLocationStr.isEmpty() && !currCountyStr.isEmpty();
    }

    public static RepDataParcel toParcel() {
        return new RepDataParcel(representatives, currCountyStr, currLocationStr);
    }

    public static String getCurrLocationStr() {
        return currLocationStr;
    }

    public static String getCurrCountyStr() {
        return currCountyStr;
    }

    public static String getCurrentZip() {
        return currZip;
    }

    public static Location getCurrentLocation() {
        return currLocation;
    }

    public static ArrayList<RepInfoObject> getRepresentatives() {

        return representatives;
    }

    public static RepInfoObject getRep(int i) {
        return representatives.get(i);
    }

    public static List<TwitterCard> getSocialNetworkFeed() {
        Collections.sort(socialData);
        return socialData;
    }


    private class DataTask extends ApiRequestTask {

        // All requests
        private AsyncListener listener;
        private int type;

        // For bills
        private String id;

        // For geocoding
        private double longitude;
        private double latitude;


        public DataTask(int reqType, AsyncListener l) {
            type = reqType;
            listener = l;
        }

        public void setId(String bio_id) {
            id = bio_id;
        }

        public void setLngLat(double lon, double lat) {
            longitude = lon;
            latitude = lat;
        }



        @Override
        public void onPostExecute(JSONObject result) {

            if (result == null) {
                listener.notifyListener(FAILED_REQUEST_CODE);
                return;
            }

            try {

                // process information differently depending on request type
                switch (type) {
                    case REP_LIST_REQUEST:
                        processRepListData(result);
                        break;
                    case BILLS_REQUEST:
                        processBillData(result);
                        break;
                    case COMMITTEES_REQUEST:
                        processCommitteeData(result);
                        break;
                    case COUNTY_REQUEST:
                        processGeoData(result);
                        break;

                }


            } catch (Exception e) {
                Log.d("DataTask::", e.getMessage());
            }

        }

        private boolean isCounty(JSONArray arr) {
            return arr.toString().contains("\"administrative_area_level_2\"");
        }

        private boolean isState(JSONArray arr) {
            return arr.toString().contains("\"administrative_area_level_1\"");
        }

        // This method will process the given JSON geocode data
        // will also set the current address strings and county strings
        // then will notify the listener
        private void processGeoData(JSONObject result) throws JSONException {
            JSONArray tmp = result.getJSONArray("results");


            // process the geocode data
            if (tmp != null) {
                JSONArray components = tmp.getJSONObject(0).getJSONArray("address_components");

                int size = components.length();
                JSONObject comp;
                JSONArray types;
                String county = "";
                String state = "";
                // look for county, and state
                for (int i=0; i < size; i++) {
                    comp = components.getJSONObject(i);
                    types = comp.getJSONArray("types");
                    if (isCounty(types)) {
                        Log.d("got county", comp.toString());
                        county = comp.getString("short_name");
                    } else if (isState(types)) {
                        Log.d("got state", comp.toString());
                        state = comp.getString("short_name");
                    }
                }

                // set the county string
                // notify LocationViewFragment
                currCountyStr = county + ", " + state;
                listener.notifyListener(COUNTY_REQUEST);

            } else {
                listener.notifyListener(FAILED_REQUEST_CODE);
            }
        }

        private void processRepListData(JSONObject result) throws JSONException {
            int count = result.getInt("count");
            if (count > 0) {
                JSONArray allRepData = result.getJSONArray("results");
                JSONObject repObj;
                RepInfoObject repInfo;
                // clear previous data
                representatives.clear();

                for (int i=0; i<count; i++) {
                    repObj = allRepData.getJSONObject(i);
                    repInfo = new RepInfoObject(repObj, i);

                    // add to representatives array
                    representatives.add(repInfo);
                    bioguideIndex.put(repInfo.getBioGuide(), i);
                }
                // notify listener that we are done
                listener.notifyListener(REP_LIST_REQUEST);

            } else {
                listener.notifyListener(FAILED_REQUEST_CODE);
            }
        }

        // Process the bill data and add it to the corresponding RepInfoObject
        private void processBillData(JSONObject result) throws JSONException {
            Log.d("DataTask::", "processing bill data onPostExecute");
            int count = result.getInt("count");
            if (count > 0) {
                JSONArray allRepData = result.getJSONArray("results");
                JSONObject billObj;
                int repIndex = bioguideIndex.get(id);
                RepInfoObject repInfo = representatives.get(repIndex);

                // We want relatively recent
                count = Math.min(10, count);

                for (int i=0; i<count; i++) {
                    billObj = allRepData.getJSONObject(i);
                    repInfo.addBill(billObj);

                }
                // notify listener that we are done
                listener.notifyListener(BILLS_REQUEST);

            } else {
                Log.d("SunApi::", "FAILED bill data request");
                listener.notifyListener(FAILED_REQUEST_CODE);
            }

        }

        // Process the bill data and add it to the corresponding RepInfoObject
        private void processCommitteeData(JSONObject result) throws JSONException {
            Log.d("DataTask::", "processing committee data onPostExecute");
            int count = result.getInt("count");
            if (count > 0) {
                JSONArray allRepData = result.getJSONArray("results");
                JSONObject comObj;
                int repIndex = bioguideIndex.get(id);
                RepInfoObject repInfo = representatives.get(repIndex);

                for (int i=0; i<count; i++) {
                    comObj = allRepData.getJSONObject(i);
                    repInfo.addCommittee(comObj);

                }
                // notify listener that we are done
                listener.notifyListener(COMMITTEES_REQUEST);

            } else {
                Log.d("SunApi::", "FAILED committee data request");
                listener.notifyListener(FAILED_REQUEST_CODE);
            }

        }


    }


}
