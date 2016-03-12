package things.shiny.represent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import things.shiny.represent.data.FeedCardInfo;
import things.shiny.represent.R;
import things.shiny.represent.data.RepInfoObject;
import things.shiny.represent.data.TwitterCard;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class FeedCardAdapter extends RecyclerView.Adapter<FeedCardAdapter.FeedCardViewHolder> {
    private List<TwitterCard> tweetList;
    private Context context;

    public FeedCardAdapter(List<TwitterCard> tweetList, Context c) {

        this.tweetList = tweetList;
        context = c;

    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    @Override
    public void onBindViewHolder(FeedCardViewHolder viewHolder, int i) {
        TwitterCard twitterCard = tweetList.get(i);
        Tweet tweet = twitterCard.tweet;
        RepInfoObject rep = twitterCard.representative;

        try {
            viewHolder.vSummary.setText("@" + tweet.user.screenName);
            viewHolder.vContent.setText(tweet.text);
            viewHolder.vTimeStr.setText(tweet.createdAt.substring(0, 20));
            viewHolder.vTypeImg.setImageResource(R.drawable.twitter);

            String party = rep.getParty();

            // bind party pic
            if (party.equals("Democrat")) {
                viewHolder.vPartyPic.setImageResource(R.drawable.democrat);
            } else {
                viewHolder.vPartyPic.setImageResource(R.drawable.republican);
            }

            // bind profile pic
            Picasso.with(context).load(rep.getSmallImgUrl()).into(viewHolder.vProfilePic);

        } catch (Exception e) {
            Log.d("FeeedViewAdapter::", e.getMessage());
        }

    }

    public FeedCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                        from(viewGroup.getContext()).
                        inflate(R.layout.fragment_feed_card, viewGroup, false);
        return new FeedCardViewHolder(itemView);
    }

    public static class FeedCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView vSummary;
        protected TextView vContent;
        protected TextView vTimeStr;

        protected ImageView vProfilePic;
        protected ImageView vPartyPic;
        protected ImageView vTypeImg;

        public FeedCardViewHolder(View v) {
            super(v);
            vSummary = (TextView) v.findViewById(R.id.summaryText);
            vContent = (TextView) v.findViewById(R.id.contentText);
            vTimeStr = (TextView) v.findViewById(R.id.timeText);

            vProfilePic = (ImageView) v.findViewById(R.id.profile_image);
            vPartyPic = (ImageView) v.findViewById(R.id.party_image);
            vTypeImg = (ImageView) v.findViewById(R.id.type_image);

        }
    }
}
