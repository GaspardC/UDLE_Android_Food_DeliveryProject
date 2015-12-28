package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.RecapActivity;


public class DrinkFragment extends Fragment {

    private LinearLayout lLayout;

    private int nbrCoca = 0;
    private int nbrOrang = 0;
    private int nbrWater = 0;
    private int nbrBeer = 0;

    private RecyclerView rv;
    private List<Item> items;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lLayout =   (LinearLayout) inflater.inflate(R.layout.recyclerview_activity, container, false);
        getActivity().setContentView(R.layout.recyclerview_activity);
        rv = (RecyclerView) getActivity().findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

       /* if(lLayout.getVisibility() == View.VISIBLE){
            initializeData();
            initializeAdapter();

        }else{*/
            rv.setVisibility(View.INVISIBLE);




/*        cocaInit();
        orangInit();
        waterInit();
        beerInit();*/


        return lLayout;
    }


    private void initializeData(){
        String devise = Orders.getMoneyDevise();
        items = new ArrayList<>();
        items.add(new Item("Coca", "2"+ devise, R.drawable.coca_cola));
        items.add(new Item("Orangina","2"+ devise, R.drawable.orangina));
        items.add(new Item("Water", "2"+ devise, R.drawable.evian));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(items);
        rv.setAdapter(adapter);
    }


    /*private void cocaInit() {
        Button cocaPlus = (Button) rlLayout.findViewById(R.id.cocaPlus);
        Button cocaMinus = (Button) rlLayout.findViewById(R.id.cocaMinus);

        int nbr = 0;
        for (DrinkTypes drink :Orders.getActiveOrder().getDrinks()){
            if (drink.toString() == DrinkTypes.COCA.toString()){
                nbr ++;
            }
        }
        nbrCoca = nbr;
        computeCocaPrice(nbrCoca);

        cocaPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.addToDrinks(DrinkTypes.COCA);
                nbrCoca++;
                computeCocaPrice(nbrCoca);
            }
        });
        cocaMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.removeToDrinks(DrinkTypes.COCA);
                if (nbrCoca != 0) {
                    nbrCoca--;
                }
                computeCocaPrice(nbrCoca);
            }
        });
    }

    private void orangInit() {
        Button orangPlus = (Button) rlLayout.findViewById(R.id.orangPlus);
        Button orangMinus = (Button) rlLayout.findViewById(R.id.orangMinus);

        int nbr = 0;
        for (DrinkTypes drink :Orders.getActiveOrder().getDrinks()){
            if (drink.toString() == DrinkTypes.ORANGINA.toString()){
                nbr ++;
            }
        }
        nbrOrang = nbr;
        computeOrangPrice(nbrOrang);

        orangPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.addToDrinks(DrinkTypes.ORANGINA);
                nbrOrang++;
                computeOrangPrice(nbrOrang);
            }
        });
        orangMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.removeToDrinks(DrinkTypes.ORANGINA);
                if (nbrOrang != 0) {
                    nbrOrang--;
                }
                computeOrangPrice(nbrOrang);
            }
        });
    }

    private void beerInit() {
        Button beerPlus = (Button) rlLayout.findViewById(R.id.beerPlus);
        Button beerMinus = (Button) rlLayout.findViewById(R.id.beerMinus);

        int nbr = 0;
        for (DrinkTypes drink :Orders.getActiveOrder().getDrinks()){
            if (drink.toString() == DrinkTypes.BEER.toString()){
                nbr ++;
            }
        }
        nbrBeer= nbr;
        computeBeerPrice(nbrBeer);

        beerPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.addToDrinks(DrinkTypes.BEER);
                nbrBeer++;
                computeBeerPrice(nbrBeer);
            }
        });
        beerMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.removeToDrinks(DrinkTypes.BEER);
                if (nbrBeer != 0) {
                    nbrBeer--;
                }
                computeBeerPrice(nbrBeer);
            }
        });
    }


    private void waterInit() {
        Button waterPlus = (Button) rlLayout.findViewById(R.id.waterPlus);
        Button waterMinus = (Button) rlLayout.findViewById(R.id.waterMinus);

        int nbr = 0;
        for (DrinkTypes drink :Orders.getActiveOrder().getDrinks()){
            if (drink.toString() == DrinkTypes.WATER.toString()){
                nbr ++;
            }
        }
        nbrWater = nbr;
        computeWaterPrice(nbrWater);

        waterPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.addToDrinks(DrinkTypes.WATER);
                nbrWater++;
                computeWaterPrice(nbrWater);
            }
        });
        waterMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OrderElement orderElement = Orders.getActiveOrder();
                orderElement.removeToDrinks(DrinkTypes.WATER);
                if (nbrWater != 0) {
                    nbrWater--;
                }
                computeWaterPrice(nbrWater);
            }
        });
    }


    private void computeCocaPrice(int nbr){
        double cocaPrice = DrinkTypes.COCA.getPrice();
        double price = nbr*cocaPrice;
        TextView cocaPriceText = (TextView) rlLayout.findViewById(R.id.cocaTotal);
        cocaPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
        TextView cocaNbr = (TextView) rlLayout.findViewById(R.id.cocaNbr);
        cocaNbr.setText(Integer.toString(nbrCoca));
    }

    private void computeOrangPrice(int nbr){
        double orangPrice = DrinkTypes.ORANGINA.getPrice();
        double price = nbr*orangPrice;
        TextView orangPriceText = (TextView) rlLayout.findViewById(R.id.orangTotal);
        orangPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
        TextView orangNbr = (TextView) rlLayout.findViewById(R.id.orangNbr);
        orangNbr.setText(Integer.toString(nbrOrang));
    }

    private void computeBeerPrice(int nbr){
        double beerPrice = DrinkTypes.BEER.getPrice();
        double price = nbr*beerPrice;
        TextView beerPriceText = (TextView) rlLayout.findViewById(R.id.beerTotal);
        beerPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
        TextView beerNbr = (TextView) rlLayout.findViewById(R.id.beerNbr);
        beerNbr.setText(Integer.toString(nbrBeer));

    }

    private void computeWaterPrice(int nbr){
        double waterPrice = DrinkTypes.WATER.getPrice();
        double price = nbr*waterPrice;
        TextView waterPriceText = (TextView) rlLayout.findViewById(R.id.waterTotal);
        waterPriceText.setText(Double.toString(price) + Orders.getMoneyDevise());
        TextView waterNbr = (TextView) rlLayout.findViewById(R.id.waterNbr);
        waterNbr.setText(Integer.toString(nbrWater));
    }*/

    public void goToRecapActivity(){
        Intent intent = new Intent(getActivity().getBaseContext(),
                RecapActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                if (rv == null) {
                    lLayout =   (LinearLayout) (getActivity().findViewById(R.id.llRv));
                    getActivity().setContentView(R.layout.recyclerview_activity);
                    rv = (RecyclerView) getActivity().findViewById(R.id.rv);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    rv.setLayoutManager(llm);
                    rv.setHasFixedSize(true);
                }
                initializeData();
                initializeAdapter();
                rv.setVisibility(View.VISIBLE);

            } else {
                if(rv!= null){
                    rv.setVisibility(View.INVISIBLE);
                }
            }
    }
}
