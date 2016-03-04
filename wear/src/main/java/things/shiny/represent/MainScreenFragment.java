package things.shiny.represent;

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



    public static MainScreenFragment newInstance(int location) {
        Bundle bundle = new Bundle();
        bundle.putInt("location", location);
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
        int location = getArguments().getInt("location");
        // update the location Textview
        TextView zipText = (TextView) v.findViewById(R.id.locationText);
        zipText.setText(Integer.toString(location));
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
        Log.d("sensor", "something happend");
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d("sensor", "reliable");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                Log.d("sensor", "update");
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    sendRandomLocation();
                    Log.d("Watch", "shake detected");

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

    // Will make a request to the DataModel for a random location
    public void sendRandomLocation() {
        int newLocation = RepresentWatchDataModel.getRandomLocation();
        Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
        sendIntent.putExtra("location", Integer.toString(newLocation));
        getActivity().startService(sendIntent);

        Intent watchIntent = new Intent(getActivity(), WatchMainActivity.class);
        watchIntent.putExtra("location", newLocation);
        getActivity().startActivity(watchIntent);

    }


}
