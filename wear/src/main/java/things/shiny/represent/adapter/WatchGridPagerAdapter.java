package things.shiny.represent.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.app.Fragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;

import java.util.List;

/**
 * Created by shinmyung0 on 3/3/16.
 */
public class WatchGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;
    private Fragment[][] pages;

    public WatchGridPagerAdapter(Context context, FragmentManager fm, Fragment[][] pg) {
        super(fm);
        mContext = context;
        pages = pg;
    }

    @Override
    public android.app.Fragment getFragment(int row, int col) {
        return pages[row][col];
    }



    @Override
    public int getRowCount() {
        return pages.length;
    }

    @Override
    public int getColumnCount(int row) {
        return pages[row].length;
    }


}
