package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class RecapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        OrderElement order = Orders.getActiveOrder();
        ListView listView = (ListView) findViewById(R.id.RecapActivity_recapListView);
        String moneyDevise = Orders.getMoneyDevise();

        //TO TEST
        order.addToDrinks(DrinkTypes.BEER);
        order.addToDrinks(DrinkTypes.COCA);
        for(Menu menu : order.getOrder()){
            menu.addToOptions(OptionsTypes.ALGERIENNE);
            menu.addToOptions(OptionsTypes.KETCHUP);
            menu.addToOptions(OptionsTypes.SALAD);
            menu.addToOptions(OptionsTypes.TOMATO);
        }
        //FINISH TEST

        String address = order.getDeliveryAddress();
        TextView deliveryAddress = (TextView) findViewById(R.id.RecapActivity_deliveryAddress);
        deliveryAddress.setText(address);

        TextView priceTextView = (TextView) findViewById(R.id.RecapActivity_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + moneyDevise);

        

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


    public void gotToPaymentActivity(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}
