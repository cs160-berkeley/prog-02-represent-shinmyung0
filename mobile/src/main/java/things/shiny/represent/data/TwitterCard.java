package things.shiny.represent.data;

import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shinmyung0 on 3/12/16.
 */
public class TwitterCard implements Comparable<TwitterCard> {
    public Tweet tweet;
    public RepInfoObject representative;
    public TwitterCard(Tweet t, RepInfoObject r) {
        tweet = t;
        representative = r;
    }
    @Override
    public int compareTo(TwitterCard other){
        String myTimeStr = this.tweet.createdAt.substring(0, 20);
        String otherTimeStr = other.tweet.createdAt.substring(0, 20);
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss");
        try {
            Date myDate = format.parse(myTimeStr);
            Date otherDate = format.parse(otherTimeStr);
            if (myDate.equals(otherDate)) {
                return 0;
            } else if (myDate.after(otherDate)) {
                return -1;
            }
            return 1;
        } catch (Exception e) {
            // ignore
            return 0;
        }


    }
}
