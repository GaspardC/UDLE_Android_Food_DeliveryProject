package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

public class RecapActivity extends AppCompatActivity {
    AlertDialog.Builder dlgAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlgAlert = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_recap);

        OrderElement order = Orders.getActiveOrder();
        String moneyDevise = Orders.getMoneyDevise();


        String userName = order.getOrderedUserName();
        TextView deliveryName = (TextView) findViewById(R.id.RecapActivity_deliveryName);
        deliveryName.setText(userName);

        String address = order.getDeliveryAddress();
        TextView deliveryAddress = (TextView) findViewById(R.id.RecapActivity_deliveryAddress);
        deliveryAddress.setText(address);

        TextView priceTextView = (TextView) findViewById(R.id.RecapActivity_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + moneyDevise);


        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list);
        DrinkTypes.displayInRecap(list);


        ListAdapter adapter = new SimpleAdapter(this, list,
                                                R.layout.recap_elem_with_price,
                                                new String[] {"elem", "price", "options"},
                                                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.RecapActivity_recapListView);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Log.v("long clicked", "pos: " + pos);
                dlgAlert.setMessage("Voulez cous supprimer cette element?");
                dlgAlert.setTitle(R.string.app_name);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dlgAlert.create().dismiss();
                            }
                        });
                dlgAlert.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dlgAlert.create().dismiss();
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return true;
            }
        });
    }


    public void gotToPaymentActivity(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}
