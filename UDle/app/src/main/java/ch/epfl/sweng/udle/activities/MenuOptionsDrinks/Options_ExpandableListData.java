package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.Orders;

public class Options_ExpandableListData {

    public static HashMap getData() {

        HashMap expandableListDetail = new HashMap();

        ArrayList<Menu> orderList = Orders.getActiveOrder().getOrder();

        for(Menu menu : orderList){
            List food = new ArrayList();
            for(OptionsTypes option : OptionsTypes.values()){
                food.add(option.toString());
            }
            expandableListDetail.put(menu.getFood().toString(), food);
        }

        return expandableListDetail;
    }
}