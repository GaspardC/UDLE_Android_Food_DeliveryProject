package ch.epfl.sweng.udle.activities.HelpActivity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.NavItem;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

/**
 * Created by Johan on 30.11.2015.
 *
 * Used an example found there :
 * https://www.bignerdranch.com/blog/viewpager-without-fragments/
 *
 */
public class HelpActivity extends SlideMenuActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new LightPagerAdapter(this));
    }
}