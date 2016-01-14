package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;


public class PaymentActivity extends SlideMenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        OrderElement order = Orders.getActiveOrder();
        String moneyDevise = Orders.getMoneyDevise();

        TextView priceTextView = (TextView) findViewById(R.id.PaymentActivity_totalCost);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + moneyDevise);

        List<HashMap<String, String>> list = new ArrayList<>();
        Menu.displayInRecap(list, getResources().getString(R.string.noOptions), getResources().getString(R.string.options));
        DrinkTypes.displayInRecap(list);

        ListAdapter adapter = new SimpleAdapter(this, list,
                R.layout.recap_elem_with_price,
                new String[] {"elem", "price", "options"},
                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        ListView listView = (ListView) findViewById(R.id.PaymentActivity_recap);
        listView.setAdapter(adapter);

    }


    public void payment_button_click(View view) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("Confirm the payment");

        adb.setIcon(android.R.drawable.ic_menu_send);

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                processPayment();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        adb.show();
    }

    private void processPayment() {

        if(DataManager.getCustomerId() == null){
            Intent intent = new Intent(this,CreditCardActivity.class);
            intent.putExtra("from","payment");
            startActivity(intent);
            return;
        }
        else{
            String customerId = DataManager.getCustomerId();
            double totalCost = Orders.getActiveOrder().getTotalCost();
            int totalInCents = (int) totalCost * 100;
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("customerId", customerId);
            params.put("price",totalInCents);


            ParseCloud.callFunctionInBackground("payment", params, new FunctionCallback<Object>() {
                @Override
                public void done(Object o, ParseException e) {
                    if (e == null) {
                        Log.d("Main Activity", "Cloud Response: " + o.toString());
                        Toast.makeText(getApplicationContext(),"Payment cancelled",Toast.LENGTH_SHORT).show();
                    }
                    if (o != null) {
                        DataManager.createNewParseUserOrderInformations();
                        Intent intent =  new Intent(PaymentActivity.this, WaitingActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
        /*if(cardNumber.getText().toString().length() < 4 || cardExpDate.getText().toString().length() < 4 || cardSecurityNumber.getText().toString().length() < 4){
            Toast.makeText(getApplicationContext(), getString(R.string.NoCardInformation),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            //Return objectId from
            //Store it in OrderElement
            //function to get status based on orderId
            DataManager.createNewParseUserOrderInformations();

            Intent intent =  new Intent(this, WaitingActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(PaymentActivity.this)
                .setTitle(R.string.TitleAlertBack)
                .setMessage(R.string.MessageAlertBack)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Intent intent =  new Intent(PaymentActivity.this, MapActivity.class);
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
