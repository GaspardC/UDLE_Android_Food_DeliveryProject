package ch.epfl.sweng.udle.activities.HelpActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.MapActivity;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

/**
 * Created by Johan on 30.11.2015.
 *
 * Used an example found there :
 * https://www.bignerdranch.com/blog/viewpager-without-fragments/
 *
 */
public class HelpActivity extends Activity {
    private static String TAG = HelpActivity.class.getSimpleName();

    ViewPager pager;
    LightPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Assigning ViewPager View and setting the adapter
        adapter = new LightPagerAdapter(this);

        pager = (ViewPager) findViewById(R.id.help_pager);
        pager.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        pager.removeAllViews();
        pager.destroyDrawingCache();
        adapter.context = null;
        adapter.contextResources = null;
        adapter.inflater = null;
        adapter.metrics = null;
        adapter = null;
        pager.setAdapter(null);
        pager.setAdapter(null);
        pager = null;
        Log.d(TAG, "stopped");
    }


    public void goToOrderActivity(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}