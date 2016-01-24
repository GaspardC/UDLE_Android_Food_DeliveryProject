package ch.epfl.sweng.udle.activities.SlideMenu;

/*
took most of the code from there
http://codetheory.in/android-navigation-drawer/
*/

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;

import com.parse.ParseUser;

import java.util.ArrayList;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.AboutActivity;
import ch.epfl.sweng.udle.activities.CreditCardActivity;
import ch.epfl.sweng.udle.activities.CurrentOrdersActivity;
import ch.epfl.sweng.udle.activities.DeliveryRestaurantMapActivity;
import ch.epfl.sweng.udle.activities.HelpActivity.HelpActivity;
import ch.epfl.sweng.udle.activities.MapActivity;
import ch.epfl.sweng.udle.activities.ProfileActivity;
import ch.epfl.sweng.udle.network.DataManager;

/**
 * You need to add the following line in the activity declaration in AndroidManifest.xml
 *
 *      android:theme="@style/SlideMenuTheme"
 *
 * because 'setTheme(R.style.SlideMenuTheme);' seems not working.
 *
 */
public abstract class SlideMenuActivity extends AppCompatActivity {
    private static String TAG = SlideMenuActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private static ProfilePictureView avatar;
    ListView slideMenuList;
    RelativeLayout slideMenu_frame;
    RelativeLayout content_frame;

    protected ArrayList<NavItem> slideMenuItems = new ArrayList(20);


    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SlideMenuTheme);
        super.onCreate(savedInstanceState);
        setTheme(R.style.SlideMenuTheme);
        super.setContentView(R.layout.activity_slidemenu);


        //set menu items

        //home
        slideMenuItems.add(new NavItem(getString(R.string.home), getString(R.string.orderNow),R.drawable.icon_home, MapActivity.class));
        slideMenuItems.add(new NavItem(getString(R.string.currentOrders), getString(R.string.currentOrdersDetail),R.drawable.icon_current_order, CurrentOrdersActivity.class));


        if(DataManager.isARestaurant()){
            slideMenuItems.add(new NavItem(getString(R.string.restaurantMode), getString(R.string.restaurantModeDesc), R.drawable.logogreen, DeliveryRestaurantMapActivity.class));
        }

        slideMenuItems.add(new NavItem(getString(R.string.creditCard), getString(R.string.addACreditCard),R.drawable.icon_credit_card, CreditCardActivity.class));
        slideMenuItems.add(new NavItem(getString(R.string.about), getString(R.string.aboutus), R.drawable.icon_about, AboutActivity.class));
        slideMenuItems.add(new NavItem(getString(R.string.help), getString(R.string.helpDesc), R.drawable.icon_howtouse, new Runnable() {
            @Override
            public void run() {
                Intent newActivity = new Intent(getApplicationContext(), HelpActivity.class);
                newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newActivity);
            }
        }));



        mTitle = mDrawerTitle = getTitle();//
        TextView helloTextView = (TextView) findViewById(R.id.hello);
        if (ParseUser.getCurrentUser() !=null){
            helloTextView.setText("Hello "+ DataManager.getUserName());
        }
        else{
            helloTextView.setText("Hello ");
        }
        TextView username = (TextView) findViewById(R.id.SlideMenu_userName);
        username.setText("yours settings");

        avatar = (ProfilePictureView) findViewById(R.id.avatar);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            avatar.setProfileId(accessToken.getUserId());
        }

        // main layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // slide menu layout
        slideMenu_frame = (RelativeLayout) findViewById(R.id.slideMenu_frame);
        // content layout
        content_frame = (RelativeLayout) findViewById(R.id.content_frame);
        // slide menu list container
        slideMenuList = (ListView) findViewById(R.id.slideMenu_items);

        RelativeLayout profileBox = (RelativeLayout) findViewById(R.id.profileBox);
        profileBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to lo login
                Intent intent = new Intent(SlideMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


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

        // Show the menu icon on top of the screen
        // More info: http://codetheory.in/difference-between-setdisplayhomeasupenabled-sethomebuttonenabled-and-setdisplayshowhomeenabled/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.openSlideMenu, R.string.closeSlideMenu) {
            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(View view) {
                Log.d(TAG, "Slide menu closed");
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "Slide menu opened");
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    // slide menu actions

    /**
     * Handle item click, depending on its position.
     * <p>
     *     If the item has a linkedActivity, the activity will be launched.
     * </p>
     * <p>
     *     If the item has an action, the action will be run.
     * </p>
     *
     * @param position
     */
    private void selectItemFromList(int position) {

        if(DataManager.getUser().getString("phone") == null || DataManager.getUser().getString("phone").equals("")){
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
            showAlertDialogForPhoneNumber();
            return;
        }

        Log.d(TAG, "SlideMenu item selected. N° " + position + " : " + slideMenuItems.get(position).name);
        //close Menu
        mDrawerLayout.closeDrawer(slideMenu_frame);
        switch (position) {
            default:
                if (slideMenuItems.get(position).action != null){
                    slideMenuItems.get(position).action.run();
                }
                if (slideMenuItems.get(position).linkedActivity != null){
                    Intent newActivity = new Intent(getApplicationContext(), slideMenuItems.get(position).linkedActivity);
                    startActivity(newActivity);
                }
        }
    }

    private void showAlertDialogForPhoneNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SlideMenuActivity.this);
        builder.setTitle("Enter your number phone before using the app");

// Set up the input
        final EditText input = new EditText(SlideMenuActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseUser currentUser = DataManager.getUser();
                String textInput = input.getText().toString();
                currentUser.put("phone",textInput);
                currentUser.saveInBackground();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /**
     * Set the activity content_frame (the main content view in the drawer) from a layout resource.  The resource will be
     * inflated, adding all top-level views to the activity.
     *
     * @param layoutResID Resource ID to be inflated.
     *
     * @see #setContentView(android.view.View)
     * @see #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        content_frame.addView(getLayoutInflater().inflate(layoutResID, null));
    }

    // force display of the 3 bar icon on the Action Bar, the "logo" widely used for Menu
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
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
        /*
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(slideMenu_frame);
        // If the nav drawer is open, hide action items related to the content view
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        */
        return super.onPrepareOptionsMenu(menu);
    }
}

