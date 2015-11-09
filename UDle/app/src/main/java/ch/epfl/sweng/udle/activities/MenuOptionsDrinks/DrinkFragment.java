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

import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.RecapActivity;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;


public class DrinkFragment extends Fragment {

    private RelativeLayout rlLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rlLayout =  (RelativeLayout) inflater.inflate(R.layout.activity_drink, container, false);
        Button buttonNext = (Button) rlLayout.findViewById(R.id.drinkNext);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToRecapActivity();
            }
        });
        return rlLayout;

    }

    public void goToRecapActivity(){
        OrderElement orderElement = Orders.getActiveOrder();
        //TODO: orderElement.addToDrinks to add the choosen drinks (if any) to the order.

//        Intent intent = new Intent(super.getActivity(), RecapActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(getActivity().getBaseContext(),
                RecapActivity.class);
        getActivity().startActivity(intent);
    }
}
