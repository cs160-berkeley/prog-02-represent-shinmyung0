package things.shiny.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        mWatchApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("T", "in onconnected");
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        Log.d("T", "found nodes");
                        //when we find a connected node, we populate the list declared above
                        //finally, we can send a message
                        sendMessage("/send_toast", "Good job!");
                        Log.d("T", "sent");
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            // get two strings from intent
            final String num = extras.getString("num");
            final String party = extras.getString("party");
            final String location = extras.getString("location");

            // Send the message with the cat name
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mWatchApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    String msgString, dataString;

                    if (location != null) {
                        msgString = "/location";
                        dataString = location;
                    } else {
                        if (party.equals("Democrat")) {
                            msgString = "/dem_profile";
                        } else {
                            msgString = "/rep_profile";
                        }
                        dataString = num;
                    }

                    sendMessage(msgString, dataString);
                }
            }).start();
        }

        return START_STICKY;
    }


    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }

}
