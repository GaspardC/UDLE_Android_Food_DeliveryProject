package ch.epfl.sweng.udle.activities;

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
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class WaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        ImageView burgerImage = (ImageView)findViewById(R.id.burgerImage);
        startAnimation(burgerImage);


        OrderElement order = Orders.getActiveOrder();
        ListView listView = (ListView) findViewById(R.id.WaitingActivity_recapList);
        String moneyDevise = Orders.getMoneyDevise();


        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> element;

        for(Menu menu : order.getOrder()){
            String food = "1 " + menu.getFood().toString();
            String price = String.format("%.2f", menu.getFood().getPrice());
            price = price + moneyDevise;

            String option = "";
            for( OptionsTypes opt : menu.getOptions()){
                option = option + opt.toString() + " ; " ;
            }

            element = new HashMap<>();
            element.put("elem", food);
            element.put("price", price);
            element.put("options", option);

            list.add(element);
        }
        for(DrinkTypes drinkType : order.getDrinks()){
            String drink = "1 " + drinkType.toString();
            String price = String.format("%.2f", drinkType.getPrice());
            price = price + moneyDevise;

            element = new HashMap<>();
            element.put("elem", drink);
            element.put("price", price);
            element.put("options", "");

            list.add(element);
        }


        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
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
}
