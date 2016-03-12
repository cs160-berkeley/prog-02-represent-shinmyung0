package things.shiny.represent.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import things.shiny.represent.R;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.data.RepresentWatchDataModel;
import things.shiny.represent.data.RepresentativeInfo;
import things.shiny.represent.data.WatchDataStore;
import things.shiny.represent.service.WatchToPhoneService;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class RepRoundFragment extends Fragment {


    // Views
    private TextView titleField;
    private TextView nameField;
    private ImageView partyImg;
    private  LinearLayout mainLayout;
    private ImageButton infoBtn;

    // Colors
    private final static int COLOR_DEM = Color.parseColor("#2F80ED");
    private final static int COLOR_REP = Color.parseColor("#EB5757");



    public static RepRoundFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        RepRoundFragment f = new RepRoundFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.round_fragment_representative, container, false);

        titleField = (TextView) v.findViewById(R.id.titleText);
        nameField = (TextView) v.findViewById(R.id.nameText);
        partyImg = (ImageView) v.findViewById(R.id.partyIcon);
        mainLayout = (LinearLayout) v.findViewById(R.id.partyColorBar);

        final int index = getArguments().getInt("index");
        RepInfoObject repInfo = WatchDataStore.getRep(index);

        try {
            titleField.setText(repInfo.getTitle());
            nameField.setText(repInfo.getFullName());
            String party = repInfo.getParty();

            if (party.equals("Democrat")) {
                partyImg.setImageResource(R.drawable.democrat);
                mainLayout.setBackgroundColor(COLOR_DEM);
            } else {
                partyImg.setImageResource(R.drawable.republican);
                mainLayout.setBackgroundColor(COLOR_REP);
            }


        } catch (Exception e) {
            Log.d("RepRoundFrag::", e.getMessage());
        }

        // More info button
        infoBtn = (ImageButton) v.findViewById(R.id.infoIcon);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
                sendIntent.putExtra("msg", "profile");
                sendIntent.putExtra("content", Integer.toString(index));
                getActivity().startService(sendIntent);
            }
        });
        
        return v;
    }

}
