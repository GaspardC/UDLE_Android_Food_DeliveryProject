package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.RecapActivity;






public class MenuFragment extends Fragment {

    private LinearLayout lLayout;

    private RecyclerView rv;
    private List<Item> items;
    private Button goToOptionsButton;
    private LinearLayout linearLayout;
    private ViewPager pager;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lLayout =   (LinearLayout) inflater.inflate(R.layout.recyclerview_activity_menu, container, false);
  /*      getActivity().setContentView(R.layout.recyclerview_activity_menu);
        rv = (RecyclerView) getActivity().findViewById(R.id.rv_menu);
        if(linearLayout == null){
            linearLayout =  (LinearLayout) getActivity().findViewById(R.id.llRv_menu);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);




        Button buttonNext = (Button) lLayout.findViewById(R.id.MenuActivity_NextButton);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToOptionsActivity();
            }
        });*/

        /*if(!this.isVisible()) {
            linearLayout.setVisibility(View.GONE);
        }*/




        return lLayout;
    }


    private void initializeData(){
        items = new ArrayList<>();
        int number = 0;
        items.add(new Item("Burger", 10, R.drawable.burger,number));
        items.add(new Item("Kebab",10, R.drawable.kebab,number));
        items.add(new Item("Pizza", 10, R.drawable.pizza,number));
    }

    private void initializeAdapter(){


        RVAdapterMenu adapter = new RVAdapterMenu(items);
        rv.setAdapter(adapter);
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (linearLayout == null) {
                getActivity().setContentView(R.layout.recyclerview_activity_menu);
                linearLayout =   (LinearLayout) (getActivity().findViewById(R.id.llRv_menu));
                rv = (RecyclerView) getActivity().findViewById(R.id.rv_menu);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);
                goToOptionsButton = (Button) getActivity().findViewById(R.id.MenuActivity_NextButton);
                goToOptionsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToOptionsActivity();
                    }
                });
            }
            initializeData();
            initializeAdapter();
            linearLayout.setVisibility(View.VISIBLE);

        } else {
            if(rv!= null){
                linearLayout.setVisibility(View.GONE);
            }
        }
    }


 //  Called when the user clicks the 'Next' button


    public void goToOptionsActivity() {
        pager.setCurrentItem(1);
    }




/*  @param pager use to change the current fragment
               the parameter is set to then be used to change the current fragment*/


    public void setPager(ViewPager pager) {
        this.pager = pager;
    }


}
/*
package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class MenuFragment extends Fragment{


    private LinearLayout llLayout;
    private ViewPager pager;
    private TextView kebabNbr;
    private ImageView imageKebab;
    private  ImageView imageBurger ;
    private TextView burgerNbr;


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


    */
/**
     * If kebabs was already added to the order, add them and show the price.
     * If not, just set the price and the kebab numbers to 0
     *//*

    private void kebabInit(){
        OrderElement orderElement = Orders.getActiveOrder();
        int nbrKebab = 0;
        if (orderElement != null){
            for(Menu menu: orderElement.getMenus()){
                if (menu.getFood().toString().equals(FoodTypes.KEBAB.toString())){
                    nbrKebab ++;
                }
            }
        }
       kebabNbr = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabNbr);
        kebabNbr.setText("" + nbrKebab);
        computeKebabPrice(nbrKebab);


        //Set the Listener for the plus button
        TextView kebabPlus = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabPlus);
        kebabPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                addKebab();
            }
        });
        imageKebab = (ImageView) llLayout.findViewById(R.id.MenuActivity_KebabImage);
        imageKebab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addKebab();
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

    private void addKebab() {
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

    */
/**
     * @param nbr number of kebab
     * Calcul of the total cost of kebabs
     *//*

    private void computeKebabPrice(int nbr){
        double kebabPrice = FoodTypes.KEBAB.getPrice();
        double price = nbr*kebabPrice;
        TextView kebabPriceText = (TextView) llLayout.findViewById(R.id.MenuActivity_KebabTotalMoney);
        kebabPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }


    */
/**
     *  If burgers was already added to the order, add them and show the price.
     * If not, just set the price and the burger numbers to 0
     *//*

    private void burgerInit(){
        //If burgers were already added to the order, add them and show the price.
        //If not, just set the price and the burger numbers to 0
        OrderElement orderElement = Orders.getActiveOrder();
        int nbrBurger = 0;
        if (orderElement != null){
            for(Menu menu: orderElement.getMenus()){
                if (menu.getFood().toString().equals(FoodTypes.BURGER.toString())){
                    nbrBurger ++;
                }
            }
        }

        burgerNbr = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerNbr);
        burgerNbr.setText("" + nbrBurger);
        computeBurgerPrice(nbrBurger);

        //Set the Listener for the plus button
        TextView burgerPlus = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerPlus);
        burgerPlus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                addBurger();

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
        imageBurger = (ImageView) llLayout.findViewById(R.id.MenuActivity_BurgerImage);
        imageBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBurger();
            }
        });
    }

    private void addBurger() {
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

    */
/**
     * @param nbr number of burgers
     * Calcul of the burger price and display it
     *//*

    private void computeBurgerPrice(int nbr){
        double burgerPrice = FoodTypes.BURGER.getPrice();
        double price = nbr*burgerPrice;
        TextView burgerPriceText = (TextView) llLayout.findViewById(R.id.MenuActivity_BurgerTotalMoney);
        burgerPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
    }





    private void addOneMenu(FoodTypes foodTypes){
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menus = orderElement.getMenus();
        Menu newOne = new Menu();
        newOne.setFood(foodTypes);
        menus.add(newOne);
    }

    */
/**
     * @param foodTypes KEBAB or BURGER type
     *                  use to remove an item_drink
     *//*

    private void removeOneMenu(FoodTypes foodTypes){
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menus = orderElement.getMenus();
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


    */
/**
     *  Called when the user clicks the 'Next' button
    *//*

    public void goToOptionsActivity() {
        pager.setCurrentItem(1);
    }


    */
/**
     * @param pager use to change the current fragment
     *              the parameter is set to then be used to change the current fragment
     *//*

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }


}
*/
