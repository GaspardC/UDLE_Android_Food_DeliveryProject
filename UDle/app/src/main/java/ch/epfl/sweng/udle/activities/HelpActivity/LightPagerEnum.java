package ch.epfl.sweng.udle.activities.HelpActivity;

import ch.epfl.sweng.udle.R;

/**
 * Created by Johan on 30.11.2015.
 *
 * Used an example found there :
 * https://www.bignerdranch.com/blog/viewpager-without-fragments/
 *
 */
public enum LightPagerEnum {

    ONE(R.layout.help_1);

    private int mLayoutResId;

    LightPagerEnum(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}