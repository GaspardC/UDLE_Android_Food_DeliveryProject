package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import ch.epfl.sweng.udle.HorizontalSlideLibrary.SlidingTabLayout;
import ch.epfl.sweng.udle.R;

public class MenuFragment extends Fragment{


    private LinearLayout        llLayout;
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
                    addOneKebab();
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
                    removeOneKebab();
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
                    nbrBurgers--;
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
    public void goToOptionsActivity() {
        int nbrMenus = nbrKebabs + nbrBurgers;
        if(nbrMenus < 1){
            Toast.makeText(super.getActivity().getApplicationContext(), getString(R.string.NoMenuSelected),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            OrderElement orderElement = Orders.getActiveOrder();

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
            Orders.setActiveOrder(orderElement);
            Log.i("KKKKKKKKKKKK", "Active Order set to the correct one");
            pager.setCurrentItem(1);
        }
    }


    public void setPager(ViewPager pager) {
        this.pager = pager;
    }


}
