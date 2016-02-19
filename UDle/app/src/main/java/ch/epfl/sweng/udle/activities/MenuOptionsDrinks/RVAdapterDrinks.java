package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

public class RVAdapterDrinks extends RecyclerView.Adapter<RVAdapterDrinks.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView price;
        ImageView itemPhoto;
        Button minusButton;
        Button plusButton;
        TextView totalPrice;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_drink);
            name = (TextView)itemView.findViewById(R.id.name_item_drink);
            price = (TextView) itemView.findViewById(R.id.price_item_drink);
            itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo_drink);
            minusButton= (Button) itemView.findViewById(R.id.minusButton_drink);
            plusButton= (Button) itemView.findViewById(R.id.plusButton_drink);
            totalPrice = (TextView) itemView.findViewById(R.id.total_item_drink);
        }
    }

    List<Item> items;

    RVAdapterDrinks(List<Item> items){
        this.items = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drink, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        int nbr = 0;
        for (DrinkTypes drink :Orders.getActiveOrder().getDrinks()){
            if (drink.toString().toUpperCase().equals(items.get(i).name.toUpperCase())){
                nbr ++;
            }
        }


        personViewHolder.name.setText(items.get(i).name);
        items.get(i).setTotalTextView(personViewHolder.totalPrice);
        items.get(i).setNameTextView(personViewHolder.name);
        personViewHolder.itemPhoto.setImageResource(items.get(i).photoId);

        if(nbr != 0) {
            items.get(i).setTotal(nbr);
            items.get(i).setText(nbr);
            items.get(i).setTotalName(nbr);
        }

        personViewHolder.itemPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button", "click" + i);
                int number = items.get(i).number;
                number++;
                items.get(i).setTotal(number);
                items.get(i).setText(number);
                items.get(i).setTotalName(number);
                addOrder(number,i);
            }
        });
      personViewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              int number = items.get(i).number;
              if(number > 0) {
                  number--;
                  items.get(i).setTotal(number);
                  items.get(i).setTotalName(number);
                  items.get(i).setText(number);
                  removeOrder(i);
              }
          }
      });

        personViewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button", "click" + i);
                int number = items.get(i).number;
                number++;
                items.get(i).setTotal(number);
                items.get(i).setText(number);
                items.get(i).setTotalName(number);
                addOrder(number,i);
            }
        });

    }

    private void removeOrder(int pos) {
        String name  = items.get(pos).name;
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<DrinkTypes> drinks = orderElement.getDrinks();
        boolean alreadyRemove = false;
        for(int i=0; i<drinks.size();i++){
            if (! alreadyRemove) {
                if (drinks.get(i).toString().equals(name)) {
                    alreadyRemove = true;
                    drinks.remove(drinks.get(i));
                }
            }
        }

    }

    private void addOrder(int number, int i) {
        String name  = items.get(i).name;
        OrderElement orderElement = Orders.getActiveOrder();

        if (name.equals("Coca")){
            orderElement.addToDrinks(DrinkTypes.COCA);
        }
        if (name.equals("Orangina")){
            orderElement.addToDrinks(DrinkTypes.ORANGINA);
        }
        if (name.equals("Water")){
            orderElement.addToDrinks(DrinkTypes.WATER);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
