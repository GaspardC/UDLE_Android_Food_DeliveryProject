package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.udle.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView price;
        ImageView itemPhoto;
        Button minusButton;
        TextView totalPrice;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            name = (TextView)itemView.findViewById(R.id.name_item);
            price = (TextView) itemView.findViewById(R.id.price_item);
            itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo);
            minusButton= (Button) itemView.findViewById(R.id.minusButton);
            totalPrice = (TextView) itemView.findViewById(R.id.total_item);
        }
    }

    List<Item> items;

    RVAdapter(List<Item> items){
        this.items = items;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {
        personViewHolder.name.setText(items.get(i).name);
        items.get(i).setTotalTextView(personViewHolder.totalPrice);
        items.get(i).setNameTextView(personViewHolder.name);
        personViewHolder.itemPhoto.setImageResource(items.get(i).photoId);
        personViewHolder.itemPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button", "click" + i);
                int number = items.get(i).number;
                number++;
                items.get(i).setTotal(number);
                items.get(i).setText(number);
                items.get(i).setTotalName(number);
            }
        });
      personViewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              int number = items.get(i).number;
              if(number > 0) {
                  number--;
                  items.get(i).setTotal(number);
                  items.get(i).setText(number);
              }
          }
      });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
