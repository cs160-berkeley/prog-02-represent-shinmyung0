package things.shiny.represent.service;

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

    private final String SHAKE_PATH = "/shake";
    private final String REP_PATH = "/profile";
    private final String INIT_PATH = "/init";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("WatchToPhoneService::", "Service created!");

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
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        Log.d("WatchPhoneService::", "found a node to connect with");

                    }
                });
    }

    // When this service is invoked
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String type = intent.getStringExtra("msg");
        // for profile requests will be the representative's index
        String content = intent.getStringExtra("content");



        if (type != null) {
            String tmp = "/";

            switch (type) {
                case "init":
                    tmp = INIT_PATH;
                    break;
                case "profile":
                    tmp = REP_PATH;
                    break;
                case "shake":
                    tmp = SHAKE_PATH;
                    break;
            }

            final String path = tmp;

            final String text = content;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mWatchApiClient.connect();
                    for (Node node : nodes) {
                        Wearable.MessageApi.sendMessage(mWatchApiClient, node.getId(), path, text.getBytes());
                        Log.d("WatchToPhone::", "sent message : " + path);
                    }
                }
            }).start();
        }

        return START_STICKY;
    }


    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}


}
