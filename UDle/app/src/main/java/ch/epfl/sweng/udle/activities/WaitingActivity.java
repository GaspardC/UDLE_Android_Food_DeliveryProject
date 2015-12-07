package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class WaitingActivity extends SlideMenuActivity {

    private static final long ANIMATION_TIME_BURGER_ROTATION = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        //start the rotating burger
        ImageView burgerImage = (ImageView)findViewById(R.id.burgerImage);
        startAnimation(burgerImage);

        setLittleRecap();
    }

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
        image.setImageResource(R.drawable.rainbowburger);

        //go to next activity : current Orders
        Intent intent =  new Intent(this, CurrentOrdersActivity.class);
        startActivity(intent);
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
}
