package ch.epfl.sweng.udle.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.network.DataManager;

public class StarActivity extends AppCompatActivity {

    private int mark = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mark = (int) rating;
            }
        });
        ImageView logo = (ImageView) findViewById(R.id.StarActivityimageBelow);
        ParseUser resto = DataManager.getRestaurantUserWithActiveOrder();
        ParseFile pLogo = resto.getParseFile("RestaurantLogo");
        String urlLogo = pLogo.getUrl();
        Glide.with(this).load(urlLogo)
                .centerCrop()
                .crossFade()
                .thumbnail(0.1f)
                .into(logo);

    }


    public void validateStar(View view) {
        DataManager.setRestaurantMarkWithActiveOrder(mark);

    }
}
