package things.shiny.represent.service;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

import things.shiny.represent.activity.WatchMainActivity;
import things.shiny.represent.data.RepDataParcel;
import things.shiny.represent.util.ParcelPacker;

/**
 */
public class WatchListenerService extends WearableListenerService {

    // In PhoneToWatchService, we passed in a path
    // These paths serve to differentiate different phone-to-watch messages
    private static final String UPDATE_PATH = "/dataUpdate";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String msgPath = messageEvent.getPath();
        Log.d("WatchListener::", "received a message at " + msgPath);

        if( msgPath.equalsIgnoreCase(UPDATE_PATH) ) {


            RepDataParcel repData = ParcelPacker.unmarshall(messageEvent.getData(), RepDataParcel.CREATOR);
            Intent mainIntent = new Intent(this, WatchMainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.putExtra("repData", repData);
            Log.d("WatchListener::", "About to start main activity");
            startActivity(mainIntent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}