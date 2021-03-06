package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

public class WaitingActivity extends SlideMenuActivity {

    private static final long ANIMATION_TIME_BURGER_ROTATION = 3000;
    private Handler hd;
    private int countBurger = 0;
    private ImageView Burger1;
    private ImageView Burger2;
    private ImageView Burger3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        hd = new Handler();
        Burger1 = (ImageView) findViewById(R.id.waiting_burger1);
        Burger2 = (ImageView) findViewById(R.id.waiting_burger2);
        Burger3 = (ImageView) findViewById(R.id.waiting_burger3);

        startRepeatingTask();
        /*//start the rotating burger
        ImageView burgerImage = (ImageView)findViewById(R.id.burgerImage);
        startAnimation(burgerImage);*/

        setLittleRecap();
    }
    // UPDATING BTN TEXT DYNAMICALLY
    Runnable myRunnableUpdater = new Runnable()
    {
        public void run() {
            changeBurgerColor();
            hd.postDelayed(myRunnableUpdater, 1000);
        }
    };
    void startRepeatingTask()
    {
        myRunnableUpdater.run();
    }
    void stopRepeatingTask()
    {
        hd.removeCallbacks(myRunnableUpdater);
    }

    private void changeBurgerColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (countBurger == 10) {
                Burger1.setBackground(getDrawable(R.drawable.red_burger));

                Burger2.setBackground(getDrawable(R.drawable.red_burger));
                Burger3.setBackground(getDrawable(R.drawable.red_burger));
                stopRepeatingTask();
                return;

            }
            int i = countBurger % 3;
            switch (i) {
                case 0: {
                    Burger1.setBackground(getDrawable(R.drawable.burger_picto));
                    Burger2.setBackground(getDrawable(R.drawable.burger_picto));
                    Burger3.setBackground(getDrawable(R.drawable.red_burger));
                    break;

                }
                case 1: {
                    Burger1.setBackground(getDrawable(R.drawable.burger_picto));
                    Burger2.setBackground(getDrawable(R.drawable.red_burger));
                    Burger3.setBackground(getDrawable(R.drawable.burger_picto));
                    break;

                }
                case 2: {
                    Burger1.setBackground(getDrawable(R.drawable.red_burger));
                    Burger2.setBackground(getDrawable(R.drawable.burger_picto));
                    Burger3.setBackground(getDrawable(R.drawable.burger_picto));
                    break;

                }
            }
            countBurger += 1;
        }
        else{
            stopRepeatingTask();
            return;
        }
    }
    //END OF UPDATING BTN TEXT DYNAMICALLY!!

    /**
     * Use to display a brief recap just above the payment api
     */
    private void setLittleRecap() {
        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list,getResources().getString(R.string.noOptions),getResources().getString(R.string.options));
        DrinkTypes.displayInRecap(list);

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.WaitingActivity_recapList);
        listView.setAdapter(adapter);
    }

    /**
     * @param image  it is the logo of the app, the Udle burger
     * It is use to stop the image rotating and goes to the next activity, current orders
     *
     */
    private void stopAnimation(ImageView image) {

        image.setAnimation(null);
        image.setImageResource(R.drawable.fusee_burger);

/*        //go to next activity : current Orders
        Intent intent =  new Intent(this, CurrentOrdersActivity.class);
        startActivity(intent);*/
    }

    /**
     * @param image  it is the logo of the app, the UDle burger
     * Show the user that its payment has been register, make the burger rotate for 3 seconds
     */
    private void startAnimation(ImageView image) {
        // Step1 : create the  RotateAnimation object
        Animation anim = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        // Step 2:  Set the Animation properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(3);
        anim.setDuration(2000);

        // Step 3: Start animating the image
        image.startAnimation(anim);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAnimation((ImageView) findViewById(R.id.burgerImage));
            }
        }, ANIMATION_TIME_BURGER_ROTATION);
    }


    /**
     * Disable the back button at this stage, the payment has been already accepted
     */
    @Override
    public void onBackPressed() {

    }



    public void orderAccepted_button_click(View view) {
        //go to next activity : current Orders
        Intent intent =  new Intent(this, CurrentOrdersActivity.class);
        startActivity(intent);
    }
}
