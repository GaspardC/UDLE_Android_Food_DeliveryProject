package ch.epfl.sweng.udle.activities.MenuOptionsDrinks;

import android.widget.TextView;

import ch.epfl.sweng.udle.Food.Orders;


class Item {
    String name;
    int price;
    int photoId;
    int number;
    private TextView totalTextView ;
    private TextView nameTextView;
    private String text;
    private int totalName;

    Item(String name, int price, int photoId, int number) {
        this.name = name;
        this.price = price;
        this.photoId = photoId;
        this.number = number;
    }

    public void setTotal (int i){
        number = i;
    }

    public void setTotalTextView(TextView totalTextView) {
        this.totalTextView = totalTextView;
    }

    public void setText(int number) {
        this.text = number*  price + Orders.getMoneyDevise();
        this.totalTextView.setText(text);
    }

    public void setTotalName(int totalName) {
        Integer integer = new Integer(totalName);
        nameTextView.setText(integer.toString()+"x " + name);
    }

    public void setNameTextView(TextView nameTextView) {
        this.nameTextView = nameTextView;
    }
}