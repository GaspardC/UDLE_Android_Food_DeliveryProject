package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

public class RecyclerViewActivity extends Activity {

    private List<Item> items;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recyclerview_activity);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
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
}
