package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

/**
 * Created by Gasp on 27/01/16.
 */

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

import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;


public class RVAdapterMenu extends RecyclerView.Adapter<RVAdapterMenu.PersonViewHolder> {

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
            cv = (CardView)itemView.findViewById(R.id.cv_menu);
            name = (TextView)itemView.findViewById(R.id.name_item_menu);
            price = (TextView) itemView.findViewById(R.id.price_item_menu);
            itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo_menu);
            minusButton= (Button) itemView.findViewById(R.id.minusButton_menu);
            plusButton= (Button) itemView.findViewById(R.id.plusButton_menu);
            totalPrice = (TextView) itemView.findViewById(R.id.total_item_menu);
        }
    }


    List<Item> items;

    RVAdapterMenu(List<Item> items){
        this.items = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        int nbr = 0;
        for (Menu menu : Orders.getActiveOrder().getMenus()){
            if (menu.getFood().toString().toUpperCase().equals(items.get(i).name.toUpperCase())){
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
                    removeOneMenu(i);
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

    private void removeOneMenu(int pos) {
        String name  = items.get(pos).name;
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menus = orderElement.getMenus();
        Boolean menuAlreadyRemoved = false;

        //Start at the end of the list in order to remove the last added Kebab
        for(int i=menus.size()-1 ; i >= 0 ; i--){
            if (menus.get(i).getFood().toString().equals(name)){
                if (! menuAlreadyRemoved){
                    menuAlreadyRemoved = true;
                    menus.remove(menus.get(i));
                }
            }
        }

    }

    private void addOrder(int number, int i) {
        String name  = items.get(i).name;
        OrderElement orderElement = Orders.getActiveOrder();

        if (name.equals("Kebab")){
            addOneMenu(FoodTypes.KEBAB);
        }
        if (name.equals("Burger")){
            addOneMenu(FoodTypes.BURGER);
        }
        if (name.equals("Margherita")){
            addOneMenu(FoodTypes.MARGHERITA);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void addOneMenu(FoodTypes foodTypes){
        OrderElement orderElement = Orders.getActiveOrder();
        ArrayList<Menu> menus = orderElement.getMenus();
        Menu newOne = new Menu();
        newOne.setFood(foodTypes);
        menus.add(newOne);
    }
}
