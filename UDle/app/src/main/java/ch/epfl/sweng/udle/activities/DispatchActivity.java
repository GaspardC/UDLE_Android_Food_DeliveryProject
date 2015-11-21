package ch.epfl.sweng.udle.activities;

import android.os.Bundle;

import com.parse.ui.ParseLoginDispatchActivity;

import ch.epfl.sweng.udle.R;

/*
* This activity is a safeguard to prevent the user to go to MapActivity at the beginning if he's not
* logged in he goes to profile activity*/
public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return MapActivity.class;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//    }
}
