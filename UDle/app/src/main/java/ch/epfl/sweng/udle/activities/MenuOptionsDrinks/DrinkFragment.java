package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.RecapActivity;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DrinkFragment extends Fragment {

    private RelativeLayout rlLayout;

    private int nbrCoca = 0;
    private int nbrOrang = 0;
    private int nbrWater = 0;
    private int nbrBeer = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rlLayout =  (RelativeLayout) inflater.inflate(R.layout.activity_drink, container, false);
        cocaInit();
        orangInit();
        waterInit();
        beerInit();
        Button buttonNext = (Button) rlLayout.findViewById(R.id.drinkNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRecapActivity();
            }
        });

        return rlLayout;

    }

    private void cocaInit() {
        Button cocaPlus = (Button) rlLayout.findViewById(R.id.cocaPlus);
        Button cocaMinus = (Button) rlLayout.findViewById(R.id.cocaMinus);
        computeCocaPrice(0);

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
        computeOrangPrice(0);

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
        computeBeerPrice(0);

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
        computeWaterPrice(0);

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
    }

    public void goToRecapActivity(){
        Intent intent = new Intent(getActivity().getBaseContext(),
                RecapActivity.class);
        getActivity().startActivity(intent);
    }
}
