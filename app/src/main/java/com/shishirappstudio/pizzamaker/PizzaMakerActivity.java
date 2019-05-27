package com.shishirappstudio.pizzamaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PizzaMakerActivity extends BaseActivity {

    @BindView(R.id.totalPriceTv)
    TextView totalPriceTv;
    @BindView(R.id.pizzaSizeTv)
    TextView pizzaSizeTv;
    @BindView(R.id.pizzaTypeTv)
    TextView pizzaTypeTv;
    @BindView(R.id.cheeseTypeTv)
    TextView cheeseTypeTv;
    @BindView(R.id.sauceTypeTv)
    TextView sauceTypeTv;
    @BindView(R.id.toppingsTv)
    TextView toppingsTv;

    // Data for each type......................
    CharSequence[] DATA_PIZZA_SIZE = {"Regular", "Medium", "Large"};
    CharSequence[] DATA_PIZZA_TYPE = {"Thin Crust", "Pan", "Chicago"};
    CharSequence[] DATA_CHEESE_TYPE = {"White", "Brown"};
    CharSequence[] DATA_SAUCE_TYPE = {"Tomato", "BBQ", "Marinara"};
    CharSequence[] DATA_TOPPINGS = {"Onion", "Pepperoni", "Green Pepper"};

    /// Price for each item..................
    Map<String, Integer> PRICE_PIZZA_SIZE = new HashMap<String, Integer>() {{
        put("Regular", 40);
        put("Medium", 60);
        put("Large", 70);
    }};

    Map<String, Integer> PRICE_PIZZA_TYPE = new HashMap<String, Integer>() {{
        put("Thin Crust", 50);
        put("Pan", 40);
        put("Chicago", 60);
    }};

    Map<String, Integer> PRICE_CHEESE = new HashMap<String, Integer>() {{
        put("White", 10);
        put("Brown", 20);
    }};

    Map<String, Integer> PRICE_SAUCE = new HashMap<String, Integer>() {{
        put("Tomato", 20);
        put("BBQ", 15);
        put("Marinara", 30);
    }};
    Map<String, Integer> PRICE_TOPPINGS = new HashMap<String, Integer>() {{
        put("Onion", 5);
        put("Pepperoni", 30);
        put("Green Pepper", 20);
    }};

    // Initially all topping are selected....
    final boolean[] currentlySelectedToppings = new boolean[]{true, true, true};

    enum PickerType {
        PIZZA_SIZE,
        PIZZA_TYPE,
        CHEESE_TYPE,
        SAUCE
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pizza_maker;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent data) {
        this.setTitle("Create your Pizza!");
        calculatePrice(); // Initially calculating the total price for the default items...
    }

    @OnClick({R.id.pizzaSizeRow, R.id.pizzaTypeRow, R.id.cheeseTypeRow, R.id.sauceTypeRow, R.id.toppingRow})
    public void onRowDidTapped(View view) {
        switch (view.getId()) {
            case R.id.pizzaSizeRow:
                showDataPicker(PickerType.PIZZA_SIZE,
                        "Pizza Size", DATA_PIZZA_SIZE,
                        Arrays.asList(DATA_PIZZA_SIZE).indexOf(pizzaSizeTv.getText().toString()));
                break;
            case R.id.pizzaTypeRow:
                showDataPicker(PickerType.PIZZA_TYPE,
                        "Pizza Type", DATA_PIZZA_TYPE,
                        Arrays.asList(DATA_PIZZA_TYPE).indexOf(pizzaTypeTv.getText().toString()));
                break;
            case R.id.cheeseTypeRow:
                showDataPicker(PickerType.CHEESE_TYPE,
                        "Cheese Type", DATA_CHEESE_TYPE,
                        Arrays.asList(DATA_CHEESE_TYPE).indexOf(cheeseTypeTv.getText().toString()));
                break;
            case R.id.sauceTypeRow:
                showDataPicker(PickerType.SAUCE,
                        "Sauce", DATA_SAUCE_TYPE,
                        Arrays.asList(DATA_SAUCE_TYPE).indexOf(sauceTypeTv.getText().toString()));
                break;
            case R.id.toppingRow:
                AlertDialog.Builder toppingsPickerBuilder = new AlertDialog.Builder(PizzaMakerActivity.this);
                toppingsPickerBuilder.setCancelable(true);
                toppingsPickerBuilder.setTitle("Toppings");
                toppingsPickerBuilder.setMultiChoiceItems(DATA_TOPPINGS, currentlySelectedToppings, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemPosition, boolean isChecked) {
                        currentlySelectedToppings[itemPosition] = isChecked; // Updating the currently selected toppings list....
                        StringBuilder selectedToppingItems = new StringBuilder();
                        for (int i = 0; i < DATA_TOPPINGS.length; i++) {
                            if (currentlySelectedToppings[i]) {
                                selectedToppingItems.append(DATA_TOPPINGS[i]).append("\n");
                            }
                        }
                        toppingsTv.setText(selectedToppingItems.toString());
                        calculatePrice(); // Calculating price on each toppings change..........
                    }
                });
                toppingsPickerBuilder.setPositiveButton("DONE", null);
                toppingsPickerBuilder.create().show();
                break;
        }
    }

    void showDataPicker(final PickerType pickerType, String pickerTitle, final CharSequence[] items, int checkedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PizzaMakerActivity.this);
        builder.setCancelable(true);
        builder.setTitle(pickerTitle);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itemPosition) {
                switch (pickerType) {
                    case PIZZA_SIZE:
                        pizzaSizeTv.setText(items[itemPosition]);
                        break;
                    case PIZZA_TYPE:
                        pizzaTypeTv.setText(items[itemPosition]);
                        break;
                    case CHEESE_TYPE:
                        cheeseTypeTv.setText(items[itemPosition]);
                        break;
                    case SAUCE:
                        sauceTypeTv.setText(items[itemPosition]);
                        break;
                }
                calculatePrice();  // Calculating price on each item change..........
            }
        });
        builder.setPositiveButton("DONE", null);
        builder.create().show();
    }

    void calculatePrice() {
        int priceForPizzaSize = Integer.parseInt(PRICE_PIZZA_SIZE.get(pizzaSizeTv.getText().toString()).toString());
        int priceForPizzaType = Integer.parseInt(PRICE_PIZZA_TYPE.get(pizzaTypeTv.getText().toString()).toString());
        int priceForCheeseType = Integer.parseInt(PRICE_CHEESE.get(cheeseTypeTv.getText().toString()).toString());
        int priceForSauce = Integer.parseInt(PRICE_SAUCE.get(sauceTypeTv.getText().toString()).toString());
        int priceForToppings = 0;
        for (int i = 0; i < DATA_TOPPINGS.length; i++) {
            if (currentlySelectedToppings[i]) {
                priceForToppings += Integer.parseInt(PRICE_TOPPINGS.get(DATA_TOPPINGS[i]).toString());
            }
        }
        String totalPrice = "TOTAL: " + (priceForPizzaSize + priceForPizzaType + priceForCheeseType + priceForSauce + priceForToppings);
        totalPriceTv.setText(totalPrice);
    }
}
