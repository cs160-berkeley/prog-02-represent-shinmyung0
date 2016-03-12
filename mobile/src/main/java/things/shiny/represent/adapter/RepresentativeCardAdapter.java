package things.shiny.represent.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import things.shiny.represent.R;
import things.shiny.represent.activity.PhoneMainActivity;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.activity.PhoneProfileActivity;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class RepresentativeCardAdapter extends RecyclerView.Adapter<RepresentativeCardAdapter.CardViewHolder> {
    private List<RepInfoObject> repList;
    private Context context;

    public RepresentativeCardAdapter(List<RepInfoObject> repList, Context c) {
        this.repList = repList;
        context = c;

    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    // Bind all the view information
    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, int i) {

        RepInfoObject repInfo = repList.get(i);
        try {
            // Get fields
            String party = repInfo.getParty();
            String homepage = repInfo.getHomepageUrl();
            String twitter = repInfo.getTwitter();
            String fb = repInfo.getFacebook();


            // Set text fields
            viewHolder.vName.setText(repInfo.getFullName());
            viewHolder.vParty.setText(party);
            viewHolder.vTitle.setText(repInfo.getTitle());

            // Set image url using Picasso
            Picasso.with(context).load(repInfo.getSmallImgUrl()).into(viewHolder.vProfilePic);

            if (party.equals("Democrat")) {
                viewHolder.vPartyPic.setImageResource(R.drawable.democrat);
            } else {
                viewHolder.vPartyPic.setImageResource(R.drawable.republican);
            }

            if (homepage != null) {
                setImageButtonLink(viewHolder.vHomeBtn, homepage);
            }

            if (twitter != null) {
                setImageButtonLink(viewHolder.vTwitterBtn, "http://twitter.com/" + twitter);

            }

            if (fb != null) {
                setImageButtonLink(viewHolder.vFacebookBtn, "http://facebook.com/" + fb);
            }

            // Set listener for more info button
            setMoreInfoClick(viewHolder.vMoreBtn, repInfo);

        } catch (Exception e) {
            Log.d("RepCardAdapter", e.getMessage());
        }


    }

    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.fragment_representative_card, viewGroup, false);
        return new CardViewHolder(itemView);
    }

    // Given an image button bind a link to it
    private void setImageButtonLink(ImageButton btn, final String urlStr) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(urlStr));
                v.getContext().startActivity(intent);
            }
        });
    }

    // Bind the More Info button to start a new activity
    private void setMoreInfoClick(ImageButton btn, final RepInfoObject info) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PhoneProfileActivity.class);
                // pass index number of the current rep
                intent.putExtra("repIndex", info.getIndex());
                // start the activity
                v.getContext().startActivity(intent);
            }
        });

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vParty;
        protected TextView vTitle;

        protected ImageView vProfilePic;
        protected ImageView vPartyPic;

        protected ImageButton vHomeBtn;
        protected ImageButton vFacebookBtn;
        protected ImageButton vTwitterBtn;

        protected ImageButton vMoreBtn;

        public CardViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.nameText);
            vParty = (TextView) v.findViewById(R.id.partyText);
            vTitle = (TextView) v.findViewById(R.id.titleText);

            vProfilePic = (ImageView) v.findViewById(R.id.profile_image);
            vPartyPic = (ImageView) v.findViewById(R.id.party_image);

            vHomeBtn = (ImageButton) v.findViewById(R.id.homeBtn);
            vFacebookBtn = (ImageButton) v.findViewById(R.id.fbBtn);
            vTwitterBtn = (ImageButton) v.findViewById(R.id.twitterBtn);
            vMoreBtn = (ImageButton) v.findViewById(R.id.moreBtn);



        }
    }
}
