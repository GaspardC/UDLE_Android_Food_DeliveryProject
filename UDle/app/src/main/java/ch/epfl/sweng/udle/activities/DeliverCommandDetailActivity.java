package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

/**
 * Display a recap of the ordered click by a Restaurant user.
 * He can choose to deliver the order by entering an expected time for the delivered.
 *
 * If the Restaurant has already accept to deliver the order, he can come back to this page in order to
 * see the recap again.
 *
 * When the delivery was made, the Restaurant confirm it.
 */
public class DeliverCommandDetailActivity extends AppCompatActivity {

    private OrderElement order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_command_detail);

        //This boolean tell us if the restaurant as already accepted to deliver this order.
        //If yes -> Show the 'delete' order and remove the possibility to set deliveryTime.
        boolean isCurrent = getIntent().getExtras().getBoolean("isCurrent");
        if (isCurrent){
            findViewById(R.id.DeliverCommandDetail_acceptCommand).setVisibility(View.GONE);
            findViewById(R.id.DeliverCommandDetail_time).setVisibility(View.INVISIBLE);
            findViewById(R.id.DeliverCommandDetail_commandDelivered).setVisibility(View.VISIBLE);
        }

        order = Orders.getActiveOrder();

        //Set the total cost of the order
        TextView priceTextView = (TextView) findViewById(R.id.DeliverCommandDetail_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + Orders.getMoneyDevise());

        //Set the delivery name of the order's user
        TextView deliveryName = (TextView) findViewById(R.id.DeliverCommandDetail_deliveryName);
        deliveryName.setText(order.getOrderedUserName());

        //Set the delivery address of the order
        TextView deliveryAddress = (TextView) findViewById(R.id.DeliverCommandDetail_deliveryAddress);
        deliveryAddress.setText(order.getDeliveryAddress());



        //Initalize the ListView for the orders.
        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list, getResources().getString(R.string.noOptions),getResources().getString(R.string.options));
        DrinkTypes.displayInRecap(list);

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.DeliverCommandDetail_recapListView);
        listView.setAdapter(adapter);
    }


    /**
     * When a restaurant accept an order, need to inform the server and the client
     *
     * @param view Not useful for the purpose of this function
     */
    public void acceptCommand(View view) {
        EditText expectedTime = (EditText) findViewById(R.id.DeliverCommandDetail_expectedTime);
        if (expectedTime.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.expectedTimeNotValid),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            int eta = Integer.parseInt(expectedTime.getText().toString());
            if (eta <= 0){
                Toast.makeText(getApplicationContext(), getString(R.string.expectedTimeNegative),
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if (eta > 60){
                    Toast.makeText(getApplicationContext(), getString(R.string.expectedTimeTooBig),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    DataManager.deliveryEnRoute(order.getUserOrderInformationsID(), eta);
                    Intent intent = new Intent(this, DeliveryRestaurantMapActivity.class);
                    startActivity(intent);
                }
            }
        }
    }


    /**
     * When an order is deliverd by the restaurant, call the server to 'close' it.
     *
     * @param view Not useful for the purpose of this function
     */
    public void commandDelivered(View view){
        DataManager.deliveryDelivered(order.getUserOrderInformationsID());

        Intent intent = new Intent(this, DeliveryRestaurantMapActivity.class);
        startActivity(intent);
    }
}
