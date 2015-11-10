package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

/**
 * Created by Gasp on 04/11/2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.HorizontalSlideLibrary.SlidingTabLayout;


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
        Log.i("DDDDDDDDDDDDDDDDDDD", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {


        Log.i("EEEEEEEEEEEEEEEEEEEEEE", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+String.valueOf(position));

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            MenuFragment menu = new MenuFragment();
            menu.setPager(pager);
            return menu;
        }
        else if(position == 1) // if the position is 0 we are returning the First tab
        {

            Log.i("XXXXXXXXXXXXXXX", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

            OrderElement orderElement = new OrderElement();
            Menu menu = new Menu();
            menu.setFood(FoodTypes.KEBAB);
            orderElement.addMenu(menu);
            Menu menu2 = new Menu();
            menu2.setFood(FoodTypes.BURGER);
            double tmp = Math.random()*1;
            if (tmp < 0.5){
                orderElement.addMenu(menu2);
            }
            else {
                orderElement.addMenu(menu);
            }
            Orders.setActiveOrder(orderElement);


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

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    public void setPager(ViewPager pager) {
        Log.i("FFFFFFFFFFFFFFFFFFFFFFF", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        this.pager = pager;
    }
}