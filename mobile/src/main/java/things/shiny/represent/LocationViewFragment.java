package things.shiny.represent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * Created by shinmyung0 on 3/2/16.
 */
public class LocationViewFragment extends Fragment {

    public static LocationViewFragment newInstance() {
        LocationViewFragment f = new LocationViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_phone_location, container, false);
        // search button
        Button locationButton = (Button) v.findViewById(R.id.locationBtn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneMainActivity mainActivity = (PhoneMainActivity) getActivity();
                EditText location = (EditText) v.findViewById(R.id.editText);
                String txt = location.getText().toString();
                int zipcode;
                if (txt.equals("")) {
                    mainActivity.setLocation(0);
                    return;
                }
                else if (txt.equals("Current Location")) {
                    zipcode = 94704;
                } else {
                    zipcode = Integer.parseInt(txt);
                }
                mainActivity.setLocation(zipcode);

                // create the intent to pass to the service which will
                // send a message to the watch
                Intent watchIntent = new Intent(mainActivity, PhoneToWatchService.class);
                watchIntent.putExtra("location", zipcode);
                mainActivity.startService(watchIntent);

                // simulate a swipe to move to next tab
                mainActivity.moveToTab(1);


            }
        });

        ImageButton currLocationBtn = (ImageButton) v.findViewById(R.id.currLocationBtn);
        currLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText location = (EditText) v.findViewById(R.id.editText);
                location.setText("Current Location");
            }
        });

        return v;
    }
}
