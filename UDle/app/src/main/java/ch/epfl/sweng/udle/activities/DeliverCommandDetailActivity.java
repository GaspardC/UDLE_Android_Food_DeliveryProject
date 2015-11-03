package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class DeliverCommandDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_command_detail);

        OrderElement order = Orders.getActiveOrder();
        if (order == null){
            for (OrderElement orders : Orders.getCurrentOrders()){
                if (orders.getDeliveryAddress().equals(getIntent().getExtras().getString("Address"))){ //TODO: Instead of compare with the address, compare with the id of the command for example.
                    order = orders;
                    findViewById(R.id.DeliverCommandDetail_acceptCommand).setVisibility(View.GONE);
                    findViewById(R.id.DeliverCommandDetail_time).setVisibility(View.INVISIBLE);
                    findViewById(R.id.DeliverCommandDetail_commandDelivered).setVisibility(View.VISIBLE);
                }
            }
        }

        ListView listView = (ListView) findViewById(R.id.DeliverCommandDetail_recapListView);
        String moneyDevise = Orders.getMoneyDevise();



        TextView priceTextView = (TextView) findViewById(R.id.DeliverCommandDetail_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + moneyDevise);

        TextView deliveryAddress = (TextView) findViewById(R.id.DeliverCommandDetail_deliveryAddress);
        deliveryAddress.setText(order.getDeliveryAddress());



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


    public void acceptCommand(View view) {
        EditText expectedTime = (EditText) findViewById(R.id.DeliverCommandDetail_expectedTime);
        if (expectedTime.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.expectedTimeNotValid),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, DeliveryRestaurantMapActivity.class);
            startActivity(intent);
        }
    }
    public void commandDelivered(View view){

        Intent intent = new Intent(this, DeliveryRestaurantMapActivity.class);
        startActivity(intent);
    }
}
