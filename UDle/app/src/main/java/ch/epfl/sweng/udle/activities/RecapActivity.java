package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.udle.Food.DrinkTypes;
import ch.epfl.sweng.udle.Food.FoodTypes;
import ch.epfl.sweng.udle.Food.Menu;
import ch.epfl.sweng.udle.Food.OptionsTypes;
import ch.epfl.sweng.udle.Food.OrderElement;
import ch.epfl.sweng.udle.Food.Orders;
import ch.epfl.sweng.udle.R;
import ch.epfl.sweng.udle.activities.SlideMenu.SlideMenuActivity;

public class RecapActivity extends SlideMenuActivity {
    AlertDialog.Builder dlgAlert;
    boolean deleteAll = false;
    private ListView listView;
    private OrderElement order;
    private ListAdapter adapter;
    private TextView deliveryName;
    private TextView deliveryAddress;
    private TextView priceTextView;
    private List<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlgAlert = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_recap);
        deliveryName = (TextView) findViewById(R.id.RecapActivity_deliveryName);
        deliveryAddress = (TextView) findViewById(R.id.RecapActivity_deliveryAddress);
        priceTextView = (TextView) findViewById(R.id.RecapActivity_totalCost);

        list = new ArrayList<>();

        adapter = new SimpleAdapter(this, list,
                                                R.layout.recap_elem_with_price,
                                                new String[] {"elem", "price", "options"},
                                                new int[] {R.id.RecapElem, R.id.RecapPriceElem, R.id.RecapOptionsString});
        listView = (ListView) findViewById(R.id.RecapActivity_recapListView);
        listView.setAdapter(adapter);
        update();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                dlgAlert.setMessage(((HashMap<String, String>) listView.getAdapter().getItem(pos)).get("elem") + "\t?");
                dlgAlert.setTitle(R.string.removeMessage_1);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteElement(pos);
                                dlgAlert.create().dismiss();
                            }
                        });
                dlgAlert.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dlgAlert.create().dismiss();
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                return true;
            }
        });
    }
    protected void update(){
        order = Orders.getActiveOrder();
        String moneyDevise = Orders.getMoneyDevise();
        String userName = order.getOrderedUserName();
        deliveryName.setText(userName);
        String address = order.getDeliveryAddress();
        deliveryAddress.setText(address);
        priceTextView.setText(String.format("%.2f", order.getTotalCost()) + moneyDevise);
        list.clear();
        Menu.displayInRecap(list);
        DrinkTypes.displayInRecap(list);
        ((SimpleAdapter)listView.getAdapter()).notifyDataSetChanged();
    }
    public void deleteElement(int pos){
        int numberOfElement = 0;
        boolean isMenu = false;
        setDeleteAll(false);
        HashMap<String, String> elementToRemove = (HashMap<String, String>) listView.getAdapter().getItem(pos);

        String[] temp = elementToRemove.get("elem").split(" ");
        if (temp.length != 2)
            throw new IllegalArgumentException("elem not initialized correctly");

        numberOfElement = Integer.parseInt(temp[0]);
        String elem = temp[1];
        //Toast.makeText(this, " "+numberOfElement, Toast.LENGTH_SHORT).show();

        for (FoodTypes food : FoodTypes.values()) {
            if (elem.equals(food.toString()))
                isMenu = true;
        }
        if (!isMenu){
            //It's a drink
            for (DrinkTypes drink : DrinkTypes.values()) {
                if (elem.equals(drink.toString())){
                    if (numberOfElement > 1){
                        for (int i = 0; i < numberOfElement; i++) {
                            order.removeToDrinks(drink);
                        }
                    }else{
                        order.removeToDrinks(drink);
                    }
                }
            }
        }else{
            // It's a menu
            String[] temp2 = elementToRemove.get("options").split(" ; ");
            ArrayList<OptionsTypes> tempListOption = new ArrayList<>();
            for (int j = 0; j < temp2.length; j++){
                for (OptionsTypes options : OptionsTypes.values()) {
                    if (temp2[j].equals(options.toString()))
                        tempListOption.add(options);
                }
            }
            for(Menu menu : order.getOrder()) {
                if (elem.equals(menu.getFood().toString())){
                    if(compareLists(menu.getOptions(),tempListOption)) {
                        order.removeToFood(menu);
                        break;
                    }
                }
            }
        }
        update();
    }

    public boolean compareLists(ArrayList<OptionsTypes> a, ArrayList<OptionsTypes> b){
        if (a == null && b == null) return true;
        if ((a.size() != b.size()) || (a == null && b!= null) || (a != null && b== null)){
            return false;
        }
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }

    public void setDeleteAll(boolean bool){
        deleteAll = bool;
    }
    public void gotToPaymentActivity(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}
