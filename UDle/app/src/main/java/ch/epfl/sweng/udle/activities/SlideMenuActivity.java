package ch.epfl.sweng.udle.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.udle.R;

public abstract class SlideMenuActivity extends AppCompatActivity {
    private static String TAG = SlideMenuActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    protected ListView slideMenuList;
    protected RelativeLayout slideMenu_frame;

    protected ArrayList<NavItem> slideMenuItems = new ArrayList<NavItem>();


    private CharSequence mDrawerTitle; //
    private CharSequence mTitle;       //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_slidemenu);

        //set menu items
        slideMenuItems.add(new NavItem("Preferences", "Change your preferences", R.mipmap.ic_launcher));
        slideMenuItems.add(new NavItem("About", "Get to know about us", R.mipmap.ic_launcher));


        mTitle = mDrawerTitle = getTitle();//

        //main layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //slide menu layout
        slideMenu_frame = (RelativeLayout) findViewById(R.id.slideMenu_frame);
        //slide menu list container
        slideMenuList = (ListView) findViewById(R.id.slideMenu_items);

        //adapter between logical NavItems and graphical representation
        DrawerListAdapter adapter = new DrawerListAdapter(this, slideMenuItems);

        //items in the slide menu will use our adapter to be displayed
        slideMenuList.setAdapter(adapter);
        //create click listeners for each menu item
        slideMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromList(position);
            }
        });



        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.openSlideMenu, R.string.closeSlideMenu) {
            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                getActionBar().setTitle(mTitle); //

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getActionBar().setTitle(mDrawerTitle); //

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Show the menu icon on top of the screen
        // More info: http://codetheory.in/difference-between-setdisplayhomeasupenabled-sethomebuttonenabled-and-setdisplayshowhomeenabled/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // slide menu actions
    private void selectItemFromList(int position) {
        //close Menu
        mDrawerLayout.closeDrawer(slideMenu_frame);
        switch (position){
            default:
                Intent newActivity = new Intent(this, DeliveryRestaurantMapActivity.class);
                startActivity(newActivity);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle other action bar items...

        return super.onOptionsItemSelected(item);
    }

    // Called when invalidateOptionsMenu() is invoked
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(slideMenuList);
        /*
        // If the nav drawer is open, hide action items related to the content view
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        */
        return super.onPrepareOptionsMenu(menu);
    }
}

class NavItem {
    String name;
    String description;
    int icon;

    public NavItem(String name, String description, int icon){
        this.name = name;
        this.description = description;
        this.icon = icon;
    }
}

class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItems.get(position).name );
        subtitleView.setText( mNavItems.get(position).description );
        iconView.setImageResource(mNavItems.get(position).icon);

        return view;
    }
}