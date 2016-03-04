package things.shiny.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class PhoneProfileActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_profile);

        // received data from the invoker
        Intent intent = getIntent();

        // determined where the request came from
        String repName, repTitle, repParty;
        RepresentativeInfo repInfo;
        int profileResource;
        // if this string exists then from watch
        String nStr = intent.getStringExtra("num");
        if (nStr != null) {
            repInfo = RepresentDataModel.getRepresentatives().get(Integer.parseInt(nStr));
            repName = repInfo.name;
            repTitle = repInfo.title;
            repParty = repInfo.party;
            profileResource = repInfo.profilePicResource;
        } else {
            repName = intent.getStringExtra("name");
            repTitle = intent.getStringExtra("title");
            repParty = intent.getStringExtra("party");
            profileResource = intent.getIntExtra("profilePic", R.drawable.dem_senator1);
        }

        // bind elements in activity with data
        TextView titleView, nameView, partyView, termView;
        titleView = (TextView) findViewById(R.id.titleText);
        nameView = (TextView) findViewById(R.id.nameText);
        partyView = (TextView) findViewById(R.id.partyText);
        termView = (TextView) findViewById(R.id.termText);

        titleView.setText(repTitle);
        nameView.setText(repName);
        partyView.setText(repParty);


        ImageView profileImage, partyImage;
        profileImage = (ImageView) findViewById(R.id.profile_image);
        partyImage = (ImageView) findViewById(R.id.party_image);
        profileImage.setImageResource(profileResource);

        if (repParty.equals("Democrat")) {
            partyImage.setImageResource(R.drawable.democrat);
        } else {
            partyImage.setImageResource(R.drawable.republican);
        }


    }

}
