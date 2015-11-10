package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class OptionsFragment extends Fragment {

    private RelativeLayout rlLayout;
    private ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();

        rlLayout    = (RelativeLayout)    inflater.inflate(R.layout.activity_options, container, false);
        Button buttonNext = (Button) rlLayout.findViewById(R.id.optionNext);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToDrinkActivity();
            }
        });

        return rlLayout;

    }

    /** Called when the user clicks the MapActivity button */
    public void goToDrinkActivity() {
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menu = orderElement.getOrder();
        //TODO: use the function menu.addToOptions to add the selected options. Be careful to add the option with the correct menu (in case of >1 menu)


        //TODO If the user wants multiple menus, need to add specific options to each of this menus.
//        Intent intent = new Intent(super.getActivity(), DrinkFragment.class);
//        startActivity(intent);

        pager.setCurrentItem(2);



    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }
}
