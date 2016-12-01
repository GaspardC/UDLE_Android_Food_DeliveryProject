package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.RecapActivity;
import ch.epfl.sweng.udle.network.DataManager;


public class DrinkFragment extends Fragment {

    private LinearLayout lLayout;

    private RecyclerView rv;
    private List<Item> items;
    private Button goToRecapButton;
    private LinearLayout linearLayout;
    private ViewPager pager;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lLayout =   (LinearLayout) inflater.inflate(R.layout.recyclerview_activity_drink, container, false);
   /*     getActivity().setContentView(R.layout.recyclerview_activity_drink);
        rv = (RecyclerView) getActivity().findViewById(R.id.rv_drink);
        if(linearLayout == null){
            linearLayout =  (LinearLayout) getActivity().findViewById(R.id.llRv_drink);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);




        goToRecapButton = (Button) getActivity().findViewById(R.id.DrinkActivity_NextButton);
        goToRecapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRecapActivity();
            }
        });

        if(!this.isVisible()) {
            linearLayout.setVisibility(View.GONE);
        }*/




        return lLayout;
    }


    private void initializeData(){
        items = new ArrayList<>();
        int number = 0;
        items.add(new Item("Coca", 0, R.drawable.coca,number));
        items.add(new Item("Coca_zero",0, R.drawable.coca_zero,number));
        items.add(new Item("Fanta", 0, R.drawable.sprite,number));
        items.add(new Item("Sprite", 0, R.drawable.fanta,number));
        items.add(new Item("Ice_Tea", 0, R.drawable.ice_tea,number));

    }

    private void initializeAdapter(){
        RVAdapterDrinks adapter = new RVAdapterDrinks(items,this);
        rv.setAdapter(adapter);
    }




    public void goToRecapActivity(){
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<DrinkTypes> drinks = orderElement.getDrinks();
        ArrayList<Menu> menus = orderElement.getMenus();

        if( drinks.size() < menus.size()){
            new AlertDialog.Builder(this.getContext())
                    .setMessage("Choisissez une boisson pour chaque menu :)")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
        else {

            if (DataManager.checkAllMandatoryOptions()) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        RecapActivity.class);
                getActivity().startActivity(intent);
            } else {
                new AlertDialog.Builder(this.getContext())
                        .setMessage("Choisissez Frites ou Potatoes pour chaque menu :)")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                pager.setCurrentItem(1);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();


            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                if (linearLayout == null) {
                    getActivity().setContentView(R.layout.recyclerview_activity_drink);
                    linearLayout =   (LinearLayout) (getActivity().findViewById(R.id.llRv_drink));
                    rv = (RecyclerView) getActivity().findViewById(R.id.rv_drink);
                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    rv.setLayoutManager(llm);
                    rv.setHasFixedSize(true);
                    goToRecapButton = (Button) getActivity().findViewById(R.id.DrinkActivity_NextButton);
                    goToRecapButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToRecapActivity();
                        }
                    });
                }
                initializeData();
                initializeAdapter();
                linearLayout.setVisibility(View.VISIBLE);
                goToRecapButton.setVisibility(View.VISIBLE);


            } else {
                if(rv!= null){
                    linearLayout.setVisibility(View.GONE);
                    goToRecapButton.setVisibility(View.GONE);
                }
            }
    }

    public void setPager(ViewPager pager) {
        this.pager = pager;
    }

}
