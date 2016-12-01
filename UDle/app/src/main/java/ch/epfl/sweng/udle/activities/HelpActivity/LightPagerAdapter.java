package ch.epfl.sweng.udle.activities.HelpActivity;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ch.epfl.sweng.udle.R;

import static ch.epfl.sweng.udle.tools.ImageLoader.decodeSampledBitmapFromResource;

/**
 * Created by Johan on 30.11.2015.
 *
 * Used an example found there :
 * https://www.bignerdranch.com/blog/viewpager-without-fragments/
 *
 */
public class LightPagerAdapter extends PagerAdapter {
    private static String TAG = LightPagerAdapter.class.getSimpleName();

    Context context;
    Resources contextResources;
    DisplayMetrics metrics;
    LayoutInflater inflater;

    int[] imgResources = {
            R.drawable.how_to_use_1st_screen,
            R.drawable.how_to_use_2nd_screen,
            R.drawable.how_to_use_3rd_screen,
            R.drawable.how_to_use_4th_screen,
            R.drawable.how_to_use_5th_screen,
            R.drawable.how_to_use_6th_screen



    };

    /**
     * @param context context on which the adapter will be "drawing"
     */
    public LightPagerAdapter(Context context) {
        this.context = context;
        contextResources = this.context.getResources();
        metrics = contextResources.getDisplayMetrics();
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Get the rank of last help page, starting from 1.
     * Equivalent as Array.length
     * Remember to increase this number when implementing a new help page
     *
     * @return # of help pages
     */
    @Override
    public int getCount() {
        return imgResources.length  ;
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Create the help page for the given position.
     * Each help page is hardcoded here, if the position
     * is valid but does not correspond to a hardcoded page,
     * the adapter will inflate an empty page with the
     * corresponding help image in {@link #imgResources}.
     *
     *
     * @param container The ViewPager in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns a View representing the new page with the correct help image.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView;
        ImageView imageView;
        switch (position){
            case 0:
                itemView = inflater.inflate(R.layout.help_page_0, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_0_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 1:
                itemView = inflater.inflate(R.layout.help_page_1, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_1_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 2:
                itemView = inflater.inflate(R.layout.help_page_2, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_2_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 3:
                itemView = inflater.inflate(R.layout.help_page_3, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_3_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 4:
                itemView = inflater.inflate(R.layout.help_page_4, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_4_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 5:
                itemView = inflater.inflate(R.layout.help_page_5, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_5_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;

            default:
                itemView = inflater.inflate(R.layout.help_page, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_imageView);
                imageView.setImageBitmap(decodeSampledBitmapFromResource(contextResources, imgResources[position], 100, 100));
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
        }
        container.addView(itemView);

        return itemView;
    }

    /**
     * Remove a help page for the given position.
     *
     * @param container The ViewPager from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
        Log.d(TAG, "item_drink destroyed");
    }
}