package ch.epfl.sweng.udle.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.SeekBar;

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
    }


    public void validateStar(View view) {
        DataManager.setRestaurantMarkWithActiveOrder(mark);

    }
}
