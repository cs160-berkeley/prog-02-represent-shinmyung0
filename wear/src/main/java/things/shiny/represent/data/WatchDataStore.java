package things.shiny.represent.data;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by shinmyung0 on 3/11/16.
 */
public class WatchDataStore {
    private static ArrayList<RepInfoObject> representatives;
    private static String currCounty;
    private static String currLocation;
    private static JSONObject voteData;

    private WatchDataStore() {

    }

    public static void setData(RepDataParcel data) {
        representatives = data.getReps();
        currCounty = data.getCounty();
        currLocation = data.getLocation();
    }

    public static void setRepresentatives(ArrayList<RepInfoObject> r) {
        representatives = r;
    }

    public static void setCurrCounty(String c) {
        currCounty = c;
    }

    public static void setCurrLocation(String l) {
        currLocation = l;
    }

    public static ArrayList<RepInfoObject> getRepresentatives() {
        return representatives;
    }

    public static RepInfoObject getRep(int i) {
        return representatives.get(i);
    }

    public static String getCurrCounty() {
        return currCounty;
    }

    public static String getCurrLocation() {
        return currLocation;
    }

    public static String getRepVote(String county) throws JSONException{
        JSONObject votes = voteData.getJSONObject(county);
        if (votes != null) {
            return votes.getString("romney");
        } else {
            return "0";
        }

    }

    public static String getDemVote(String county) throws JSONException {
        JSONObject votes = voteData.getJSONObject(county);
        if (votes != null) {
            return votes.getString("obama");
        } else {
            return "0";
        }
    }

    // Initialization code for loading voting data
    public static void loadVoteData(Context context) {

        if (voteData == null) {
            String json = null;
            try {

                InputStream is = context.getAssets().open("newelectioncounty2012.json");

                int size = is.available();

                byte[] buffer = new byte[size];

                is.read(buffer);

                is.close();

                json = new String(buffer, "UTF-8");
                voteData = new JSONObject(json);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
