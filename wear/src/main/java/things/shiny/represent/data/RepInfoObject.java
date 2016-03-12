package things.shiny.represent.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shinmyung0 on 3/11/16.
 */
public class RepInfoObject implements Parcelable {

    private JSONObject data;
    private ArrayList<BillInfo> bills;
    private ArrayList<CommitteeInfo> committees;
    private int index;


    public RepInfoObject(JSONObject obj, int pos) {
        data = obj;
        index = pos;
        bills = new ArrayList<>();
        committees = new ArrayList<>();

    }

    // Private constructor for Parcelable.Creator
    private RepInfoObject(Parcel in) {
        JSONObject obj = new JSONObject();
        Bundle bundle = in.readBundle();
        try {

            obj.put("party", bundle.getString("party"));
            obj.put("title", bundle.getString("title"));
            obj.put("first_name", bundle.getString("first_name"));
            obj.put("last_name", bundle.getString("last_name"));
            data = obj;
            index = bundle.getInt("index");

        } catch (Exception e) {
            Log.d("private constructor::", e.getMessage());
        }
    }

    // Static CREATOR
    public static final Parcelable.Creator<RepInfoObject> CREATOR = new Creator<RepInfoObject>() {
        @Override
        public RepInfoObject createFromParcel(Parcel in) {
            return new RepInfoObject(in);
        }

        @Override
        public RepInfoObject[] newArray(int size) {
            return new RepInfoObject[size];
        }


    };


    @Override
    public void writeToParcel(Parcel out, int flags) {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("party", data.getString("party"));
            bundle.putString("title", data.getString("title"));
            bundle.putString("first_name", data.getString("first_name"));
            bundle.putString("last_name", data.getString("last_name"));
            bundle.putInt("index", index);
            out.writeBundle(bundle);

        } catch (Exception e) {
            Log.d("Repinfo/writeToParcel::", e.getMessage());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }


    /* Getters and Setters */
    public int getIndex() {
        return index;
    }

    public void addBill(JSONObject obj) throws JSONException {
        String name = obj.getString("short_title");
        if (name == null || name.isEmpty() || name.equals("null")) {
            name = obj.getString("popular_title");
        }
        if (name == null || name.isEmpty()|| name.equals("null")) {
            name = "Bill #" + obj.getString("number");
        }
        String intro = obj.getString("introduced_on");
        bills.add(new BillInfo(name, intro));
    }

    public void addCommittee(JSONObject obj) throws JSONException {
        String name = obj.getString("name");
        committees.add(new CommitteeInfo(name));
    }

    public ArrayList<BillInfo> getBills() {
        return bills;
    }

    public ArrayList<CommitteeInfo> getCommittees() {
        return committees;
    }

    public String getFullName() throws JSONException {
        return String.format("%s %s", this.getFirstName(), this.getLastName());
    }

    public String getFirstName() throws JSONException {
        return data.getString("first_name");
    }


    public String getLastName() throws JSONException {
        return data.getString("last_name");
    }

    public String getParty() throws JSONException {
        String party = data.getString("party");
        if (party.equals("D")) {
            return "Democrat";
        }
        else if (party.equals("R")) {
            return "Republican";
        } else {
            return null;
        }
    }

    public String getTitle() throws JSONException {
        String title = data.getString("title");
        switch (title) {
            case "Sen":
                return "Senator";
            case "Rep":
                return "Representative";
        }
        return null;
    }

    public String getBioGuide() throws JSONException {
        return data.getString("bioguide_id");
    }

    public String getFullImgUrl() throws JSONException {
        return String.format("https://theunitedstates.io/images/congress/original/%s.jpg", this.getBioGuide());
    }

    public String getSmallImgUrl() throws JSONException {
        return String.format("https://theunitedstates.io/images/congress/225x275/%s.jpg", this.getBioGuide());
    }

    public String getBigImgUrl() throws JSONException {
        return String.format("https://theunitedstates.io/images/congress/450x550/%s.jpg", this.getBioGuide());
    }

    public String getHomepageUrl() throws JSONException {
        return data.getString("website");
    }

    public String getTwitter() throws JSONException {
        return data.getString("twitter_id");
    }

    public String getFacebook() throws JSONException {
        return data.getString("facebook_id");
    }

    public String getPhone() throws JSONException {
        return data.getString("phone");
    }

    public String getEmail() throws JSONException {
        return data.getString("oc_email");
    }

    public String getTermStart() throws JSONException {
        return data.getString("term_start");
    }

    public String getTermEnd() throws JSONException {
        return data.getString("term_end");
    }

    // Helper Classes
    public class BillInfo {
        public String name;
        public String introDate;

        public BillInfo(String billName,  String intro) {
            name = billName;
            introDate = intro;
        }

    }

    public class CommitteeInfo {
        public String name;
        public CommitteeInfo(String n) {
            name = n;
        }
    }

}


