package things.shiny.represent.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.lang.reflect.Array;
import java.util.ArrayList;

import things.shiny.represent.data.RepDataParcel;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.util.ParcelPacker;


public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;
    private final String UPDATE_PATH = "/dataUpdate";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
        }).build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get the update package
        final RepDataParcel data = intent.getParcelableExtra("dataUpdate");
        final byte[] dataPack = ParcelPacker.marshall(data);

        Log.d("PhoneToWatch::", "Service invoked!!");

        if (data != null) {
            Log.d("PhoneToWatch::", "data detected");

            // Send a new message
            new Thread( new Runnable() {
                @Override
                public void run() {
                    mApiClient.connect();
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                    for(Node node : nodes.getNodes()) {

                        //we find 'nodes', which are nearby bluetooth devices (aka emulators)
                        //send a message for each of these nodes (just one, for an emulator)
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                                mApiClient, node.getId(), UPDATE_PATH, dataPack).await();
                        //4 arguments: api client, the node ID, the path (for the listener to parse),
                        //and the message itself (you need to convert it to bytes.)

                        Log.d("PhoneToWatch::", "finished sending msg");

                    }
                }
            }).start();

        }


        return START_STICKY;
    }

    @Override //remember, all services need to implement an IBiner
    public IBinder onBind(Intent intent) {
        return null;
    }

}
