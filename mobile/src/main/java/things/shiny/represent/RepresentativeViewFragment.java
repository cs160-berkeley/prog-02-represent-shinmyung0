package things.shiny.represent;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinmyung0 on 3/2/16.
 */
public class RepresentativeViewFragment extends Fragment {


    public static RepresentativeViewFragment newInstance() {
        RepresentativeViewFragment f = new RepresentativeViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_representatives, container, false);

        RecyclerView repRecycleView = (RecyclerView) v.findViewById(R.id.repRecycleView);
        repRecycleView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        repRecycleView.setLayoutManager(manager);

        TextView addrTxt = (TextView) v.findViewById(R.id.addressText);
        addrTxt.setText(getLocationString());

        RepresentDataModel.setLocation(getLocation());
        RepresentativeCardAdapter repAdapter = new RepresentativeCardAdapter(RepresentDataModel.getRepresentatives());
        repRecycleView.setAdapter(repAdapter);

        return v;
    }



    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RepresentDataModel.setLocation(getLocation());
        RecyclerView repRecycleView = (RecyclerView) getView().findViewById(R.id.repRecycleView);
        RepresentativeCardAdapter repAdapter = new RepresentativeCardAdapter(RepresentDataModel.getRepresentatives());
        repRecycleView.setAdapter(repAdapter);
        repRecycleView.invalidate();

        TextView addrTxt = (TextView) getView().findViewById(R.id.addressText);
        addrTxt.setText(getLocationString());

    }


    // Get the location from location fragment
    private int getLocation() {
        PhoneMainActivity mainActivity = (PhoneMainActivity) getActivity();
        return mainActivity.getLocation();
    }

    private String getLocationString() {

        if (getLocation() == 0) {
            return "Please input a zip code";
        }

        if (getLocation() == 94704) {
            return "Berkeley, CA 94704";
        } else {
            return "San Francisco, CA " + getLocation();
        }

    }

}
