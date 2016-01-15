package ch.epfl.sweng.udle.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ch.epfl.sweng.udle.R;

/**
 * Created by Gasp on 15/01/16.
 */

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Number> marksRestos = new ArrayList<>();


    public ImageListAdapter(Context context, ArrayList<String> imageUrls, ArrayList<Number> marksRestos) {
        super(context, R.layout.listview_item_image_logo_restaurant, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        this.marksRestos = marksRestos;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        RatingBar ratingBar = null;

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image_logo_restaurant, parent, false);

        }
        imageView = (ImageView) convertView.findViewById(R.id.logoImageListDialog);
        ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarDialog);

        ratingBar.setRating( marksRestos.get(position).floatValue());


        Glide
                .with(context)
                .load(imageUrls.get(position))
                .into(imageView);

        return convertView;
    }
}