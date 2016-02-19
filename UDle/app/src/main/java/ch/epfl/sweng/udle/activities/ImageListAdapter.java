package ch.epfl.sweng.udle.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import java.util.ArrayList;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;
import ch.epfl.sweng.udle.network.DataManager;

/**
 * Created by Gasp on 15/01/16.
 */

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Number> marksRestos = new ArrayList<>();
    ArrayList<ParseUser> nearbyRestaurants = new ArrayList<>();


    public ImageListAdapter(Context context, ArrayList<String> imageUrls, ArrayList<Number> marksRestos, ArrayList<ParseUser> nearbyRestaurants) {
        super(context, R.layout.listview_item_image_logo_restaurant, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        this.marksRestos = marksRestos;
        this.nearbyRestaurants = nearbyRestaurants;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        RatingBar ratingBar = null;

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image_logo_restaurant, parent, false);

        }
        imageView = (ImageView) convertView.findViewById(R.id.logoImageListDialog);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idResto = nearbyRestaurants.get(position).getObjectId();
                ArrayList<String> nearbyRestaurantsId = new ArrayList<>();
                nearbyRestaurantsId.add(idResto);

                DataManager.getUser().put("ArrayOfNearRestaurant", nearbyRestaurantsId);
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);

            }
        });
        ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarDialog);

        ratingBar.setRating( marksRestos.get(position).floatValue());


        Glide
                .with(context)
                .load(imageUrls.get(position))
                .into(imageView);

        return convertView;
    }
}