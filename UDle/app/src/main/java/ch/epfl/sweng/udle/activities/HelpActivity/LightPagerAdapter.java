package ch.epfl.sweng.udle.activities.HelpActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Johan on 30.11.2015.
 *
 * Used an example found there :
 * https://www.bignerdranch.com/blog/viewpager-without-fragments/
 *
 */
public class LightPagerAdapter extends PagerAdapter {

    private Context mContext;

    public LightPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LightPagerEnum lightPagerEnum = LightPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(lightPagerEnum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return LightPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}