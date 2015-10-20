package ch.epfl.sweng.udle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private final double KEBABPRICE = 10.00;
    private final int MAXNBRKEBAB = 10;
    private final double BURGERPRICE = 10.00;
    private final int MAXNBRBURGER = 10;
    private final String MONEYDEVISE = " CHF";
    private int nbr = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        kebabInit();
        burgerInit();
    }

    private void kebabInit(){
        final TextView kebabNbr = (TextView) findViewById(R.id.MenuActivity_KebabNbr);
        kebabNbr.setText("" + nbr);
        computeKebabPrice(0);

        TextView kebabPlus = (TextView) findViewById(R.id.MenuActivity_KebabPlus);
        kebabPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = kebabNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue + 1;
                if (newNbrValue > MAXNBRKEBAB) {
                    newNbrValue = MAXNBRKEBAB;
                }
                kebabNbr.setText(Integer.toString(newNbrValue));

                computeKebabPrice(newNbrValue);
            }
        });

        TextView kebabMinus = (TextView) findViewById(R.id.MenuActivity_KebabMinus);
        kebabMinus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = kebabNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue - 1;
                if (newNbrValue < 0) {
                    newNbrValue = 0;
                }
                kebabNbr.setText(Integer.toString(newNbrValue));

                computeKebabPrice(newNbrValue);
            }
        });
    }

    private void computeKebabPrice(int nbr){
        double price = nbr*KEBABPRICE;
        TextView kebabPrice = (TextView)findViewById(R.id.MenuActivity_KebabTotalMoney);
        kebabPrice.setText(Double.toString(price) + MONEYDEVISE);
    }




    private void burgerInit(){
        final TextView burgerNbr = (TextView) findViewById(R.id.MenuActivity_BurgerNbr);
        burgerNbr.setText("" + nbr);
        computeBurgerPrice(0);

        TextView burgerPlus = (TextView) findViewById(R.id.MenuActivity_BurgerPlus);
        burgerPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = burgerNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue + 1;
                if (newNbrValue > MAXNBRBURGER) {
                    newNbrValue = MAXNBRBURGER;
                }
                burgerNbr.setText(Integer.toString(newNbrValue));

                computeBurgerPrice(newNbrValue);
            }
        });

        TextView burgerMinus = (TextView) findViewById(R.id.MenuActivity_BurgerMinus);
        burgerMinus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = burgerNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue - 1;
                if (newNbrValue < 0) {
                    newNbrValue = 0;
                }
                burgerNbr.setText(Integer.toString(newNbrValue));

                computeBurgerPrice(newNbrValue);
            }
        });
    }

    private void computeBurgerPrice(int nbr){
        double price = nbr*BURGERPRICE;
        TextView burgerPrice = (TextView)findViewById(R.id.MenuActivity_BurgerTotalMoney);
        burgerPrice.setText(Double.toString(price) + MONEYDEVISE);
    }





    /** Called when the user clicks the MapActivity button */
    public void goToOptionsActivity(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }


}
