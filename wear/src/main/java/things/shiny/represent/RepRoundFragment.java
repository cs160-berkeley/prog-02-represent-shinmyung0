package things.shiny.represent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class RepRoundFragment extends Fragment {


    public static RepRoundFragment newInstance(int location, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("location", location);
        bundle.putInt("position", position);
        RepRoundFragment f = new RepRoundFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.round_fragment_representative, container, false);
        final int position = getArguments().getInt("position");
        // get info from data model then change view elements
        final RepresentativeInfo info = RepresentWatchDataModel.getRepresentatives().get(position);
        TextView title = (TextView) v.findViewById(R.id.titleText);
        TextView name = (TextView) v.findViewById(R.id.nameText);
        ImageView partyIcon = (ImageView) v.findViewById(R.id.partyIcon);
        LinearLayout colorBar = (LinearLayout) v.findViewById(R.id.partyColorBar);

        title.setText(info.title);
        name.setText(info.name);
        if (info.party.equals("Democrat")) {
            partyIcon.setImageResource(R.drawable.democrat);
            colorBar.setBackgroundColor(Color.parseColor("#2F80ED"));
        } else {
            partyIcon.setImageResource(R.drawable.republican);
            colorBar.setBackgroundColor(Color.parseColor("#EB5757"));
        }

        ImageButton infoBtn = (ImageButton) v.findViewById(R.id.infoIcon);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
                sendIntent.putExtra("num", Integer.toString(position));
                sendIntent.putExtra("party", info.party);
                getActivity().startService(sendIntent);
            }
        });
        
        return v;
    }

}
