package things.shiny.represent.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shinmyung0 on 3/11/16.
 */
public class RepDataParcel implements Parcelable {

    private ArrayList<RepInfoObject> reps;
    private String county;
    private String location;



    public RepDataParcel() {
        reps = new ArrayList<>();
        county = "";
        location = "";
    }
    public RepDataParcel(ArrayList<RepInfoObject> r, String c, String l) {
        reps = r;
        county = c;
        location = l;
    }
    private RepDataParcel(Parcel in) {
//        Bundle bundle = in.readBundle();
//        reps = bundle.getParcelableArrayList("reps");
//        county = bundle.getString("county");
//        location = bundle.getString("location");
        reps = new ArrayList<>();
        in.readTypedList(reps, RepInfoObject.CREATOR);
        county = in.readString();
        location = in.readString();
    }

    public void addRepresentative(RepInfoObject obj) {
        reps.add(obj);
    }

    public void setCounty(String c) {
        county = c;
    }

    public void setLocationStr(String s) {
        location = s;
    }

    public ArrayList<RepInfoObject> getReps() {
        return reps;
    }

    public String getCounty() {
        return county;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {


        out.writeTypedList(reps);
        out.writeString(county);
        out.writeString(location);
//
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("reps", reps);
//        bundle.putString("county", county);
//        bundle.putString("location", location);
//        out.writeBundle(bundle);


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RepDataParcel> CREATOR = new Creator<RepDataParcel>() {
        @Override
        public RepDataParcel createFromParcel(Parcel input) {
            return new RepDataParcel(input);
        }

        @Override
        public RepDataParcel[] newArray(int size) {
            return new RepDataParcel[size];
        }

    };





}
