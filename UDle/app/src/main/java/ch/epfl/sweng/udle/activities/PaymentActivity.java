package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;


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

        setUpStripe();
    }

    private void setUpStripe() {
        String cardNumber = null;
        Integer cardExpMonth = null;
        Integer cardExpYear = null;
        String cardCVC = null;
        Card card = new Card(
                cardNumber,
                cardExpMonth,
                cardExpYear,
                cardCVC
        );

        card.validateNumber();
        card.validateCVC();

        if ( !card.validateCard() ) {
            // Show errors
            Toast.makeText(this,"Number Invalid",Toast.LENGTH_SHORT).show();
        }

        Stripe stripe = null;
        try {
            stripe = new Stripe("pk_test_6pRNASCoBOKtIshFeQd4XMUh");

        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(PaymentActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        } catch (AuthenticationException e) {
            Toast.makeText(PaymentActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void payment_button_click(View view) {
        EditText cardNumber = (EditText) findViewById(R.id.payment_cardNumber);
        EditText cardExpDate = (EditText) findViewById(R.id.payment_expDate);
        EditText cardSecurityNumber = (EditText) findViewById(R.id.payment_securityNumber);

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
