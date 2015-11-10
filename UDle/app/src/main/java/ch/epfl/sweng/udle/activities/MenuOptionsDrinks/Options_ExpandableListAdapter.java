package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;

/**
 * Created by rodri on 09/11/2015.
 */
public class Options_ExpandableListAdapter extends BaseExpandableListAdapter {

    LayoutInflater inflater;
    HashMap<String, List<String>> menuOptions;
    ArrayList<Menu> orderList = Orders.getActiveOrder().getOrder();

    public Options_ExpandableListAdapter(LayoutInflater context, HashMap<String, List<String>> hashMap){
        this.inflater = context;
        this.menuOptions = hashMap;
    }




    @Override
    public int getGroupCount() {
        return orderList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return OptionsTypes.values().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderList.get(groupPosition).getFood().toString();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return OptionsTypes.values()[childPosition].toString();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.activity_options_list_childs, parent, false);
        }
        TextView childTextView = (TextView) convertView.findViewById(R.id.OptionsFragment_MenuElement);
        childTextView.setText(childTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
