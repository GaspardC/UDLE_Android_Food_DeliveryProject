package ch.epfl.sweng.udle.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class MenuActivity extends AppCompatActivity {


    private int nbrKebabs = 0;
    private int nbrBurgers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        kebabInit();
        burgerInit();
    }

    private void kebabInit(){
        final TextView kebabNbr = (TextView) findViewById(R.id.MenuActivity_KebabNbr);
        kebabNbr.setText("" + 0);
        computeKebabPrice(0);

        TextView kebabPlus = (TextView) findViewById(R.id.MenuActivity_KebabPlus);
        kebabPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int maxNbr = FoodTypes.KEBAB.getMaxNbr();

                String value = kebabNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue + 1;
                if (newNbrValue <= maxNbr) {
                    kebabNbr.setText(Integer.toString(newNbrValue));
                    computeKebabPrice(newNbrValue);
                    nbrKebabs++;
                }
            }
        });

        TextView kebabMinus = (TextView) findViewById(R.id.MenuActivity_KebabMinus);
        kebabMinus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = kebabNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue - 1;
                if (newNbrValue >= 0) {
                    kebabNbr.setText(Integer.toString(newNbrValue));
                    computeKebabPrice(newNbrValue);
                    nbrKebabs --;
                }
            }
        });
    }

    private void computeKebabPrice(int nbr){
        double kebabPrice = FoodTypes.KEBAB.getPrice();
        double price = nbr*kebabPrice;
        TextView kebabPriceText = (TextView)findViewById(R.id.MenuActivity_KebabTotalMoney);
        kebabPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }




    private void burgerInit(){
        final TextView burgerNbr = (TextView) findViewById(R.id.MenuActivity_BurgerNbr);
        burgerNbr.setText("" + 0);
        computeBurgerPrice(0);

        TextView burgerPlus = (TextView) findViewById(R.id.MenuActivity_BurgerPlus);
        burgerPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int maxNbr = FoodTypes.BURGER.getMaxNbr();
                String value = burgerNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue + 1;
                if (newNbrValue <= maxNbr) {
                    burgerNbr.setText(Integer.toString(newNbrValue));

                    computeBurgerPrice(newNbrValue);
                    nbrBurgers++;
                }
            }
        });

        TextView burgerMinus = (TextView) findViewById(R.id.MenuActivity_BurgerMinus);
        burgerMinus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = burgerNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue - 1;
                if (newNbrValue >= 0) {
                    burgerNbr.setText(Integer.toString(newNbrValue));

                    computeBurgerPrice(newNbrValue);
                    nbrBurgers --;
                }
            }
        });
    }

    private void computeBurgerPrice(int nbr){
        double burgerPrice = FoodTypes.BURGER.getPrice();
        double price = nbr*burgerPrice;
        TextView burgerPriceText = (TextView)findViewById(R.id.MenuActivity_BurgerTotalMoney);
        burgerPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }





    /** Called when the user clicks the MapActivity button */
    public void goToOptionsActivity(View view) {
        int nbrMenus = nbrKebabs + nbrBurgers;
        if(nbrMenus < 1){
            Toast.makeText(getApplicationContext(), getString(R.string.NoMenuSelected),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            OrderElement orderElement = Orders.getActiveOrder();
            orderElement.empty();

            for (int i=0; i<nbrKebabs; i++){
                Menu menu = new Menu();
                menu.setFood(FoodTypes.KEBAB);
                orderElement.addMenu(menu);
            }
            for (int i=0; i<nbrBurgers; i++){
                Menu menu = new Menu();
                menu.setFood(FoodTypes.BURGER);
                orderElement.addMenu(menu);
            }

            Intent intent = new Intent(this, OptionsActivity.class);
            startActivity(intent);
        }
    }


}
