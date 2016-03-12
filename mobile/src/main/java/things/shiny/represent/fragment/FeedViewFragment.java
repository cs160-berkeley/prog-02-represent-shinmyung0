package things.shiny.represent.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import things.shiny.represent.adapter.FeedCardAdapter;
import things.shiny.represent.R;
import things.shiny.represent.data.RepresentDataStore;

/**
 * Created by shinmyung0 on 3/2/16.
 */
public class FeedViewFragment extends Fragment {

    // View elements
    private RecyclerView feedRecycleView;
    private FeedCardAdapter feedAdapter;


    public static FeedViewFragment newInstance() {
        FeedViewFragment f = new FeedViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phone_feed, container, false);

        initMainLayout(v);

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
        RecyclerView feedRecycleView = (RecyclerView) getView().findViewById(R.id.feedRecycleView);
        FeedCardAdapter repAdapter = new FeedCardAdapter(RepresentDataStore.getSocialNetworkFeed(), getContext());
        feedRecycleView.setAdapter(repAdapter);
        feedRecycleView.invalidate();

    }

    private void initMainLayout(View v) {
        feedRecycleView= (RecyclerView) v.findViewById(R.id.feedRecycleView);
        feedRecycleView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        feedRecycleView.setLayoutManager(manager);
        feedAdapter = new FeedCardAdapter(RepresentDataStore.getSocialNetworkFeed(), getContext());
        feedRecycleView.setAdapter(feedAdapter);
    }


}
