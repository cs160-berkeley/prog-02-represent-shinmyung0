package things.shiny.represent;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinmyung0 on 3/2/16.
 */
public class FeedViewFragment extends Fragment {

    public static FeedViewFragment newInstance() {
        FeedViewFragment f = new FeedViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_feed, container, false);

        RecyclerView feedRecycleView= (RecyclerView) v.findViewById(R.id.feedRecycleView);
        feedRecycleView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        feedRecycleView.setLayoutManager(manager);

        RepresentDataModel.setLocation(getLocation());
        FeedCardAdapter feedAdapter = new FeedCardAdapter(RepresentDataModel.getCurrFeed());
        feedRecycleView.setAdapter(feedAdapter);
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
        RecyclerView feedRecycleView = (RecyclerView) getView().findViewById(R.id.feedRecycleView);
        FeedCardAdapter repAdapter = new FeedCardAdapter(RepresentDataModel.getCurrFeed());
        feedRecycleView.setAdapter(repAdapter);
        feedRecycleView.invalidate();


    }
    // Get the location from location fragment
    private int getLocation() {
        PhoneMainActivity mainActivity = (PhoneMainActivity) getActivity();
        return mainActivity.getLocation();
    }

}
