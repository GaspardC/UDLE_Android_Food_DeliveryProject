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
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.RecapActivity;


public class DrinkFragment extends Fragment {

    private LinearLayout lLayout;

    private RecyclerView rv;
    private List<Item> items;
    private Button goToRecapButton;
    private LinearLayout linearLayout;


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
        items.add(new Item("Coca", 2, R.drawable.coca_cola,number));
        items.add(new Item("Orangina",2, R.drawable.orangina,number));
        items.add(new Item("Water", 2, R.drawable.evian,number));
    }

    private void initializeAdapter(){
        RVAdapterDrinks adapter = new RVAdapterDrinks(items);
        rv.setAdapter(adapter);
    }




    public void goToRecapActivity(){
        Intent intent = new Intent(getActivity().getBaseContext(),
                RecapActivity.class);
        getActivity().startActivity(intent);
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
}
