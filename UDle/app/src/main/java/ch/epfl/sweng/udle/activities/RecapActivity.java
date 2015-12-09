package ch.epfl.sweng.udle.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import ch.epfl.sweng.udle.activities.MenuOptionsDrinks.MainActivity;
import ch.epfl.sweng.udle.network.DataManager;
import ch.epfl.sweng.udle.network.ParseUserOrderInformations;

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
    private String from = ""; //state if the activity has been launch directly by MapActivity default no (by MenuActivity)
    private LinearLayout expected_time_layout;
    private LinearLayout status_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlgAlert = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_recap);
        deliveryName = (TextView) findViewById(R.id.RecapActivity_deliveryName);
        deliveryAddress = (TextView) findViewById(R.id.RecapActivity_deliveryAddress);
        priceTextView = (TextView) findViewById(R.id.RecapActivity_totalCost);
        expected_time_layout = (LinearLayout) findViewById(R.id.RecapActivity_expected_time_layout);
        status_layout = (LinearLayout) findViewById(R.id.RecapActivity_status_layout);
        Button confirmButton = (Button) findViewById(R.id.RecapActivity_recapConfirm);

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
                dlgAlert.setMessage(getResources().getString(R.string.removeMessage_2) + ((HashMap<String, String>) listView.getAdapter().getItem(pos)).get("elem") + "\t?");
                dlgAlert.setTitle(R.string.removeMessage_1);
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteElement(pos, false);
                                dlgAlert.create().dismiss();
                            }
                        });
                dlgAlert.setNeutralButton("Delete All",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteElement(pos, true);
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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
             from = bundle.getString("from");
            if(from.equals("Map") || from.equals("Current")){
                confirmButton.setVisibility(View.GONE);
                String expectedTime = DataManager.getExpectedTime(order.getUserOrderInformationsID());
                if(!expectedTime.equals("-1")){
                    expected_time_layout.setVisibility(View.VISIBLE);
                    status_layout.setVisibility(View.VISIBLE);
                    TextView text = (TextView) findViewById(R.id.RecapActivity_expected_time);
                    text.setText(expectedTime);
                    TextView textStatus = (TextView) findViewById(R.id.RecapActivity_status);
                    textStatus.setText(R.string.enRoute);
                }
                else{
                    status_layout.setVisibility(View.VISIBLE);
                    TextView textStatus = (TextView) findViewById(R.id.RecapActivity_status);
                    textStatus.setText(R.string.WaitingOrders);
                }
            }
        }
        else{
            confirmButton.setVisibility(View.VISIBLE);
            expected_time_layout.setVisibility(View.GONE);
            status_layout.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed(){
        if(from.equals("Current")){
            Intent intent = new Intent(this, CurrentOrdersActivity.class);
            startActivity(intent);
        }
        else if(from.equals("Map")){
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

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
        Menu.displayInRecap(list, getResources().getString(R.string.noOptions),getResources().getString(R.string.options));
        DrinkTypes.displayInRecap(list);
        ((SimpleAdapter)listView.getAdapter()).notifyDataSetChanged();
        if (list.size() == 0){
            Toast.makeText(this, getString(R.string.NoMenuSelected), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    public void deleteElement(int pos, boolean deleteAll){
        boolean isMenu = false;
        int numberOfElement = 0;

        HashMap<String, String> elementToRemove = (HashMap<String, String>) listView.getAdapter().getItem(pos);

        String[] temp = elementToRemove.get("elem").split(" ");
        if (temp.length != 2)
            throw new IllegalArgumentException("elem not initialized correctly");
        numberOfElement = Integer.parseInt(temp[0]);
        String elem = temp[1];

        for (FoodTypes food : FoodTypes.values()) {
            if (elem.equals(food.toString()))
                isMenu = true;
        }
        if (!isMenu){
            //It's a drink
            for (DrinkTypes drink : DrinkTypes.values()) {
                if (elem.equals(drink.toString())){
                    if (numberOfElement > 1 && deleteAll){
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
            String optionOfElemToRemove = elementToRemove.get("options");
            if (optionOfElemToRemove.equals(getResources().getString(R.string.noOptions))){
                ArrayList<Menu> toDelete = new ArrayList<>();
                for(Menu menu : order.getOrder()) {
                    if (elem.equals(menu.getFood().toString())){
                        if(menu.getOptions().size()==0) {
                            if (deleteAll){
                                toDelete.add(menu);
                            }else {
                                toDelete.add(menu);
                                break;
                            }
                        }
                    }
                }
                // Now we suppress them
                for(Menu menu : toDelete) {
                    order.removeToFood(menu);
                }
            }else{
                if(optionOfElemToRemove.startsWith(getResources().getString(R.string.options))){
                    optionOfElemToRemove = optionOfElemToRemove.substring(getResources().getString(R.string.options).length());
                    String[] temp2 = optionOfElemToRemove.split(" ; ");
                    ArrayList<OptionsTypes> tempListOption = new ArrayList<>();
                    for (int j = 0; j < temp2.length; j++){
                        for (OptionsTypes options : OptionsTypes.values()) {
                            if (temp2[j].equals(options.toString()))
                                tempListOption.add(options);
                        }
                    }
                    ArrayList<Menu> toDelete = new ArrayList<>();
                    for(Menu menu : order.getOrder()) {
                        if (elem.equals(menu.getFood().toString())){
                            if(compareLists(menu.getOptions(),tempListOption)) {
                                if (deleteAll){
                                    toDelete.add(menu);
                                }else {
                                    toDelete.add(menu);
                                    break;
                                }
                            }
                        }
                    }
                    // Now we suppress them
                    for(Menu menu : toDelete) {
                        order.removeToFood(menu);
                    }
                }else
                    throw new IllegalArgumentException("elem options not set correctly");
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
