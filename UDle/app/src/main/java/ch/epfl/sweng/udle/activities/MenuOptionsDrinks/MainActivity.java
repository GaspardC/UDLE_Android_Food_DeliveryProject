package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.HorizontalSlideLibrary.SlidingTabLayout;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenuActivity;


public class MainActivity extends SlideMenuActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Menu","Options","Drinks"};
    int Numboftabs =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Creating The Toolbar and setting it as the Toolbar for the activity

/*        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);*/


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        adapter.setPager(pager);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        getActionBar();


    }

    /**
     * orders set to null in order to prevent from a bug in the previous mapActivity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Orders.setActiveOrder(null);
    }
}