package ch.epfl.sweng.udle.activities;

import com.parse.ui.ParseLoginDispatchActivity;

public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return MapActivity.class;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dispatch);
//    }
}
