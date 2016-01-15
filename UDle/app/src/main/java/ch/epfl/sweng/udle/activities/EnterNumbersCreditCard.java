package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import java.util.HashMap;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;
import ch.epfl.sweng.udle.network.DataManager;

public class EnterNumbersCreditCard extends SlideMenuActivity {

    private Button validateButton;
    private EditText cardExpDate;
    private EditText cardSecurityNumber;
    private EditText cardNumber;
    private boolean directPayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_numbers_credit_card);
        cardExpDate = (EditText) findViewById(R.id.payment_expDate);
        cardSecurityNumber = (EditText) findViewById(R.id.payment_securityNumber);
        cardNumber = (EditText) findViewById(R.id.payment_cardNumber);
        validateButton = (Button) findViewById(R.id.validateNewCreditCard);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if(bundle.getString("from").equals("payment")){
                directPayment = true;
            }
        }
        setButtonBehavor();
    }

    private void setButtonBehavor() {
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setUpStripe(cardNumber, cardExpDate, cardSecurityNumber);
            }
        });
    }

    private void setUpStripe(EditText number, EditText expDate, EditText cardSecurityNumber) {

        //only for tests
        cardNumber.setText("4000000000000077");
        String cardNumber = number.getText().toString();
        String expD = expDate.getText().toString();

        if(expD.length() < 4) return;
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
            Toast.makeText(this, "Number Invalid", Toast.LENGTH_SHORT).show();
        }

        Stripe stripe = null;
        try {
            //Stripe test public Key
            stripe = new Stripe("pk_test_If3q98H3IFSDM2FfjyfWMBAS");


            stripe.createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            // Send token to your server
                            Log.d("token", token.toString());
                            DataManager.saveLast4(token.getCard().getLast4());

                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("cardToken", token.getId());
                            params.put("userId", DataManager.getUser().getObjectId());

                            ParseCloud.callFunctionInBackground("registerCustomer", params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object o, ParseException e) {
                                    if (e == null) {
                                        Log.d("Main Activity", "Cloud Response: " + o.toString());
                                    }
                                    if(o!=null){
                                        onBackPressed();
                                    }
                                }
                            });

                        }
                        public void onError(Exception error) {
                            // Show localized error message
                            Toast.makeText(EnterNumbersCreditCard.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
            );
        } catch (AuthenticationException e) {
            Toast.makeText(EnterNumbersCreditCard.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        if (directPayment){
            Intent intent = new Intent(this,EnterNumbersCreditCard.class);
            startActivity(intent);
        }
        else{
            super.onBackPressed();
        }
    }
}
