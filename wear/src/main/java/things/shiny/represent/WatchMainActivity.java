package things.shiny.represent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class WatchMainActivity extends Activity {

    private TextView mTextView;
    private int currZip = 94704;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_main);

        // get the intent from the phone message
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            int newZip = extras.getInt("location");
            Log.d("from phone", Integer.toString(newZip));
            currZip = newZip;
        }

        // with the current zip code generate the 2D array of fragments
        RepresentWatchDataModel.setLocation(currZip);
        int repNum = RepresentWatchDataModel.getRepNum();
        Fragment[] repPages = new Fragment[repNum];
        for (int i=0; i < repNum; i++) {
            repPages[i] = RepRoundFragment.newInstance(currZip, i);
        }

        final Fragment[][] pages = {
                // main loading screen
                {MainScreenFragment.newInstance(currZip)},
                // representatives screens
                repPages,
                // District voting screen
                {VoteScreenFragment.newInstance(currZip)}
        };


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                final GridViewPager pager = (GridViewPager) findViewById(R.id.viewPager);
                pager.setAdapter(new WatchGridPagerAdapter(stub.getContext(), getFragmentManager(), pages));

            }
        });
    }

    public void setLocation(int newZip) {
        currZip = newZip;
    }

    public int getLocation() {
        return currZip;
    }
}
