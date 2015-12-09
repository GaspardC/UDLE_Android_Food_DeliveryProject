package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class MenuFragment extends Fragment{


    private LinearLayout llLayout;
    private ViewPager pager;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        llLayout =  (LinearLayout) inflater.inflate(R.layout.activity_menu, container, false);
        kebabInit();
        burgerInit();
        Button buttonNext = (Button) llLayout.findViewById(R.id.MenuActivity_NextButton);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToOptionsActivity();
            }
        });

        return llLayout;

    }


    /**
     * If kebabs was already added to the order, add them and show the price.
     * If not, just set the price and the kebab numbers to 0
     */
    private void kebabInit(){
        OrderElement orderElement = Orders.getActiveOrder();
        int nbrKebab = 0;
        if (orderElement != null){
            for(Menu menu: orderElement.getOrder()){
                if (menu.getFood().toString().equals(FoodTypes.KEBAB.toString())){
                    nbrKebab ++;
                }
            }
        }
        final TextView kebabNbr = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabNbr);
        kebabNbr.setText("" + nbrKebab);
        computeKebabPrice(nbrKebab);


        //Set the Listener for the plus button
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
                    addOneMenu(FoodTypes.KEBAB);
                }
            }
        });

        //Set the Listener for the minus button
        TextView kebabMinus = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabMinus);
        kebabMinus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = kebabNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue - 1;
                if (newNbrValue >= 0) {
                    kebabNbr.setText(Integer.toString(newNbrValue));
                    computeKebabPrice(newNbrValue);
                    removeOneMenu(FoodTypes.KEBAB);
                }
            }
        });
    }

    /**
     * @param nbr number of kebab
     * Calcul of the total cost of kebabs
     */
    private void computeKebabPrice(int nbr){
        double kebabPrice = FoodTypes.KEBAB.getPrice();
        double price = nbr*kebabPrice;
        TextView kebabPriceText = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabTotalMoney);
        kebabPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }


    /**
     *  If burgers was already added to the order, add them and show the price.
     * If not, just set the price and the burger numbers to 0
     */
    private void burgerInit(){
        //If burgers were already added to the order, add them and show the price.
        //If not, just set the price and the burger numbers to 0
        OrderElement orderElement = Orders.getActiveOrder();
        int nbrBurger = 0;
        if (orderElement != null){
            for(Menu menu: orderElement.getOrder()){
                if (menu.getFood().toString().equals(FoodTypes.BURGER.toString())){
                    nbrBurger ++;
                }
            }
        }

        final TextView burgerNbr = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerNbr);
        burgerNbr.setText("" + nbrBurger);
        computeBurgerPrice(nbrBurger);

        //Set the Listener for the plus button
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
                    addOneMenu(FoodTypes.BURGER);
                }

            }
        });
        //Set the Listener for the minus button
        TextView burgerMinus = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerMinus);
        burgerMinus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String value = burgerNbr.getText().toString();
                int actualValue = Integer.parseInt(value);
                int newNbrValue = actualValue - 1;
                if (newNbrValue >= 0) {
                    burgerNbr.setText(Integer.toString(newNbrValue));

                    computeBurgerPrice(newNbrValue);
                    removeOneMenu(FoodTypes.BURGER);
                }
            }
        });
    }

    /**
     * @param nbr number of burgers
     * Calcul of the burger price and display it
     */
    private void computeBurgerPrice(int nbr){
        double burgerPrice = FoodTypes.BURGER.getPrice();
        double price = nbr*burgerPrice;
        TextView burgerPriceText = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerTotalMoney);
        burgerPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }





    private void addOneMenu(FoodTypes foodTypes){
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menus = orderElement.getOrder();
        Menu newOne = new Menu();
        newOne.setFood(foodTypes);
        menus.add(newOne);
    }

    /**
     * @param foodTypes KEBAB or BURGER type
     *                  use to remove an item
     */
    private void removeOneMenu(FoodTypes foodTypes){
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menus = orderElement.getOrder();
        Boolean menuAlreadyRemoved = false;

        //Start at the end of the list in order to remove the last added Kebab
        for(int i=menus.size()-1 ; i >= 0 ; i--){
            if (menus.get(i).getFood().toString().equals(foodTypes.toString())){
                if (! menuAlreadyRemoved){
                    menuAlreadyRemoved = true;
                    menus.remove(menus.get(i));
                }
            }
        }
    }


    /**
     *  Called when the user clicks the 'Next' button
    */
    public void goToOptionsActivity() {
        pager.setCurrentItem(1);
    }


    /**
     * @param pager use to change the current fragment
     *              the parameter is set to then be used to change the current fragment
     */
    public void setPager(ViewPager pager) {
        this.pager = pager;
    }


}
