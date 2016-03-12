package things.shiny.represent.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import things.shiny.represent.R;
import things.shiny.represent.data.RepresentWatchDataModel;
import things.shiny.represent.data.WatchDataStore;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class VoteScreenFragment extends Fragment {

    private TextView countyField;
    private TextView demVote;
    private TextView repVote;

    public static VoteScreenFragment newInstance(String county) {
        Bundle bundle = new Bundle();
        bundle.putString("county", county);
        VoteScreenFragment f = new VoteScreenFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.round_fragment_vote, container, false);

        // Using the county string, get voting data
        String county = getArguments().getString("county");

        String demPercent = "N/A";
        String repPercent = "N/A";
        try {
            demPercent = WatchDataStore.getDemVote(county);
            repPercent = WatchDataStore.getRepVote(county);
        } catch (Exception e) {
            Log.d("WatchVote::", e.getMessage());
        }

        demVote = (TextView) v.findViewById(R.id.demVote);
        repVote = (TextView) v.findViewById(R.id.repVote);
        countyField = (TextView) v.findViewById(R.id.locationText);
        demVote.setText(demPercent + "%");
        repVote.setText(repPercent + "%");
        countyField.setText(county);
        return v;
    }
}
