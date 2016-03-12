package things.shiny.represent.service;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import things.shiny.represent.activity.PhoneMainActivity;
import things.shiny.represent.activity.PhoneProfileActivity;
import things.shiny.represent.data.RepDataParcel;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.data.RepresentDataStore;
import things.shiny.represent.util.ParcelPacker;


public class PhoneListenerService extends WearableListenerService {

    // Watch just turned on and is asking for init data if the phone has any
    private static final String INIT_PATH = "/init";

    // The watch detected a shake, select random location
    private static final String SHAKE_PATH = "/shake";
    // The watch is requesting detailed profile views
    // will send an index String
    private static final String REP_PATH = "/profile";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        Log.d("PhoneListener::", "Message received : " + path);

        String content;

        if (path.equals(INIT_PATH)) {

            if (RepresentDataStore.hasData()) {

                // Pack current data store into a parcel and send to watch
                Intent watchIntent = new Intent(this, PhoneToWatchService.class);
                watchIntent.putExtra("dataUpdate", RepresentDataStore.toParcel());
                startService(watchIntent);
            }
        // TODO: SHAKE logic
        } else if (path.equals(SHAKE_PATH)) {
            Intent intent = new Intent(this, PhoneMainActivity.class);
            intent.putExtra("shake", "fun");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (path.equals(REP_PATH)) {
            content = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            int index = Integer.valueOf(content);
            Intent intent = new Intent(this, PhoneProfileActivity.class);
            intent.putExtra("repIndex", index);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }
    }
}
