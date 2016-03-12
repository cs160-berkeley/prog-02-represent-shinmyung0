package things.shiny.represent.data;

import java.util.ArrayList;
import java.util.List;

import things.shiny.represent.R;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class RepresentWatchDataModel {


    private static int location = 94704;

    private static List<RepresentativeInfo> currReps = new ArrayList<>();

    private static List<FeedCardInfo> currFeed = new ArrayList<>();

    private RepresentWatchDataModel() {}

    public static void setLocation(int newLocation) {
        location = newLocation;
        updateRepresentatives();
        updateFeed();
    }

    public static int getRepNum() {
        return currReps.size();
    }

    public static List<RepresentativeInfo> getRepresentatives() {
        return currReps;
    }

    public static List<FeedCardInfo> getCurrFeed() {
        return currFeed;
    }

    public static int getRandomLocation() {
        if (location != 94704) {
            location = 94704;
            return 94704;
        } else {
            location = 94001;
            return 94001;
        }
    }

    // return random location strings
    public static String getLocationString() {
        if (location == 0) {
            return "";
        }
        else if (location == 94704) {
            return "Berkeley, CA";
        } else {
            return "San Francisco, CA";
        }
    }

    // return mocked democrat vote percent
    public static int getDistrictDemPercent() {
        if (location == 94704) {
            return 67;
        } else {
            return 45;
        }
    }

    private static void updateRepresentatives() {

        RepresentativeInfo senator1, senator2, congress1, congress2;
        currReps.clear();

        if (location == 0) {
            return;
        }

        // mock data for berkeley
        if (location == 94704) {
            senator1 = new RepresentativeInfo();
            senator1.name = "Arthur Clark";
            senator1.party = "Democrat";
            senator1.title = "Senator";
            senator1.fbHandle = "democrat";
            senator1.twitterHandle = "TheDemocrats";
            senator1.homepage = "http://www.democrats.org/";
            senator1.profilePicResource = R.drawable.dem_senator1;

            senator2 = new RepresentativeInfo();
            senator2.name = "John Baxton Doe";
            senator2.party = "Democrat";
            senator2.title = "Senator";
            senator2.fbHandle = "democrat";
            senator2.twitterHandle = "TheDemocrats";
            senator2.homepage = "http://www.democrats.org/";
            senator2.profilePicResource = R.drawable.dem_senator2;

            congress1 = new RepresentativeInfo();
            congress1.name = "Rachael Miller";
            congress1.party = "Democrat";
            congress1.title = "Congresswoman";
            congress1.fbHandle = "democrat";
            congress1.twitterHandle = "TheDemocrats";
            congress1.homepage = "http://www.democrats.org/";
            congress1.profilePicResource = R.drawable.dem_congress1;

            currReps.add(senator1);
            currReps.add(senator2);
            currReps.add(congress1);

        }
        // mock data for all other places
        else {
            senator1 = new RepresentativeInfo();
            senator1.name = "Ted \"Machinegun\" Cruz";
            senator1.party = "Republican";
            senator1.title = "Senator";
            senator1.fbHandle = "GOP";
            senator1.twitterHandle = "GOP";
            senator1.homepage = "http://www.gop.com/";
            senator1.profilePicResource = R.drawable.rep_senator1;

            senator2 = new RepresentativeInfo();
            senator2.name = "Jessica Hailey";
            senator2.party = "Republican";
            senator2.title = "Senator";
            senator2.fbHandle = "GOP";
            senator2.twitterHandle = "GOP";
            senator2.homepage = "http://www.gop.com/";
            senator2.profilePicResource = R.drawable.rep_senator2;

            congress1 = new RepresentativeInfo();
            congress1.name = "Anita Adams";
            congress1.party = "Republican";
            congress1.title = "Congresswoman";
            congress1.fbHandle = "GOP";
            congress1.twitterHandle = "GOP";
            congress1.homepage = "http://www.gop.com/";
            congress1.profilePicResource = R.drawable.rep_congress1;

            congress2 = new RepresentativeInfo();
            congress2.name = "Jacqueline Kimmel";
            congress2.party = "Republican";
            congress2.title = "Congresswoman";
            congress2.fbHandle = "GOP";
            congress2.twitterHandle = "GOP";
            congress2.homepage = "http://www.gop.com/";
            congress2.profilePicResource = R.drawable.rep_congress2;

            currReps.add(senator1);
            currReps.add(senator2);
            currReps.add(congress1);
            currReps.add(congress2);
        }
    }

    private static void updateFeed() {
        // mock data for berkeley
        currFeed.clear();

        if (location == 0) {
            return;
        }

        FeedCardInfo card0, card1, card2, card3, card4;
        if (location == 94704) {
            card0 = new FeedCardInfo();
            card0.summary = "New post by Arthur Clark";
            card0.content = "Come support us at the Party Rally tonight!";
            card0.link = "http://www.facebook.com/democrat";
            card0.timeStr = "15mins ago";
            card0.type = "facebook";
            card0.name = "Arthur Clark";
            card0.party = "Democrat";
            card0.profilePicResource = R.drawable.dem_senator1;

            card1 = new FeedCardInfo();
            card1.summary = "New tweet by John Doe";
            card1.content = "We need each and every one of you guys!! #rallyfordemocracy";
            card1.link = "http://twitter.com/TheDemocrats";
            card1.timeStr = "37mins ago";
            card1.type = "twitter";
            card1.name = "John Baxton Doe";
            card1.party = "Democrat";
            card1.profilePicResource = R.drawable.dem_senator2;

            card2 = new FeedCardInfo();
            card2.summary = "New post by Rachael Miller";
            card2.content = "Can't we all just get along? #BlackLivesMatter";
            card2.link = "http://twitter.com/TheDemocrats";
            card2.timeStr = "1hr ago";
            card2.type = "twitter";
            card2.name = "Rachael Miller";
            card2.party = "Democrat";
            card2.profilePicResource = R.drawable.dem_congress1;

            card3 = new FeedCardInfo();
            card3.summary = "New post by Arthur Clark";
            card3.content = "Hateful rhetoric like the kind that's being thrown around by some " +
                            "so-called politicians goes against everything that this great nation stands for.";
            card3.link = "http://www.facebook.com/democrat";
            card3.timeStr = "1hr 15mins ago";
            card3.type = "facebook";
            card3.name = "Arthur Clark";
            card3.party = "Democrat";
            card3.profilePicResource = R.drawable.dem_senator1;

            card4 = new FeedCardInfo();
            card4.summary = "New tweet by Rachael Miller";
            card4.content = "Hilary or Bernie? #presidentialRace";
            card4.link = "http://twitter.com/TheDemocrats";
            card4.timeStr = "3/3 4:32 PM";
            card4.type = "twitter";
            card4.name = "Rachael Miller";
            card4.party = "Democrat";
            card4.profilePicResource = R.drawable.dem_congress1;


        }
        // mock data for all other places
        else {
            card0 = new FeedCardInfo();
            card0.summary = "New post by Ted Cruz";
            card0.content = "Come support us at the Party Rally tonight!";
            card0.link = "http://www.facebook.com/gop";
            card0.timeStr = "15mins ago";
            card0.type = "facebook";
            card0.name = "Ted Cruz";
            card0.party = "Republican";
            card0.profilePicResource = R.drawable.rep_senator1;

            card1 = new FeedCardInfo();
            card1.summary = "New tweet by Jessica Hailey";
            card1.content = "We need each and every one of you guys!! #rallyforgop";
            card1.link = "http://twitter.com/GOP";
            card1.timeStr = "37mins ago";
            card1.type = "twitter";
            card1.name = "Jessica Hailey";
            card1.party = "Republican";
            card1.profilePicResource = R.drawable.rep_senator2;

            card2 = new FeedCardInfo();
            card2.summary = "New post by Ted Cruz";
            card2.content = "Come get some of my machine gun bacon!! #gunrights";
            card2.link = "http://twitter.com/GOP";
            card2.timeStr = "1hr ago";
            card2.type = "twitter";
            card2.name = "Ted Cruz";
            card2.party = "Republican";
            card2.profilePicResource = R.drawable.rep_senator1;

            card3 = new FeedCardInfo();
            card3.summary = "New post by Jacqueline Kimmel";
            card3.content = "Hateful rhetoric like the kind that's being thrown around by some " +
                    "so-called politicians goes against everything that this great nation stands for.";
            card3.link = "http://www.facebook.com/gop";
            card3.timeStr = "1hr 15mins ago";
            card3.type = "facebook";
            card3.name = "Jacqueline Kimmel";
            card3.party = "Republican";
            card3.profilePicResource = R.drawable.rep_congress2;

            card4 = new FeedCardInfo();
            card4.summary = "New tweet by Anita Adams";
            card4.content = "Really? Are we really backing Trump now? #presidentialRace";
            card4.link = "http://twitter.com/GOP";
            card4.timeStr = "3/3 4:32 PM";
            card4.type = "twitter";
            card4.name = "Anita Adams";
            card4.party = "Republican";
            card4.profilePicResource = R.drawable.rep_congress1;
        }

        currFeed.add(card0);
        currFeed.add(card1);
        currFeed.add(card2);
        currFeed.add(card3);
        currFeed.add(card4);

    }




}
