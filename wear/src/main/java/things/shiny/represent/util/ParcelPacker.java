package things.shiny.represent.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shinmyung0 on 3/11/16.
 */
public class ParcelPacker {
    public static byte[] marshall(Parcelable parceable) {
        // Make an empty parcel
        Parcel parcel = Parcel.obtain();
        // Write data to this parcel object
        parceable.writeToParcel(parcel, 0);
        // serialize the parcel to byte array
        byte[] bytes = parcel.marshall();
        // Put the parcel object back in to a "pool"
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        // Make an empty parcel
        Parcel parcel = Parcel.obtain();
        // unmarshall byte array into parcel
        parcel.unmarshall(bytes, 0, bytes.length);
        // Move current read/write position
        parcel.setDataPosition(0); // this is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        return creator.createFromParcel(parcel);
    }
}
