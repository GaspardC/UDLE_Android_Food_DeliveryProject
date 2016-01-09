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

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;


public class PaymentActivity extends SlideMenuActivity {

    private EditText cardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cardNumber = (EditText) findViewById(R.id.payment_cardNumber);
        //only for tests
        cardNumber.setText("4000000000000077");

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

    private void setUpStripe(EditText number, EditText expDate, EditText cardSecurityNumber) {
        String cardNumber = number.getText().toString();
        String expD = expDate.getText().toString();

        Integer cardExpMonth = Integer.valueOf(expD.substring(0, 2));
        Integer cardExpYear = Integer.valueOf(expD.substring(2, 4));
        String cardCVC =cardSecurityNumber.getText().toString();

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
            stripe = new Stripe("pk_test_If3q98H3IFSDM2FfjyfWMBAS");


            stripe.createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            // Send token to your server
                            Log.d("token", token.toString());

                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("cardToken", token.getId());
                            params.put("userId", DataManager.getUser().getObjectId());

                            ParseCloud.callFunctionInBackground("test", params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object o, ParseException e) {
                                    if(e == null){
                                        Log.d("Main Activity","Cloud Response: " + o.toString());
                                    }
                                }
                            });

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
        EditText cardExpDate = (EditText) findViewById(R.id.payment_expDate);
        EditText cardSecurityNumber = (EditText) findViewById(R.id.payment_securityNumber);
        setUpStripe(cardNumber,cardExpDate,cardSecurityNumber);

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
