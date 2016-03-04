package things.shiny.represent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";
    private static final String PROFILE_DEM = "/dem_profile";
    private static final String PROFILE_REP = "/rep_profile";
    private static final String LOCATION = "/location";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(TOAST) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("from watch", value);


        } else if (messageEvent.getPath().equalsIgnoreCase(PROFILE_DEM)
                || messageEvent.getPath().equalsIgnoreCase(PROFILE_REP) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, PhoneProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String partyStr = (messageEvent.getPath().equalsIgnoreCase(PROFILE_DEM)) ? "Democrat" : "Republican";
            intent.putExtra("party", partyStr);
            intent.putExtra("num", value);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase(LOCATION)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Log.d("location update", value);

            Intent intent = new Intent(this, PhoneMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("location", value);
            startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
