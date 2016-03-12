package things.shiny.represent.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import things.shiny.represent.R;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.data.RepresentDataModel;
import things.shiny.represent.data.RepresentDataStore;
import things.shiny.represent.data.RepresentativeInfo;
import things.shiny.represent.util.AsyncListener;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class PhoneProfileActivity extends AppCompatActivity implements AsyncListener {

    // View objects
    // Header
    private ImageView profilePic;
    private ImageView partyImg;
    private TextView titlePartyText;
    private TextView nameText;
    private TextView termStartText;
    private TextView termEndText;
    // Contact Info
    private TextView phoneText;
    private TextView emailText;
    private TextView homepageText;

    // LinearLayouts
    private LinearLayout committeesList;
    private LinearLayout billsList;

    // RepInfoObj
    private RepInfoObject repData;
    private RepresentDataStore dataStore;
    private ProgressDialog loadingDialog;
    // Flags for incoming data
    private boolean gotBills;
    private boolean gotCommittees;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_profile);

        try {
            // grab RepInfo using index
            Intent intent = getIntent();
            int index = intent.getIntExtra("repIndex", 0);
            repData = RepresentDataStore.getRep(index);

            // Bind all the view objects
            bindViewReferences();
            linkViewData();

            showLoadingScreen();

            // using bioguide_id make data requests to committees and bills apis
            dataStore = new RepresentDataStore();
            getBillData();
            getCommitteeData();
        } catch (Exception e) {
            Log.d("ProfileActivity::", e.getMessage());
        }

    }

    // Bind all view references for later use
    private void bindViewReferences() {
        profilePic = (ImageView) findViewById(R.id.profile_image);
        partyImg = (ImageView) findViewById(R.id.party_image);
        titlePartyText = (TextView) findViewById(R.id.titleText);
        nameText = (TextView) findViewById(R.id.nameText);
        termStartText = (TextView) findViewById(R.id.termStartText);
        termEndText = (TextView) findViewById(R.id.termEndText);

        phoneText = (TextView) findViewById(R.id.phoneText);
        emailText = (TextView) findViewById(R.id.emailText);
        homepageText = (TextView) findViewById(R.id.homeText);

        committeesList = (LinearLayout) findViewById(R.id.committeesList);
        billsList = (LinearLayout) findViewById(R.id.billsList);

    }

    private void linkViewData() throws JSONException {
        // get image
        Picasso.with(this).load(repData.getSmallImgUrl()).into(profilePic);
        String party = repData.getParty();
        String partyAbbr = party.substring(0, 3);
        String title = repData.getTitle();

        if (party.equals("Democrat")) {
            partyImg.setImageResource(R.drawable.democrat);
        } else {
            partyImg.setImageResource(R.drawable.republican);
        }

        titlePartyText.setText(partyAbbr + ". " + title);

        nameText.setText(repData.getFullName());
        termStartText.setText("From  " + repData.getTermStart());
        termEndText.setText("Until  " + repData.getTermEnd());
        phoneText.setText(repData.getPhone());
        emailText.setText(repData.getEmail());
        homepageText.setText(repData.getHomepageUrl());

    }

    private void showLoadingScreen() {
        loadingDialog = ProgressDialog.show(this, "Loading Representative Data", "Please wait...", true);
        gotBills = false;
        gotCommittees = false;
    }

    private void dismissLoadingScreen() {
        loadingDialog.dismiss();
    }

    private void updateViewsWithNewData() {
        Log.d("Profile::", "updating views..");
        ArrayList<RepInfoObject.BillInfo> bills = repData.getBills();
        ArrayList<RepInfoObject.CommitteeInfo> committees = repData.getCommittees();
        int billNum = bills.size();
        int cNum = committees.size();
        TextView t;
        RepInfoObject.BillInfo b;
        RepInfoObject.CommitteeInfo c;
        for (int i=0; i < cNum; i++) {
            c = committees.get(i);
            t = makeTextView("- " + c.name);
            committeesList.addView(t);
        }
        for (int j=0; j < billNum; j++) {
            b = bills.get(j);
            t = makeTextView(b.name + " \n- Introduced " + b.introDate);
            billsList.addView(t);
        }
    }

    private TextView makeTextView(String text) {
        TextView result = new TextView(this);
        result.setText(text);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,0,5);
        result.setLayoutParams(p);
        result.setTextColor(Color.BLACK);
        return result;
    }
    private void getBillData() throws JSONException{
        Log.d("Profile::", "fetching bill data");
        dataStore.requestBillDataForRep(this, repData.getBioGuide());
    }

    private void getCommitteeData() throws JSONException{
        Log.d("Profile::", "fetching committee data");
        dataStore.requestCommitteeDataForRep(this, repData.getBioGuide());
    }

    @Override
    public void notifyListener(int code) {
        Log.d("Profile::", "Got a notification");
        switch (code) {
            case RepresentDataStore.FAILED_REQUEST_CODE:
                // display failed request message
                // do some error handling
                Log.d("ProfileActivity::", "We got a failed request");
                dismissLoadingScreen();
                break;
            case RepresentDataStore.BILLS_REQUEST:
                gotBills = true;
                break;
            case RepresentDataStore.COMMITTEES_REQUEST:
                gotCommittees = true;
                break;
        }
        // if we got both types of data back
        if (gotBills && gotCommittees) {
            dismissLoadingScreen();
            updateViewsWithNewData();
        }

    }
}
