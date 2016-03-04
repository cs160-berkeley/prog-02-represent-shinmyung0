package things.shiny.represent;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class FeedCardAdapter extends RecyclerView.Adapter<FeedCardAdapter.FeedCardViewHolder> {
    private List<FeedCardInfo> feedList;

    public FeedCardAdapter(List<FeedCardInfo> feedList) {
        this.feedList = feedList;
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public void onBindViewHolder(FeedCardViewHolder viewHolder, int i) {
        FeedCardInfo feedInfo = feedList.get(i);

        viewHolder.vSummary.setText(feedInfo.summary);
        viewHolder.vContent.setText(feedInfo.content);
        viewHolder.vTimeStr.setText(feedInfo.timeStr);

        // bind party pic
        if (feedInfo.party.equals("Democrat")) {
            viewHolder.vPartyPic.setImageResource(R.drawable.democrat);
        } else {
            viewHolder.vPartyPic.setImageResource(R.drawable.republican);
        }

        // bind post type
        if (feedInfo.type.equals("facebook")) {
            viewHolder.vTypeImg.setImageResource(R.drawable.fb);
        } else if (feedInfo.type.equals("twitter")) {
            viewHolder.vTypeImg.setImageResource(R.drawable.twitter);
        }

        // bind profile pic
        viewHolder.vProfilePic.setImageResource(feedInfo.profilePicResource);


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
