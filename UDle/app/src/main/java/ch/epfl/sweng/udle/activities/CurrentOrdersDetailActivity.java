package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

/**
 * Based on DeliveryCommandDetailActivity
 */
public class CurrentOrdersDetailActivity extends AppCompatActivity {

    private OrderElement order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders_detail);

        order = Orders.getActiveOrder();

        TextView priceTextView = (TextView) findViewById(R.id.CurrentOrders_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + Orders.getMoneyDevise());

        TextView deliveryAddress = (TextView) findViewById(R.id.CurrentOrders_deliveryAddress);
        deliveryAddress.setText(order.getDeliveryAddress());


        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list);
        DrinkTypes.displayInRecap(list);

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.CurrentOrders_recapListView);
        listView.setAdapter(adapter);
    }

   public void back(View view){
       Intent intent = new Intent(this, CurrentOrdersActivity.class);
       startActivity(intent);
    }
}
