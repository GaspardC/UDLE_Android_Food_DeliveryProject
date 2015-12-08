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
            case 6:
                itemView = inflater.inflate(R.layout.help_page_6, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_6_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 7:
                itemView = inflater.inflate(R.layout.help_page_7, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_7_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 8:
                itemView = inflater.inflate(R.layout.help_page_8, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_8_imageView);
                imageView.setContentDescription(context.getString(R.string.help_page) + position);
                break;
            case 9:
                itemView = inflater.inflate(R.layout.help_page_9, container, false);
                imageView = (ImageView) itemView.findViewById(R.id.help_page_9_imageView);
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
        Log.d(TAG, "item destroyed");
    }
}