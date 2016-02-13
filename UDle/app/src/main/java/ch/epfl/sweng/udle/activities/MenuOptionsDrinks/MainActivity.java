package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.HorizontalSlideLibrary.SlidingTabLayout;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.MapActivity;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;


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
       /* OrderElement orderElement = new OrderElement();
        Orders.setActiveOrder(orderElement);*/




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

        /*// Attach the page change listener inside the activity
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                fragmentSelected(position);
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
*/


    }

    private void fragmentSelected(int position) {
/*
        Fragment page = getSupportFragmentManager().findFragmentByTag(""+ pager.getCurrentItem());
*/

/*        if(position == 2 && page != null){
            ((DrinkFragment)page).initializeData();

        }*/
/*        if(position == 2){
            DrinkFragment drinks = new DrinkFragment();
            DrinkFragment drikfrag = (DrinkFragment) adapter.getItem(pager.getCurrentItem());
            drikfrag.initializeData();
            pager.setCurrentItem(pager.getCurrentItem());

        }*/
    }

    /**
     * orders set to null in order to prevent from a bug in the previous mapActivity
     */
    @Override
    public void onBackPressed() {
        Orders.setActiveOrder(null);
        super.onBackPressed();
        /*Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);*/
    }


}