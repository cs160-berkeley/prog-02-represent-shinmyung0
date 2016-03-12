package things.shiny.represent.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import things.shiny.represent.R;
import things.shiny.represent.adapter.WatchGridPagerAdapter;
import things.shiny.represent.data.RepDataParcel;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.data.RepresentWatchDataModel;
import things.shiny.represent.data.WatchDataStore;
import things.shiny.represent.fragment.MainScreenFragment;
import things.shiny.represent.fragment.RepRoundFragment;
import things.shiny.represent.fragment.VoteScreenFragment;
import things.shiny.represent.service.WatchToPhoneService;


public class WatchMainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_main);
        // Initialize the voting data
        WatchDataStore.loadVoteData(this);


        // Check for data update from the phone
        Intent intent = getIntent();
        RepDataParcel repData = intent.getParcelableExtra("repData");

        // if empty ask if the phone has anything
        if (repData == null) {
            askPhoneForInitData();
        }

        // Generate fragment grid using the received data
        final Fragment[][] pages = generatePages(repData);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                final GridViewPager pager = (GridViewPager) findViewById(R.id.viewPager);
                pager.setAdapter(new WatchGridPagerAdapter(stub.getContext(), getFragmentManager(), pages));

            }
        });
    }

    // TODO: init data from phone on start
    private void askPhoneForInitData() {
        Intent intent = new Intent(this, WatchToPhoneService.class);
        intent.putExtra("msg", "init");
        intent.putExtra("content", "init");
        startService(intent);
    }

    private Fragment[][] generatePages(RepDataParcel repData) {

        if (repData == null) {
            // Only initialize the main screen
            final Fragment[][] pages = {
                    // main loading screen
                    {MainScreenFragment.newInstance("Waiting...")},
            };
            return pages;
        } else {

            ArrayList<RepInfoObject> reps = repData.getReps();
            String county = repData.getCounty();
            String location = repData.getLocation();
            WatchDataStore.setData(repData);

            int repNum = reps.size();
            Fragment[] repPages = new Fragment[repNum];
            for (int i=0; i < repNum; i++) {
                repPages[i] = RepRoundFragment.newInstance(i);
            }


            final Fragment[][] pages = {
                    // main loading screen
                    {MainScreenFragment.newInstance(location)},
                    // representatives screens
                    repPages,
                    // District voting screen
                    {VoteScreenFragment.newInstance(county)}
            };
            return pages;
        }
    }

}
