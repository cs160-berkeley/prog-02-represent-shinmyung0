package things.shiny.represent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class VoteScreenFragment extends Fragment {

    public static VoteScreenFragment newInstance(int location) {
        Bundle bundle = new Bundle();
        bundle.putInt("location", location);
        VoteScreenFragment f = new VoteScreenFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.round_fragment_vote, container, false);

        // with location get vote info from data model
        int demPercent = RepresentWatchDataModel.getDistrictDemPercent();
        int repPercent = 100 - demPercent;
        String locationStr = RepresentWatchDataModel.getLocationString();

        TextView demVote = (TextView) v.findViewById(R.id.demVote);
        TextView repVote = (TextView) v.findViewById(R.id.repVote);
        TextView loca = (TextView) v.findViewById(R.id.locationText);
        demVote.setText(Integer.toString(demPercent));
        repVote.setText(Integer.toString(repPercent));
        loca.setText(locationStr);
        return v;
    }
}
