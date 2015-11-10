package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.widget.ExpandableListView;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class OptionsFragment extends Fragment {

    private LinearLayout rlLayout;
    private ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("BBBBBBBBBBBBBBBBBBBBB", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();

        rlLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_options, container, false);
        Button buttonNext = (Button) rlLayout.findViewById(R.id.optionNext);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToDrinkActivity();
            }
        });

        ExpandableListView expandableListView = (ExpandableListView) rlLayout.findViewById(R.id.MenuElement_list);
        HashMap<String, List<String>> hashMap = Options_ExpandableListData.getData();
        ArrayList<String> hashMapKeys = new ArrayList<String>(hashMap.keySet());
        Options_ExpandableListAdapter adapter = new Options_ExpandableListAdapter(inflater, hashMap);
        expandableListView.setAdapter(adapter);

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
        Log.i("CCCCCCCCCCCCCCCCCCCCCCC", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        this.pager = pager;
    }
}
