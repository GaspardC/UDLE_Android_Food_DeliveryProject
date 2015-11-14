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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class DeliverCommandDetailActivity extends AppCompatActivity {

    private OrderElement order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_command_detail);

        order = Orders.getActiveOrder();
        if (Orders.getCurrentOrders().contains(order)){
            findViewById(R.id.DeliverCommandDetail_acceptCommand).setVisibility(View.GONE);
            findViewById(R.id.DeliverCommandDetail_time).setVisibility(View.INVISIBLE);
            findViewById(R.id.DeliverCommandDetail_commandDelivered).setVisibility(View.VISIBLE);
        }

        TextView priceTextView = (TextView) findViewById(R.id.DeliverCommandDetail_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + Orders.getMoneyDevise());

        TextView deliveryAddress = (TextView) findViewById(R.id.DeliverCommandDetail_deliveryAddress);
        deliveryAddress.setText(order.getDeliveryAddress());


        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list);
        DrinkTypes.displayInRecap(list);

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.DeliverCommandDetail_recapListView);
        listView.setAdapter(adapter);
    }


    public void acceptCommand(View view) {
        EditText expectedTime = (EditText) findViewById(R.id.DeliverCommandDetail_expectedTime);
        if (expectedTime.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.expectedTimeNotValid),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Orders.activeOrderToCurrentOrder(order);
            //TODO: DataManager -> deliveryEnRoute (Issue #66)
            Intent intent = new Intent(this, DeliveryRestaurantMapActivity.class);
            startActivity(intent);
        }
    }
    public void commandDelivered(View view){
        Orders.currentOrderFinished(order);
        //TODO: DataManager -> deliveryDelivered  (Issue #67)
        Intent intent = new Intent(this, DeliveryRestaurantMapActivity.class);
        startActivity(intent);
    }
}
