package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

/**
 * Created by rodri on 09/11/2015.
 */
public class Options_ExpandableListAdapter extends BaseExpandableListAdapter {

    LayoutInflater inflater;
    int nbrGroup = 0; //Here, the nbrGroup represents the numbers of menus to display.

    public Options_ExpandableListAdapter(LayoutInflater context){
        this.inflater = context;
    }


    //Group count is the numbers of menus.
    @Override
    public int getGroupCount() {
        int nbr = Orders.getActiveOrder().getOrder().size();

        if (nbr != nbrGroup){ //If a menu was added/removed, need to 'refresh' the options list
            nbrGroup = nbr;
            this.notifyDataSetChanged();
        }
        return nbr;
    }


    //Children are the options present in the enum 'OptionsTypes'
    @Override
    public int getChildrenCount(int groupPosition) {
        return OptionsTypes.values().length;
    }

    //Return the title of the group (e.g: #2 Burger)
    @Override
    public Object getGroup(int groupPosition) {
        return getOrders().get(groupPosition);
    }

    //Return the options string (e.g : Salad)
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return OptionsTypes.values()[childPosition].toString();
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_options_menu_title, parent, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.OptionsFragment_MenuTitle);
        parentTextView.setText(groupTitle);
        return convertView;
    }

    //Call when a menu is click.
    //Display the list of child (optionTypes) and configure his listener to add/remove options for the menu.
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_options_list_childs, parent, false);
        }
        final CheckBox childTextView = (CheckBox) convertView.findViewById(R.id.OptionsFragment_MenuElement);
        childTextView.setText(childTitle);

        //If the options is already added for the menu, need to check the checkbox
        Menu menu = Orders.getActiveOrder().getOrder().get(groupPosition);
        if (menu.getOptions().contains(OptionsTypes.values()[childPosition])){
            childTextView.setChecked(true);
        }
        else{
            childTextView.setChecked(false);
        }


        childTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Menu> menu = Orders.getActiveOrder().getOrder();
                if(childTextView.isChecked()){
                    menu.get(groupPosition).addToOptions(OptionsTypes.values()[childPosition]);
                }
                else{ //Remove from Options
                    menu.get(groupPosition).removeFromOptions(OptionsTypes.values()[childPosition]);
                }
            }
        });

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //Not used
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    //Not used
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    //Not used
    @Override
    public boolean hasStableIds() {
        return false;
    }


    //Return a list of orders name with the position on this list (e.g : #1 Burger, #2 Kebab, ...);
    public ArrayList<String> getOrders(){

        ArrayList<Menu> orders = Orders.getActiveOrder().getOrder();
        ArrayList<String> tmp = new ArrayList<>();

        int nbr = 1;
        for (Menu menu : orders){
            tmp.add("#"+String.valueOf(nbr)+"  "+menu.getFood().toString());
            nbr ++;
        }
        return tmp;
    }
}



