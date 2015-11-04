package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;



import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class MenuFragment extends Fragment{


    private int nbrKebabs = 0;
    private int nbrBurgers = 0;
    private LinearLayout        llLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        llLayout =  (LinearLayout) inflater.inflate(R.layout.activity_menu, container, false);
        kebabInit();
        burgerInit();
        return llLayout;

    }


    private void kebabInit(){
        final TextView kebabNbr = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabNbr);
        kebabNbr.setText("" + 0);
        computeKebabPrice(0);

        TextView kebabPlus = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabPlus);
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

        TextView kebabMinus = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabMinus);
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
        TextView kebabPriceText = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabTotalMoney);
        kebabPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }




    private void burgerInit(){
        final TextView burgerNbr = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerNbr);
        burgerNbr.setText("" + 0);
        computeBurgerPrice(0);

        TextView burgerPlus = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerPlus);
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

        TextView burgerMinus = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerMinus);
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
        TextView burgerPriceText = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerTotalMoney);
        burgerPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }





    /** Called when the user clicks the MapActivity button */
    public void goToOptionsActivity(View view) {
        int nbrMenus = nbrKebabs + nbrBurgers;
        if(nbrMenus < 1){
            Toast.makeText(super.getActivity().getApplicationContext(), getString(R.string.NoMenuSelected),
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


//            MainActivity.adapter.getItem(1);

//            Intent intent = new Intent(super.getActivity(), OptionsFragment.class);
//            startActivity(intent);
        }
    }


}
