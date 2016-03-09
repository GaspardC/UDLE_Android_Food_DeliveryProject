package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

/**
 * Created by rodri on 09/11/2015.
 */
public class Options_ExpandableListAdapter extends BaseExpandableListAdapter {

    private final TextView infoTextView;
    LayoutInflater inflater;
    int nbrMenusToDisplay = 0;
    private boolean dontShowAlertAgain = false;
/*
    HashMap mapCheckbox = new HashMap(50);
*/


    public Options_ExpandableListAdapter(LayoutInflater context, TextView infoTextView){
        this.inflater = context;
        this.infoTextView = infoTextView;
    }


    //Group count is the numbers of menus.
    @Override
    public int getGroupCount() {

        int nbrMenus = Orders.getActiveOrder().getMenus().size();

        if (nbrMenus != nbrMenusToDisplay){ //If a menu was added/removed, need to 'refresh' the options list
            nbrMenusToDisplay = nbrMenus;
            this.notifyDataSetChanged();
        }

        return nbrMenusToDisplay;
    }


    //Children are the options present in the enum 'OptionsTypes'
    @Override
    public int getChildrenCount(int groupPosition) {

        if(getGroup(groupPosition).toString().contains("Margherita")){
            return 0;
        }
            return OptionsTypes.values().length;
    }

    //Return the title of the group (e.g: #2 Burger)
    @Override
    public Object getGroup(int groupPosition) {
        return getOrdersTitle().get(groupPosition);
    }

    //Return the options string (e.g : Salad)
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return OptionsTypes.values()[childPosition].toString();
    }


    //Display the title for each group
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_options_menu_title, parent, false);
        }
        Button plusButton = (Button) convertView.findViewById(R.id.plusButtonOptionsList);
        plusButton.setFocusable(false);
        TextView menuTitle = (TextView) convertView.findViewById(R.id.OptionsFragment_MenuTitle);
        menuTitle.setText(groupTitle);
        return convertView;
    }

    //Call when a menu is click.
    //Display the list of child (optionTypes) and configure his listener to add/remove options for the menu.
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_options_list_childs, parent, false);
        }
        final CheckBox optionTitleTextView = (CheckBox) convertView.findViewById(R.id.OptionsFragment_MenuElement);
        optionTitleTextView.setText(childTitle);
/*
        mapCheckbox.put(groupPosition * 100 + childPosition, optionTitleTextView);
*/

        //If the options is already added for the menu, need to check the checkbox
        Menu menu = Orders.getActiveOrder().getMenus().get(groupPosition);

        if (menu.getOptions().contains(OptionsTypes.values()[childPosition])){
            optionTitleTextView.setChecked(true);
        }
        else{
            optionTitleTextView.setChecked(false);
        }


        final View finalConvertView = convertView;
        optionTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoTextView.setVisibility(View.GONE);
                ArrayList<Menu> menu = Orders.getActiveOrder().getMenus();
                if (optionTitleTextView.isChecked()) {

                    /*Delete Frites if Potatoes selected and inversely*/
                    if( OptionsTypes.values()[childPosition].equals(OptionsTypes.Potatoes)){
                        if(menu.get(groupPosition).getOptions().contains(OptionsTypes.Frites)){
                            if(dontShowAlertAgain){
                                optionTitleTextView.setChecked(false);

                            }
                            else {


                                new AlertDialog.Builder(parent.getContext())
                                        .setMessage("Choisissez Frites ou Potatoes, pas les deux :)")
                                        .setPositiveButton("compris", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                optionTitleTextView.setChecked(false);
                                                dontShowAlertAgain = true;

                                            }
                                        })

                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                            }
                        }
                        else{
                            menu.get(groupPosition).addToOptions(OptionsTypes.values()[childPosition]);
                        }
                    }
                    else{
                        menu.get(groupPosition).addToOptions(OptionsTypes.values()[childPosition]);
                    }




                     /*Delete Potatoes if Frites selected and inversely*/
                    if( OptionsTypes.values()[childPosition].equals(OptionsTypes.Frites)){
                        if(menu.get(groupPosition).getOptions().contains(OptionsTypes.Potatoes)){

                            if(dontShowAlertAgain){
                                optionTitleTextView.setChecked(false);

                            }
                            else {
                                new AlertDialog.Builder(parent.getContext())
                                        .setMessage("Choisissez Frites ou Potatoes, pas les deux :)")
                                        .setPositiveButton("compris", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                                optionTitleTextView.setChecked(false);
                                                dontShowAlertAgain = true;
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
                            }
                        }
                        else{
                            menu.get(groupPosition).addToOptions(OptionsTypes.values()[childPosition]);
                        }
                    }
                    else{
                        menu.get(groupPosition).addToOptions(OptionsTypes.values()[childPosition]);
                    }


                } else { //Remove from Options
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
    public ArrayList<String> getOrdersTitle(){

        ArrayList<Menu> orders = Orders.getActiveOrder().getMenus();
        ArrayList<String> menusTitles = new ArrayList<>();

        int listOrdersIndex = 1;
        for (Menu menu : orders){
            menusTitles.add("#" + String.valueOf(listOrdersIndex) + "  " + menu.getFood().toString());
            listOrdersIndex ++;
        }
        return menusTitles;
    }
}



