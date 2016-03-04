package things.shiny.represent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class RepresentativeCardAdapter extends RecyclerView.Adapter<RepresentativeCardAdapter.CardViewHolder> {
    private List<RepresentativeInfo> repList;

    public RepresentativeCardAdapter(List<RepresentativeInfo> repList) {
        this.repList = repList;
    }

    @Override
    public int getItemCount() {
        return repList.size();
    }

    // Bind all the view information
    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, int i) {

        RepresentativeInfo repInfo = repList.get(i);

        viewHolder.vName.setText(repInfo.name);
        viewHolder.vParty.setText(repInfo.party);
        viewHolder.vTitle.setText(repInfo.title);

        viewHolder.vProfilePic.setImageResource(repInfo.profilePicResource);
        if (repInfo.party.equals("Democrat")) {
            viewHolder.vPartyPic.setImageResource(R.drawable.democrat);
        } else {
            viewHolder.vPartyPic.setImageResource(R.drawable.republican);
        }

        setImageButtonLink(viewHolder.vHomeBtn, repInfo.homepage);
        setImageButtonLink(viewHolder.vFacebookBtn, RepresentativeInfo.FB_URL + repInfo.fbHandle);
        setImageButtonLink(viewHolder.vTwitterBtn, RepresentativeInfo.TWITTER_URL + repInfo.twitterHandle);
        setMoreInfoClick(viewHolder.vMoreBtn, repInfo);

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
    private void setMoreInfoClick(ImageButton btn, final RepresentativeInfo info) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PhoneProfileActivity.class);
                // pass information to the intent
                intent.putExtra("name", info.name);
                intent.putExtra("title", info.title);
                intent.putExtra("party", info.party);
                intent.putExtra("profilePic", info.profilePicResource);
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
