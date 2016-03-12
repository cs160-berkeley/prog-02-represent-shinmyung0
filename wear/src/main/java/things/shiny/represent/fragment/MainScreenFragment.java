package things.shiny.represent.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import things.shiny.represent.R;
import things.shiny.represent.data.RepresentWatchDataModel;
import things.shiny.represent.activity.WatchMainActivity;
import things.shiny.represent.service.WatchToPhoneService;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class MainScreenFragment extends Fragment implements SensorEventListener {

    // Shake detection related variables
    private SensorManager sensorManager;
    private Sensor accelerometer;
    final private int sensorType = Sensor.TYPE_ACCELEROMETER;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 100;

    // View
    private TextView mainText;


    public static MainScreenFragment newInstance(String m) {
        Bundle bundle = new Bundle();
        bundle.putString("main", m);
        MainScreenFragment f = new MainScreenFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(sensorType);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.round_fragment_main, container, false);

        String location = getArguments().getString("main");

        String[] loc = location.split(",\\s");
        if (loc.length > 2) {
            location = loc[0] + ", " + loc[1];
        }

        mainText = (TextView) v.findViewById(R.id.locationText);
        mainText.setText(location);

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("WatchMain::", "shake detected");
                    sendRandomLocation();

                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Send a message to the WatchToPhone service
    public void sendRandomLocation() {


        Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
        sendIntent.putExtra("msg", "shake");
        sendIntent.putExtra("content", "fun");
        getActivity().startService(sendIntent);

//
//        Intent watchIntent = new Intent(getActivity(), WatchMainActivity.class);
//        watchIntent.putExtra("location", newLocation);
//        getActivity().startActivity(watchIntent);

    }


}
