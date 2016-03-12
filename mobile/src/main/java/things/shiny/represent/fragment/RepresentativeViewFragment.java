package things.shiny.represent.fragment;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import things.shiny.represent.R;
import things.shiny.represent.adapter.RepresentativeCardAdapter;
import things.shiny.represent.data.RepresentDataStore;
import things.shiny.represent.util.GeoLocationHandler;

/**
 * Created by shinmyung0 on 3/2/16.
 */
public class RepresentativeViewFragment extends Fragment {


    private RecyclerView repRecycleView;
    private RepresentativeCardAdapter repAdapter;
    private View mainView;


    public static RepresentativeViewFragment newInstance() {
        RepresentativeViewFragment f = new RepresentativeViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_phone_representatives, container, false);

        initRecycleView();

        updateAddressText();

        loadRepresentativeData();

        return mainView;
    }

    private void initRecycleView() {
        // Initialize the scrolling view
        repRecycleView = (RecyclerView) mainView.findViewById(R.id.repRecycleView);
        repRecycleView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        repRecycleView.setLayoutManager(manager);
    }

    private void updateAddressText() {
        // Address Text
        TextView addrTxt = (TextView) mainView.findViewById(R.id.addressText);
        addrTxt.setText(getLocationString());
    }

    private void loadRepresentativeData() {
        // Load data from the model to adapter
        repAdapter = new RepresentativeCardAdapter(RepresentDataStore.getRepresentatives(), getContext());
        repRecycleView.setAdapter(repAdapter);
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
        RecyclerView repRecycleView = (RecyclerView) getView().findViewById(R.id.repRecycleView);
        RepresentativeCardAdapter repAdapter = new RepresentativeCardAdapter(RepresentDataStore.getRepresentatives(), getContext());
        repRecycleView.setAdapter(repAdapter);
        repRecycleView.invalidate();

        TextView addrTxt = (TextView) getView().findViewById(R.id.addressText);
        addrTxt.setText(getLocationString());

    }


    private String getLocationString() {

        Location location = RepresentDataStore.getCurrentLocation();
        if (location != null) {
            return GeoLocationHandler.getAddressString(location, getContext());
        } else {
            String zip = RepresentDataStore.getCurrentZip();
            return GeoLocationHandler.getAddressString(zip, getContext());
        }
    }

}
