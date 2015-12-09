package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

/**
 * Created by Gasp on 04/11/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;


/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    ViewPager pager;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    /**
     * @param position number of fragment
     * @return This method return the fragment for the every position in the View Pager
     */

    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            MenuFragment menu = new MenuFragment();
            menu.setPager(pager);
            return menu;
        }
        else if(position == 1) // if the position is 0 we are returning the First tab
        {
            OptionsFragment options = new OptionsFragment();
            options.setPager(pager);
            return options;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            DrinkFragment drinks = new DrinkFragment();
            return drinks;
        }


    }



    /**
     * @param position number of fragment
     * @return This method return the titles for the Tabs in the Tab Strip
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    /**
     * @return This method return the Number of tabs for the tabs Strip
     */
    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }


    /**
     * @param container
     * Use this method to show/hide 'noMenuSelected' message in Options Layer
     */
    @Override
    public void startUpdate(ViewGroup container) {
        View view = container.findViewById(R.id.Layout_Options);
        if (view != null){
            if (Orders.getActiveOrder().getOrder().size() == 0){ //If no menus selected
                view.findViewById(R.id.Options_noMenuSelected).setVisibility(View.VISIBLE);
            }
            else{
                view.findViewById(R.id.Options_noMenuSelected).setVisibility(View.GONE);
            }
        }
        super.startUpdate(container);
    }
}