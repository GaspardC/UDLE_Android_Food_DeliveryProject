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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        ImageView burgerImage = (ImageView)findViewById(R.id.burgerImage);
        startAnimation(burgerImage);

        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list);
        DrinkTypes.displayInRecap(list);

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.WaitingActivity_recapList);
        listView.setAdapter(adapter);

    }

    private void stopAnimation(ImageView image) {

        image.setAnimation(null);
//        image.setBackgroundColor(ContextCompat.getColor(this, R.color.validateColor));
//        image.setColorFilter(ContextCompat.getColor(this, R.color.validateColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        image.setImageResource(R.drawable.rainbowburger);
        TextView textInfo = (TextView) findViewById(R.id.textView7);
        textInfo.setText(R.string.infotextwaitingConfirmed);

    }

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
            stopAnimation((ImageView)findViewById(R.id.burgerImage));
            }
        }, 6000);
    }

    public void orderAccepted_button_click(View view) {
        Intent intent =  new Intent(this, DeliveryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(WaitingActivity.this)
                .setTitle(R.string.TitleAlertBack)
                .setMessage(R.string.MessageAlertBack)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent =  new Intent(WaitingActivity.this, MapActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
