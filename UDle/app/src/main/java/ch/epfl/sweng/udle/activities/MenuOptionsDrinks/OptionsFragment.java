package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

public class OptionsFragment extends Fragment {

    private LinearLayout layout;
    private ViewPager pager;
    private Options_ExpandableListAdapter adapter;
    private Button buttonNext;
    private ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        layout = (LinearLayout)    inflater.inflate(R.layout.activity_options, container, false);
        buttonNext = (Button) layout.findViewById(R.id.optionNext);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToDrinkActivity();
            }
        });

        expandableListView = (ExpandableListView) layout.findViewById(R.id.MenuElement_list);

        TextView infoTextView = (TextView) layout.findViewById(R.id.clickOnYourMeal);
        ImageView dottedLIne = (ImageView) layout.findViewById(R.id.dottedLine);
        dottedLIne.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        boolean expandList = false;
        adapter = new Options_ExpandableListAdapter(inflater,infoTextView,expandList);
        expandableListView.setAdapter(adapter);



        return layout;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            adapter.notifyDataSetChanged();
            buttonNext.setVisibility(View.VISIBLE);


            int count = Orders.getActiveOrder().getMenus().size();
            if (count >= 1){
                expandableListView.expandGroup(0);
            }
            for (int i = 1; i <count ; i++)
                expandableListView.collapseGroup(i);

        }
        else {
            if(layout!=null){
                buttonNext.setVisibility(View.GONE);

            }

        }
    }


    /** Called when the user clicks the MapActivity button */
    public void goToDrinkActivity() {
        if(DataManager.checkAllMandatoryOptions()){
            pager.setCurrentItem(2);
        }
        else{
            new AlertDialog.Builder(this.getContext())
                    .setMessage("Choisissez Frites ou Potatoes pour chaque menu :)")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }



    public void setPager(ViewPager pager){
        this.pager = pager;
    }
}
