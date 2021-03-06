package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.udle.R;

public class OptionsFragment extends Fragment {

    private LinearLayout layout;
    private ViewPager pager;
    private Options_ExpandableListAdapter adapter;
    private Button buttonNext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        layout = (LinearLayout)    inflater.inflate(R.layout.activity_options, container, false);
        buttonNext = (Button) layout.findViewById(R.id.optionNext);
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                goToDrinkActivity();
            }
        });

        ExpandableListView expandableListView = (ExpandableListView) layout.findViewById(R.id.MenuElement_list);

        TextView infoTextView = (TextView) layout.findViewById(R.id.clickOnYourMeal);
        ImageView dottedLIne = (ImageView) layout.findViewById(R.id.dottedLine);
        dottedLIne.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        adapter = new Options_ExpandableListAdapter(inflater,infoTextView);
        expandableListView.setAdapter(adapter);



        return layout;

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            adapter.notifyDataSetChanged();
            buttonNext.setVisibility(View.VISIBLE);
        }
        else {
            if(layout!=null){
                buttonNext.setVisibility(View.GONE);
            }

        }
    }


    /** Called when the user clicks the MapActivity button */
    public void goToDrinkActivity() {
        pager.setCurrentItem(2);
    }

    public void setPager(ViewPager pager){
        this.pager = pager;
    }
}
