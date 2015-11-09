package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
