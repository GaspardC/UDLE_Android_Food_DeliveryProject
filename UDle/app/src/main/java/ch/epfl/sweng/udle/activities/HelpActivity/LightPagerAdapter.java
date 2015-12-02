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
            R.drawable.background_city_night,
            R.drawable.flou2
    };

    public LightPagerAdapter(Context context) {
        this.context = context;
        contextResources = this.context.getResources();
        metrics = contextResources.getDisplayMetrics();
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.help_page, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.help_page_imageView);
        imageView.setImageBitmap(decodeSampledBitmapFromResource(contextResources, imgResources[position], 100, 100));
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
        Log.d(TAG, "item destroyed");
    }
}